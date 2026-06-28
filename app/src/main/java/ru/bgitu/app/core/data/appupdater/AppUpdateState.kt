package ru.bgitu.app.core.data.appupdater

sealed interface AppUpdateState {
    data object Unknown : AppUpdateState

    data class UpdateAvailable(val versionCode: Long, val appUpdateInfo: AppUpdateInfo) :
        AppUpdateState

    data class Downloading(val progress: Float) : AppUpdateState

    data object ReadyToInstall : AppUpdateState

    data object Failed : AppUpdateState
}
