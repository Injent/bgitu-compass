package ru.bgitu.app.core.updates

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.bgitu.app.core.data.appupdater.InstallException
import java.io.InputStream
import kotlin.coroutines.resume

suspend fun installApk(context: Context, apkUri: Uri): Result<Unit> = suspendCancellableCoroutine { continuation ->
    val packageInstaller = context.packageManager.packageInstaller
    val contentResolver = context.contentResolver

    val action = "${context.packageName}.INSTALL_COMPLETE_${System.currentTimeMillis()}"

    val receiver = object : BroadcastReceiver() {
        @SuppressLint("UnsafeIntentLaunch")
        override fun onReceive(ctx: Context, intent: Intent) {
            if (intent.action != action) return

            val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)
            val message = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE) ?: "Unknown error"

            when (status) {
                PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                    val confirmationIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(Intent.EXTRA_INTENT)
                    }

                    if (confirmationIntent != null) {
                        confirmationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(confirmationIntent)
                    } else {
                        context.unregisterReceiver(this)
                        continuation.resume(
                            Result.failure(
                                InstallException(
                                    status,
                                    "Intent for confirmation is null"
                                )
                            )
                        )
                    }
                }
                PackageInstaller.STATUS_SUCCESS -> {
                    context.unregisterReceiver(this)
                    continuation.resume(Result.success(Unit))
                }
                else -> {
                    context.unregisterReceiver(this)
                    continuation.resume(Result.failure(InstallException(status, message)))
                }
            }
        }
    }

    val filter = IntentFilter(action)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
    } else {
        context.registerReceiver(receiver, filter)
    }

    continuation.invokeOnCancellation {
        try { context.unregisterReceiver(receiver) } catch (e: Exception) {}
    }

    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    val intent = Intent(action).setPackage(context.packageName)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags)

    var session: PackageInstaller.Session? = null
    try {
        val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        val sessionId = packageInstaller.createSession(params)
        session = packageInstaller.openSession(sessionId)

        val inputStream: InputStream = contentResolver.openInputStream(apkUri)
            ?: throw InstallException(-1, "Failed to open InputStream for Uri")

        val outputStream = session.openWrite("package", 0, -1)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        session.commit(pendingIntent.intentSender)
    } catch (e: Exception) {
        session?.close()
        try { context.unregisterReceiver(receiver) } catch (_: Exception) {}
        if (e is CancellationException) throw e

        val exception =
            e as? InstallException ?: InstallException(-1, e.message ?: "IO Error", e)
        continuation.resume(Result.failure(exception))
    }
}