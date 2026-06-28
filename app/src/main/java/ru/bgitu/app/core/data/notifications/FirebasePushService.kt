package ru.bgitu.app.core.data.notifications

import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.component.KoinComponent
import ru.bgitu.app.R
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.datastore.model.CachedInAppNotification
import ru.bgitu.app.core.model.SubscribeTopic
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Clock
import kotlin.time.Instant

class FirebasePushService : FirebaseMessagingService(), KoinComponent {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val id = message.data["id"]?.toIntOrNull() ?: Random.nextInt()
        val source = message.data["source"] ?: applicationContext.getString(R.string.app_name)
        val title = message.data["title"] ?: ""
        val body = message.data["body"] ?: ""
        val url = message.data["url"]
        val postedAt = message.data["postedAt"]?.let { Instant.parseOrNull(it) } ?: Clock.System.now()
        val topic = try {
            SubscribeTopic.valueOf(message.data["notificationType"]!!)
        } catch (_: Exception) {
            Log.e("FirebasePushService", "Unknown topic: ${message.data["notificationType"]}")
            return
        }
        val notification = AppNotifications.basicPushNotification(
            topic = topic,
            title = title,
            body = body,
            uri = try {
                url?.toUri()
            } catch (_: Exception) {
                null
            }
        )
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(id, notification)
        }
        CoroutineScope(Dispatchers.IO).launch {
            get<SettingsRepository>().cacheNotifications(
                CachedInAppNotification(
                    id = id,
                    source = source,
                    title = title,
                    body = body,
                    url = url,
                    read = false,
                    postedAt = postedAt
                ).let(::listOf).also { println(it) }
            )
        }
    }

    @Suppress("RedundantOverride")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Токен хранить не нужно. Все Push уведомления глобальные и распределены по топикам
    }
}