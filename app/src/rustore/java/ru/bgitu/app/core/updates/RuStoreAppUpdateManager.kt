package ru.bgitu.app.core.updates

import android.app.Activity
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.bgitu.app.core.data.appupdater.AppUpdateInfo
import ru.bgitu.app.core.data.appupdater.AppUpdateManager
import ru.bgitu.app.core.data.appupdater.AppUpdateState
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.core.exception.RuStoreNotInstalledException
import kotlin.coroutines.resume

class RuStoreAppUpdateManager(
    context: Context
) : AppUpdateManager {

    private val ruStoreUpdateManager by lazy { RuStoreAppUpdateManagerFactory.create(context) }
    private var appUpdateInfo: RuStoreAppUpdateInfo? = null
    private val _appUpdateState = MutableStateFlow<AppUpdateState>(AppUpdateState.Unknown)
    override val appUpdateState: StateFlow<AppUpdateState> = _appUpdateState.asStateFlow()

    override suspend fun fetchAppUpdateInfo(): AppUpdateInfo? {
        return suspendCancellableCoroutine { continuation ->
            ruStoreUpdateManager.getAppUpdateInfo()
                .addOnSuccessListener {
                    val info = RuStoreAppUpdateInfo(it)
                    appUpdateInfo = info
                    if (it.installStatus == InstallStatus.DOWNLOADED) {
                        _appUpdateState.value = AppUpdateState.ReadyToInstall
                    }
                    continuation.resume(info)
                }
                .addOnFailureListener { e ->
                    if (e !is RuStoreNotInstalledException) {
                        Log.e("UpdateManager", "Error", e)
                    }
                    continuation.resume(null)
                }
        }
    }

    override suspend fun startUpdateFlow(): Result<Unit> {
        val listener = InstallStateUpdateListener { state ->
            _appUpdateState.value = when (state.installStatus) {
                InstallStatus.DOWNLOADING -> {
                    val totalBytes = state.totalBytesToDownload
                    val bytesDownloaded = state.bytesDownloaded
                    val progress = if (totalBytes > 0) bytesDownloaded.toFloat() / totalBytes else 0f
                    AppUpdateState.Downloading(progress = progress)
                }
                InstallStatus.PENDING,
                InstallStatus.DOWNLOADED -> AppUpdateState.ReadyToInstall
                InstallStatus.FAILED -> AppUpdateState.Failed
                else -> AppUpdateState.Unknown
            }
        }
        ruStoreUpdateManager.registerListener(listener)

        return suspendCancellableCoroutine { continuation ->
            ruStoreUpdateManager.startUpdateFlow(
                appUpdateInfo?.info ?: run {
                    continuation.resume(Result.failure(NullPointerException()))
                    return@suspendCancellableCoroutine
                },
                getAppUpdateOptions()
            )
                .addOnSuccessListener { resultCode ->
                    if (resultCode == Activity.RESULT_OK) {
                        continuation.resume(Result.success(Unit))
                    } else {
                        continuation.resume(Result.failure(IllegalStateException()))
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("RuStoreAppUpdateManager", "Failed to start update flow", e)
                    continuation.resume(Result.failure(e))
                }
                .addOnCompletionListener { ruStoreUpdateManager.unregisterListener(listener) }
        }
    }

    private fun getAppUpdateOptions(): AppUpdateOptions {
        return AppUpdateOptions.Builder()
            .appUpdateType(AppUpdateType.IMMEDIATE)
            .build()
    }
}
