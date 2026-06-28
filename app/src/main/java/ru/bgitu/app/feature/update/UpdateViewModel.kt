package ru.bgitu.app.feature.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.app.core.data.appupdater.AppUpdateManager
import ru.bgitu.app.core.data.appupdater.AppUpdateState
import ru.bgitu.app.core.util.OneTimeEvent
import ru.bgitu.app.core.util.trigger

class UpdateViewModel(
    private val updateManager: AppUpdateManager
) : ViewModel() {
    val navigateToRoot = OneTimeEvent<Unit>()
    val showErrorEvent = OneTimeEvent<Unit>()
    private var updateJob: Job? = null
    private val isDownloadingUpdateInfo = MutableStateFlow(false)

    val installStatus = combine(
        updateManager.appUpdateState,
        isDownloadingUpdateInfo
    ) { state, isDownloading ->
        if (isDownloading) return@combine InstallStatus.Downloading(0f)
        when (state) {
            is AppUpdateState.Downloading -> InstallStatus.Downloading(state.progress)
            is AppUpdateState.Failed -> InstallStatus.Failed
            is AppUpdateState.ReadyToInstall -> InstallStatus.ReadyToInstall
            else -> InstallStatus.Pending
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InstallStatus.Pending
        )

    fun startUpdateFlow() {
        if (updateJob?.isActive == true) return
        updateJob = viewModelScope.launch {
            updateManager.startUpdateFlow()
                .onFailure { e ->
                    e.printStackTrace()
                    showErrorEvent.trigger()
                }
        }
    }
}

sealed interface InstallStatus {
    data object Pending : InstallStatus

    data object ReadyToInstall : InstallStatus

    data class Downloading(val progress: Float) : InstallStatus

    data object Failed : InstallStatus
}
