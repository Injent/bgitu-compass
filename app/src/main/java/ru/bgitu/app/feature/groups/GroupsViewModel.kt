package ru.bgitu.app.feature.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.app.core.data.liveupdate.LiveUpdateNotificationManager
import ru.bgitu.app.core.data.schedule.GroupsRepository
import ru.bgitu.app.core.data.util.NetworkMonitor
import ru.bgitu.app.core.model.Group

class GroupsViewModel(
    private val groupsRepository: GroupsRepository,
    private val liveUpdateNotificationManager: LiveUpdateNotificationManager,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(
        groupsRepository.groups,
        networkMonitor.isOnline.onStart { emit(true) }
    ) { groups, isOnline ->
        GroupsUiState.Success(
            groups = groups.distinctBy { it.id }.toImmutableList(),
            isOnline = isOnline
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GroupsUiState.Loading
        )

    fun onSetGroups(groups: List<Group>) {
        viewModelScope.launch {
            groupsRepository.setGroups(groups = groups.distinctBy { it.id })
            liveUpdateNotificationManager.restart()
        }
    }
}


sealed interface GroupsUiState {
    data class Success(
        val groups: ImmutableList<Group>,
        val isOnline: Boolean,
    ) : GroupsUiState

    data object Loading : GroupsUiState
}