package ru.bgitu.app.feature.groups

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pro.respawn.apiresult.map
import pro.respawn.apiresult.onError
import pro.respawn.apiresult.onSuccess
import pro.respawn.apiresult.orElse
import ru.bgitu.app.core.analytics.CrashAnalytics
import ru.bgitu.app.core.common.exception.NetworkException
import ru.bgitu.app.core.data.schedule.GroupsRepository
import ru.bgitu.app.core.data.util.NetworkMonitor
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.util.OneTimeEvent
import ru.bgitu.app.core.util.searchAndSort

class GroupsSearchViewModel(
    private val groupsRepository: GroupsRepository,
    networkMonitor: NetworkMonitor,
) : ViewModel() {
    private val mutex = Mutex()
    val searchState = TextFieldState()
    private val allGroups = combine(
        networkMonitor.isOnline.onStart { emit(true) },
        groupsRepository.groups
    ) { _, userGroups ->
        groupsRepository.search("")
            .onError { e -> CrashAnalytics.e(e, "Failed to get all groups") }
            .map { groups ->
                if (groups.isEmpty()) return@map null
                groups.filter { it !in userGroups }
            }
    }
    val navigateBackEvent = OneTimeEvent<Unit>()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState = combine(
        networkMonitor.isOnline.onStart { emit(true) },
        allGroups,
        snapshotFlow { searchState.text }.debounce(300),
    ) { isOnline, allGroups, query ->
        if (!isOnline) return@combine GroupsSearchUiState.HasNoInternet

        val groups = allGroups
            .map { groups ->
                if (groups == null) return@combine GroupsSearchUiState.NotPublishedYet
                if (query.isBlank()) return@map groups

                searchAndSort(
                    query = query.trim().toString(),
                    groups = groups
                )
            }
            .orElse { e ->
                if (e is NetworkException) return@combine GroupsSearchUiState.HasNoInternet
                return@combine GroupsSearchUiState.Error(e)
            }

        GroupsSearchUiState.Success(
            results = groups.toImmutableList()
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GroupsSearchUiState.Loading
        )

    fun addGroup(group: Group) {
        if (mutex.isLocked) return

        viewModelScope.launch {
            mutex.withLock {
                groupsRepository.addGroup(group)
                navigateBackEvent.send(Unit)
                // Задержка для того чтобы нельзя было спамить кликами
                delay(1000)
            }
        }
    }
}

@Immutable
sealed interface GroupsSearchUiState {
    data object NotPublishedYet : GroupsSearchUiState
    data class Error(val e: Exception) : GroupsSearchUiState
    data object HasNoInternet : GroupsSearchUiState
    data class Success(val results: ImmutableList<Group>) : GroupsSearchUiState
    data object Loading : GroupsSearchUiState
}
