package ru.bgitu.app.core.updates

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.ktor.client.call.body
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.bgitu.app.BuildConfig
import ru.bgitu.app.core.common.exception.AppException
import ru.bgitu.app.core.data.appupdater.AppUpdateInfo
import ru.bgitu.app.core.data.appupdater.AppUpdateManager
import ru.bgitu.app.core.data.appupdater.AppUpdateState
import ru.bgitu.app.core.network.service.CompassApi
import ru.bgitu.app.core.util.getUri
import java.io.File
import java.io.FileNotFoundException

internal class StandaloneUpdateManager(
    private val api: CompassApi,
    private val context: Context,
    private val applicationScope: CoroutineScope,
) : AppUpdateManager {

    private val _appUpdateState = MutableStateFlow<AppUpdateState>(AppUpdateState.Unknown)
    override val appUpdateState: StateFlow<AppUpdateState> = _appUpdateState.asStateFlow()
    private var appUpdateInfo: StandaloneAppUpdateInfo? = null
    private val updatesDir = context.cacheDir.resolve("app_updates").also(File::mkdirs)

    init {
        val latestApk = updatesDir.listFiles { file ->
            val v = file.nameWithoutExtension.toIntOrNull() ?: 0
            v > BuildConfig.VERSION_CODE && file.extension == "apk"
        }?.maxByOrNull { file -> file.nameWithoutExtension.toInt() }

        if (latestApk != null) {
            val v = latestApk.nameWithoutExtension.toIntOrNull() ?: 0
            appUpdateInfo = StandaloneAppUpdateInfo(versionCode = v)
            _appUpdateState.value = AppUpdateState.ReadyToInstall
        }
    }

    override suspend fun fetchAppUpdateInfo(): AppUpdateInfo? {
        return try {
            val info = api.getRemoteConfig().body<NetworkAppUpdateInfo>()
            // Delete unused apks
            updatesDir.listFiles { file ->
                val version = file.nameWithoutExtension.toIntOrNull() ?: 0
                version < info.versionCode || version <= BuildConfig.VERSION_CODE
            }?.forEach(File::delete)

            if (info.versionCode > BuildConfig.VERSION_CODE) {
                val apkFile = resolveUpdateFile(info.versionCode)
                val updateInfo = StandaloneAppUpdateInfo(versionCode = info.versionCode)

                if (apkFile.exists()) {
                    _appUpdateState.value = AppUpdateState.ReadyToInstall
                    return updateInfo
                }

                enqueueDownload(info.apkDownloadUrl, info.versionCode)
                updateInfo
            } else {
                null
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if (e !is AppException) Log.e(TAG, "Failed to get app update info", e)
            null
        }?.also { appUpdateInfo = it }
    }

    @OptIn(FlowPreview::class)
    private fun enqueueDownload(url: String, versionCode: Int) {
        val workManager = WorkManager.getInstance(context)
        val uniqueWorkName = "apk_download_$versionCode"

        val workRequest = OneTimeWorkRequestBuilder<DownloadApkWorker>()
            .setInputData(workDataOf(
                DownloadApkWorker.KEY_URL to url,
                DownloadApkWorker.KEY_VERSION to versionCode
            ))
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName,
            ExistingWorkPolicy.KEEP,
            workRequest
        )

        workManager.getWorkInfosForUniqueWorkFlow(uniqueWorkName)
            .onEach { workInfos ->
                val workInfo = workInfos.firstOrNull() ?: return@onEach
                when (workInfo.state) {
                    WorkInfo.State.RUNNING -> {
                        val progress = workInfo.progress.getFloat(DownloadApkWorker.KEY_PROGRESS, 0f)
                        _appUpdateState.value = AppUpdateState.Downloading(progress)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        _appUpdateState.value = AppUpdateState.ReadyToInstall
                    }
                    WorkInfo.State.FAILED -> {
                        _appUpdateState.value = AppUpdateState.Failed
                    }
                    else -> {}
                }
            }
            .launchIn(applicationScope)
    }

    override suspend fun startUpdateFlow(): Result<Unit> = runCatching {
        val info = requireNotNull(appUpdateInfo) { "Update not available" }
        val updateFile = resolveUpdateFile(info.versionCode)
        return installApk(
            context = context,
            apkUri = updateFile.getUri(context) ?: return Result.failure(FileNotFoundException())
        )
    }
        .onFailure {
            updatesDir.listFiles()?.forEach(File::delete)
            _appUpdateState.value = AppUpdateState.Failed
        }

    private fun resolveUpdateFile(versionCode: Int): File =
        updatesDir.resolve("$versionCode.apk")

    companion object {
        const val TAG = "StandaloneUpdateManager"
    }
}

@Serializable
private data class NetworkAppUpdateInfo(
    @SerialName("versionCode")
    val versionCode: Int,
    @SerialName("downloadUrl")
    val apkDownloadUrl: String
)
