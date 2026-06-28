package ru.bgitu.app.core.data.appupdater

import kotlinx.coroutines.flow.StateFlow

interface AppUpdateManager {

    val appUpdateState: StateFlow<AppUpdateState>

    suspend fun fetchAppUpdateInfo(): AppUpdateInfo?

    suspend fun startUpdateFlow(): Result<Unit>
}