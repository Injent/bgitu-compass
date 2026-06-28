package ru.bgitu.app.feature.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.app.core.data.TeachersRepository
import ru.bgitu.app.core.data.liveupdate.LiveUpdateNotificationManager
import ru.bgitu.app.core.data.notifications.FirebaseNotificationsRepository
import ru.bgitu.app.core.data.remoteconfig.RemoteConfigRepository
import ru.bgitu.app.core.data.schedule.GroupsRepository
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.model.SubscribeTopic
import ru.bgitu.app.core.model.UiTheme
import kotlin.time.Instant

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val groupsRepository: GroupsRepository,
    private val notificationsRepository: FirebaseNotificationsRepository,
    private val teachersRepository: TeachersRepository,
    private val liveUpdateNotificationManager: LiveUpdateNotificationManager,
    remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    private val notifications = combine(
        notificationsRepository.subscribedTopics,
        notificationsRepository.isNotificationsEnabled
    ) { topics, isNotificationsEnabled ->
        topics to isNotificationsEnabled
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.data,
        groupsRepository.groups,
        notifications,
        teachersRepository.userIsTeacher,
        remoteConfigRepository.config
    ) { prefs, userGroups, (subscribedTopics, isNotificationsEnabled), userIsTeacher, config ->
            UserEditableSettings(
                uiTheme = prefs.uiTheme,
                highQualityBlur = prefs.highQualityBlur,
                blurEnabled = prefs.blueEnabled,
                liveUpdateNotification = prefs.liveUpdateNotification,
                liveUpdateGroup = userGroups.find { it.id == prefs.liveUpdateGroupId },
                liveUpdateTriggerTime = prefs.liveUpdateNotificationTriggerTime,
                userGroups = userGroups.toImmutableList(),
                subscribedTopics = subscribedTopics.toImmutableSet(),
                isNotificationsEnabled = isNotificationsEnabled,
                userIsTeacher = userIsTeacher,
                vkLinkSupport = config.vkLinkSupport,
                maxLinkSupport = config.maxLinkSupport,
                telegramLinkSupport = config.telegramLinkSupport,
                syncAllGroups = prefs.syncAllGroups,
                shouldShowTomorrowSchedule = prefs.shouldShowTomorrowSchedule
            ).let { SettingsUiState.Success(it) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading
        )

    fun updateUiTheme(uiTheme: UiTheme) {
        viewModelScope.launch {
            settingsRepository.setUiTheme(uiTheme)
        }
    }

    fun updateBlurQuality(highQualityBlur: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHighQualityBlur(highQualityBlur)
        }
    }

    fun updateBlurEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBlurEnabled(enabled)
        }
    }

    fun setLiveUpdateNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                liveUpdateNotificationManager.enable()
            } else {
                liveUpdateNotificationManager.disable()
            }
        }
    }

    fun subscribeToTopic(topic: SubscribeTopic) {
        viewModelScope.launch {
            notificationsRepository.subscribeToTopic(topic)
        }
    }

    fun unsubscribeFromTopic(topic: SubscribeTopic) {
        viewModelScope.launch {
            notificationsRepository.unsubscribeFromTopic(topic)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                notificationsRepository.enableNotifications()
            } else {
                notificationsRepository.disableNotifications()
            }
        }
    }

    fun setLiveUpdateGroup(group: Group) {
        viewModelScope.launch {
            groupsRepository.setLiveUpdateGroup(group)
            liveUpdateNotificationManager.restart()
        }
    }

    fun setUserIsTeacher(isTeacher: Boolean) {
        viewModelScope.launch {
            teachersRepository.setUserIsTeacher(isTeacher)
        }
    }

    fun setSyncAllGroups(syncAllGroups: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSyncAllGroups(syncAllGroups)
        }
    }

    fun onSetShouldShowTomorrowSchedule(shouldShowTomorrowSchedule: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShouldShowTomorrowSchedule(shouldShowTomorrowSchedule)
        }
    }
}

data class UserEditableSettings(
    val uiTheme: UiTheme,
    val highQualityBlur: Boolean,
    val blurEnabled: Boolean,
    val liveUpdateNotification: Boolean,
    val liveUpdateTriggerTime: Instant?,
    val liveUpdateGroup: Group?,
    val userGroups: ImmutableList<Group>,
    val subscribedTopics: ImmutableSet<SubscribeTopic>,
    val isNotificationsEnabled: Boolean,
    val userIsTeacher: Boolean,
    val vkLinkSupport: String?,
    val maxLinkSupport: String?,
    val telegramLinkSupport: String?,
    val syncAllGroups: Boolean,
    val shouldShowTomorrowSchedule: Boolean
)

@Immutable
sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}