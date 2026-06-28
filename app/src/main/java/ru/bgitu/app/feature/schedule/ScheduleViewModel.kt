package ru.bgitu.app.feature.schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import ru.bgitu.app.core.data.appupdater.AppUpdateManager
import ru.bgitu.app.core.data.remoteconfig.RemoteConfigRepository
import ru.bgitu.app.core.data.schedule.GroupsRepository
import ru.bgitu.app.core.data.schedule.ScheduleRepository
import ru.bgitu.app.core.data.schedule.ScheduleSource
import ru.bgitu.app.core.data.util.AcademicWeekProvider
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.model.Schedule
import ru.bgitu.app.core.util.OneTimeEvent
import ru.bgitu.app.core.util.now
import ru.bgitu.app.core.util.trigger
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

class ScheduleViewModel(
    private val settingsRepository: SettingsRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val academicWeekProvider: AcademicWeekProvider,
    private val scheduleRepository: ScheduleRepository,
    appUpdateManager: AppUpdateManager,
    groupsRepository: GroupsRepository,
    savedStateHandle: SavedStateHandle,
    private val params: Params
) : ViewModel() {

    private val shouldShowTomorrowSchedule = settingsRepository.data.mapLatest { it.shouldShowTomorrowSchedule }
    private var lastDateUpdateMillis by savedStateHandle.saved<Long?>(KEY_LAST_DATE_UPDATE_TIME) { null }
    private val selectedGroupId = savedStateHandle.getMutableStateFlow(KEY_GROUP_ID, params.groupId)
    private val _selectedDate =
        savedStateHandle.getMutableStateFlow(KEY_START_DATE, params.date ?: LocalDate.now())
    val startDate = _selectedDate.asStateFlow()

    val navigateToGroupSearchEvent = OneTimeEvent<Unit>()
    val setSelectedDateEvent = OneTimeEvent<LocalDate>()

    @ExperimentalCoroutinesApi
    val uiState: StateFlow<ScheduleUiState> = combine(
        selectedGroupId,
        groupsRepository.groups,
    ) { groupId, groups ->
        val effectiveGroupId = if (groups.any { it.id == groupId }) {
            groupId ?: groups.firstOrNull()?.id
        } else {
            groups.firstOrNull()?.id
        }

        if (groupId == null && effectiveGroupId != null) {
            selectedGroupId.value = effectiveGroupId
        }
        effectiveGroupId
    }
        .distinctUntilChanged()
        .flatMapLatest { groupId ->
            if (groupId == null) {
                combine(
                    settingsRepository.data.map { it.showNotificationsDialog },
                    academicWeekProvider.termStartDate
                ) { showNotificationsDialog, termStartDate ->
                    ScheduleUiState(
                        loading = false,
                        showNotificationsDialog = showNotificationsDialog,
                        termStartDate = termStartDate
                    )
                }
            } else {
                checkAndResetDate(groupId)
                combine(
                    scheduleRepository.getScheduleFlow(groupId = groupId),
                    groupsRepository.groups,
                    settingsRepository.data.map { it.showNotificationsDialog }.distinctUntilChanged(),
                    academicWeekProvider.termStartDate
                ) { scheduleSource, groups, showNotificationsDialog, termStartDate ->
                    val selectedGroup = groups.find { it.id == groupId }

                    ScheduleUiState(
                        scheduleState = when (scheduleSource) {
                            is ScheduleSource.Failed -> ScheduleState.Failed(scheduleSource.throwable)
                            is ScheduleSource.Loading -> ScheduleState.Loading
                            is ScheduleSource.Success -> ScheduleState.Success(scheduleSource.schedule)
                            is ScheduleSource.Dropped -> ScheduleState.Dropped
                        },
                        groups = groups.toImmutableList(),
                        selectedGroup = selectedGroup,
                        updatedAt = (scheduleSource as? ScheduleSource.Success)?.updatedAt,
                        refreshing = (scheduleSource as? ScheduleSource.Success)?.refreshing == true,
                        termStartDate = termStartDate,
                        showNotificationsDialog = showNotificationsDialog,
                        loading = false
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ScheduleUiState()
        )

    fun onSelectGroup(group: Group) {
        selectedGroupId.value = group.id
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
        lastDateUpdateMillis = Clock.System.now().toEpochMilliseconds()
    }

    fun onScheduleDropped() {
        viewModelScope.launch {
            remoteConfigRepository.config.first().lastResetTimestamp?.let { timeResetAt ->
                settingsRepository.resetData(timeResetAt)
            }
            navigateToGroupSearchEvent.trigger()
        }
    }

    fun dismissNotificationsDialog() {
        viewModelScope.launch {
            settingsRepository.hideNotificationsDialog()
        }
    }

    private suspend fun checkAndResetDate(groupId: Int) {
        if (params.date != null) {
            return
        }
        val now = Clock.System.now()
        val nowMillis = now.toEpochMilliseconds()

        val lastUpdate = lastDateUpdateMillis
        if (lastUpdate != null && (nowMillis - lastUpdate < 1.hours.inWholeMilliseconds)) {
            return
        }

        val today = LocalDate.now()
        var targetDate = today

        if (shouldShowTomorrowSchedule.first()) {
            val todayLessons = scheduleRepository.getLessonsByDate(groupId, today)
            val lastLessonEnd = todayLessons?.lastOrNull()?.endAt
            if (todayLessons.isNullOrEmpty() || (lastLessonEnd != null && (now - 1.hours).toLocalDateTime(TimeZone.currentSystemDefault()).time > lastLessonEnd)) {
                targetDate = today.plus(1, DateTimeUnit.DAY)
            }
        }

        if (_selectedDate.value != targetDate) {
            _selectedDate.value = targetDate
            setSelectedDateEvent.send(targetDate)
        }
        lastDateUpdateMillis = nowMillis
    }

    data class Params(val date: LocalDate?, val groupId: Int?)
}

data class ScheduleUiState(
    val selectedGroup: Group? = null,
    val groups: ImmutableList<Group> = persistentListOf(),
    val scheduleState: ScheduleState = ScheduleState.Loading,
    val updatedAt: Instant? = null,
    val refreshing: Boolean = false,
    val termStartDate: LocalDate = LocalDate.now(),
    val showNotificationsDialog: Boolean = false,
    val loading: Boolean = true
)

sealed interface ScheduleState {
    data class Success(val schedule: Schedule) : ScheduleState
    data object Loading : ScheduleState
    data class Failed(val throwable: Throwable) : ScheduleState
    data object Dropped : ScheduleState
}

private const val KEY_GROUP_ID = "groupId"
private const val KEY_START_DATE = "startDate"
private const val KEY_LAST_DATE_UPDATE_TIME = "lastDateUpdateTime"