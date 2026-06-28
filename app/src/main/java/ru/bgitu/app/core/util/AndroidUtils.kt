package ru.bgitu.app.core.util

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.bgitu.app.R
import ru.bgitu.app.feature.widget.glance.CompassWidget
import ru.bgitu.app.feature.widget.glance.CompassWidgetReceiver
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.java

fun File.getUri(context: Context): Uri? {
    return try {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            this
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun requestWidgetPin(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val myProvider = ComponentName(context, CompassWidgetReceiver::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        val successCallback = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, CompassWidgetReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
    } else {
        Toast.makeText(context, R.string.feature_widget_launcherNotSupportWidgetPinning, Toast.LENGTH_SHORT).show()
    }
}

fun shouldShowWidgetPinRequest(context: Context): Boolean = runCatching {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    if (appWidgetManager.isRequestPinAppWidgetSupported.not()) return false
    val widgetComponentName = ComponentName(context, CompassWidgetReceiver::class.java)
    appWidgetManager.getAppWidgetIds(widgetComponentName).isEmpty()
}.getOrDefault(false)

fun Context.openTab(url: String) {
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setToolbarCornerRadiusDp(16)
        .build()
        .launchUrl(this, url.toUri())
}

fun Context.checkNotificationsEnabled(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }
    return NotificationManagerCompat.from(this).areNotificationsEnabled()
}

fun Context.openNotificationSettings() {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, this@openNotificationSettings.packageName)
    }
    try {
        this.startActivity(intent)
    } catch (_: Exception) {
        val backUpIntent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = "package:${this@openNotificationSettings.packageName}".toUri()
        }
        try {
            this.startActivity(backUpIntent)
        } catch (_: Exception) {}
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}