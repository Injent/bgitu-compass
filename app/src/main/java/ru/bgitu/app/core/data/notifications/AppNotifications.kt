package ru.bgitu.app.core.data.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.app.MainActivity
import ru.bgitu.app.R
import ru.bgitu.app.core.model.SubscribeTopic

const val CHANNEL_GENERAL_ID = "general"
const val CHANNEL_LIVE_UPDATE_ID = "pinned_schedule_channel"

object AppNotifications : KoinComponent {

    fun basicPushNotification(
        topic: SubscribeTopic,
        title: String,
        body: String,
        uri: Uri? = null
    ): Notification {
        val context = get<Context>()
        createGeneralChannel(context)

        return NotificationCompat.Builder(context, CHANNEL_GENERAL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setPriority(
                when (topic) {
                    SubscribeTopic.BROADCAST,
                    SubscribeTopic.SCHEDULE_CHANGE -> NotificationCompat.PRIORITY_DEFAULT
                    SubscribeTopic.SEVERE -> NotificationCompat.PRIORITY_HIGH
                }
            )
            .apply {
                setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(
                            Intent.ACTION_VIEW,
                            "app://notifications".toUri(),
                            context,
                            MainActivity::class.java
                        ),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )

                if (uri != null) {
                    NotificationCompat.Action.Builder(
                        null,
                        context.getString(R.string.core_data_open_link),
                        PendingIntent.getActivity(
                            context,
                            1,
                            Intent(Intent.ACTION_VIEW, uri),
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                        .build()
                        .also(::addAction)
                }
            }
            .setContentTitle(title)
            .setContentText(body)
            .build()
    }

    private fun createGeneralChannel(context: Context) {
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_GENERAL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
            .setName(context.getString(R.string.core_data_channel_general))
            .setShowBadge(true)
            .setLightsEnabled(true)
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}