package ru.bgitu.app.core.updates

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class DownloadApkWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val httpClient: HttpClient by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val url = inputData.getString(KEY_URL) ?: return@withContext Result.failure()
        val versionCode = inputData.getInt(KEY_VERSION, 0)
        if (versionCode == 0) return@withContext Result.failure()

        val updatesDir = applicationContext.cacheDir.resolve("app_updates").also { it.mkdirs() }
        val tempFile = updatesDir.resolve("${versionCode}_temp.apk")
        val destFile = updatesDir.resolve("$versionCode.apk")

        try {
            if (tempFile.exists()) tempFile.delete()

            var lastUpdateTime = 0L
            httpClient.prepareGet(url) {
                onDownload { bytesDownloaded, totalBytes ->
                    val total = totalBytes ?: 0L
                    val progress = if (total > 0L) bytesDownloaded.toFloat() / total else 0f

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastUpdateTime >= 100L || progress == 1f) {
                        lastUpdateTime = currentTime
                        setProgressAsync(workDataOf(KEY_PROGRESS to progress))
                    }
                }
            }.execute { response ->
                val channel = response.bodyAsChannel()
                tempFile.outputStream().use { output ->
                    val buffer = ByteArray(8192)
                    while (!channel.isClosedForRead) {
                        val bytesRead = channel.readAvailable(buffer)
                        if (bytesRead > 0) {
                            output.write(buffer, 0, bytesRead)
                        }
                    }
                }
            }

            if (tempFile.exists()) {
                if (destFile.exists()) destFile.delete()
                tempFile.renameTo(destFile)
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("DownloadApkWorker", "Failed to download update APK", e)
            if (tempFile.exists()) tempFile.delete()
            Result.failure()
        }
    }

    companion object {
        const val KEY_URL = "apk_url"
        const val KEY_VERSION = "version_code"
        const val KEY_PROGRESS = "progress"
    }
}
