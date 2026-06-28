package ru.bgitu.app.core.data.liveupdate

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.app.MainActivity
import ru.bgitu.app.R
import ru.bgitu.app.core.data.notifications.CHANNEL_LIVE_UPDATE_ID
import ru.bgitu.app.core.data.schedule.ScheduleRepository
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.model.Lesson
import ru.bgitu.app.core.util.checkNotificationsEnabled
import ru.bgitu.app.core.util.now
import ru.bgitu.app.core.utilui.getLessonLocation
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class LiveUpdateNotificationManager(
    private val context: Context
) : KoinComponent {

    private val settingsRepository by inject<SettingsRepository>()
    private val scheduleRepository by inject<ScheduleRepository>()
    private val notificationManager: NotificationManagerCompat
        get() = NotificationManagerCompat.from(context)
    private val alarmManager: AlarmManager
        get() = context.getSystemService<AlarmManager>()!!

    private fun hide() {
        alarmManager.cancel(alarmPendingIntent())
        notificationManager.cancel(LIVE_UPDATE_NOTIFICATION_ID)
    }

    suspend fun enable(): Boolean {
        context.checkNotificationsEnabled().let { enabled -> if (!enabled) return false }
        canScheduleAlarms().let { canSchedule -> if (!canSchedule) return false }
        hide()
        settingsRepository.setLiveUpdateNotificationEnabled(true)
        context.sendBroadcast(Intent(context, AlarmReceiver::class.java).setAction(ACTION_TRIGGER_UPDATE))
        return true
    }

    suspend fun disable(forever: Boolean = true) {
        alarmManager.cancel(alarmPendingIntent())
        notificationManager.cancel(LIVE_UPDATE_NOTIFICATION_ID)
        if (forever) {
            settingsRepository.setLiveUpdateNotificationEnabled(false)
            settingsRepository.setLiveUpdateNotificationTriggerTime(time = null)
        } else {
            val groupId = getGroupId() ?: return
            val tomorrow = adjustTomorrow(LocalDate.now())
            val nextTriggerAt = computeNextTrigger(groupId, tomorrow, tomorrow.atTime(0, 0))
            if (nextTriggerAt != null) {
                scheduleAlarmAt(nextTriggerAt)
            }
        }
    }

    suspend fun updateAndScheduleNext(groupId: Int) {
        val today = LocalDate.now()
        val now = LocalDateTime.now()
        showForToday(groupId)
        val nextTrigger = computeNextTrigger(groupId, today, now)
        if (nextTrigger != null) {
            scheduleAlarmAt(nextTrigger)
        }
    }

    suspend fun restart() {
        val prefs = settingsRepository.data.first()
        val enabled = prefs.liveUpdateNotification
        val groupId = prefs.liveUpdateGroupId ?: prefs.userGroups.firstOrNull()?.id
        if (!enabled || groupId == null) {
            hide()
            return
        }

        val triggerTime = prefs.liveUpdateNotificationTriggerTime
        val nowInstant = Clock.System.now()

        if (triggerTime != null && triggerTime > nowInstant) {
            scheduleAlarmAt(triggerTime)
            val today = LocalDate.now()
            if (triggerTime.toLocalDateTime(TimeZone.currentSystemDefault()).date == today) {
                showForToday(groupId)
            }
        } else {
            updateAndScheduleNext(groupId)
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun showForToday(groupId: Int) {
        val today = LocalDate.now()
        val now = LocalDateTime.now()
        val lessons = scheduleRepository.getLessonsByDate(groupId = groupId, date = today)
        val (current, next) = getCurrentAndNextLesson(lessons, today, now)

        val isSoon = if (current == null && next != null) {
            val startTime = next.startAt.atDate(today).toInstant(TimeZone.currentSystemDefault())
            val currentTime = now.toInstant(TimeZone.currentSystemDefault())
            (startTime - currentTime) <= 20.minutes
        } else false

        if (current == null && !isSoon) {
            notificationManager.cancel(LIVE_UPDATE_NOTIFICATION_ID)
            return
        }

        ensureLiveUpdateChannel(context)
        val smallContent = RemoteViews(context.packageName, R.layout.liveupdate_notification)
        val bigContent = RemoteViews(context.packageName, R.layout.liveupdate_notification_big)

        val activeLesson = current ?: next!!
        val followingLesson = if (current == null) {
            val index = lessons!!.indexOf(next!!)
            lessons.getOrNull(index + 1)
        } else next

        if (current != null) {
            bigContent.setTextViewText(R.id.now, context.getString(R.string.core_data_now))
        } else {
            bigContent.setTextViewText(R.id.now, context.getString(R.string.core_data_starts_soon))
        }

        bigContent.setTextViewText(R.id.current_subject, activeLesson.subjectName)
        bigContent.setTextViewText(R.id.current_time_start, formatTime(activeLesson.startAt))
        bigContent.setTextViewText(R.id.current_time_end, formatTime(activeLesson.endAt))
        bigContent.setTextViewText(
            R.id.current_location,
            getLessonLocation(context.resources, activeLesson.building, activeLesson.classroom)
        )
        bigContent.setViewVisibility(R.id.current_subject, View.VISIBLE)
        bigContent.setViewVisibility(R.id.current_time_start, View.VISIBLE)
        bigContent.setViewVisibility(R.id.current_time_end, View.VISIBLE)
        bigContent.setViewVisibility(R.id.current_location, View.VISIBLE)

        smallContent.setTextViewText(R.id.title, activeLesson.subjectName)
        smallContent.setTextViewText(
            R.id.supportingText,
            getLessonLocation(context.resources, activeLesson.building, activeLesson.classroom)
        )

        if (followingLesson != null) {
            bigContent.setTextViewText(R.id.next_subject, followingLesson.subjectName)
            bigContent.setTextViewText(R.id.next_time_start, formatTime(followingLesson.startAt))
            bigContent.setTextViewText(R.id.next_time_end, formatTime(followingLesson.endAt))
            bigContent.setTextViewText(
                R.id.next_location,
                getLessonLocation(context.resources, followingLesson.building, followingLesson.classroom)
            )
            bigContent.setViewVisibility(R.id.next_subject, View.VISIBLE)
            bigContent.setViewVisibility(R.id.next_time_start, View.VISIBLE)
            bigContent.setViewVisibility(R.id.next_time_end, View.VISIBLE)
            bigContent.setViewVisibility(R.id.next_location, View.VISIBLE)
        } else {
            bigContent.setTextViewText(R.id.next_subject, context.getString(R.string.core_data_then_end_of_classes))
            bigContent.setViewVisibility(R.id.next_time_start, View.GONE)
            bigContent.setViewVisibility(R.id.next_time_end, View.GONE)
            bigContent.setViewVisibility(R.id.next_location, View.GONE)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_LIVE_UPDATE_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(smallContent)
            .setCustomBigContentView(bigContent)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setOngoing(true)
            .setAutoCancel(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_notification,
                    context.getString(R.string.core_data_turn_off_for_today),
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(context, AlarmReceiver::class.java).setAction(ACTION_DISABLE_FOR_TODAY),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            )
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

        if (!(context.checkNotificationsEnabled())) return
        NotificationManagerCompat.from(context).notify(LIVE_UPDATE_NOTIFICATION_ID, notification)
    }

    private suspend fun scheduleAlarmAt(instant: Instant) {
        settingsRepository.setLiveUpdateNotificationTriggerTime(instant)

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC,
            instant.toEpochMilliseconds(),
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, AlarmReceiver::class.java).setAction(ACTION_TRIGGER_UPDATE),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private fun alarmPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, AlarmReceiver::class.java).setAction(ACTION_TRIGGER_UPDATE),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun canScheduleAlarms() = AlarmManagerCompat.canScheduleExactAlarms(alarmManager)

    private suspend fun getGroupId(): Int? {
        return settingsRepository.data.first().liveUpdateGroupId
    }

    private fun ensureLiveUpdateChannel(context: Context) {
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_LIVE_UPDATE_ID,
            NotificationManager.IMPORTANCE_DEFAULT
        )
            .setName(context.getString(R.string.core_data_channel_liveUpdate))
            .setShowBadge(false)
            .setLightsEnabled(false)
            .setVibrationEnabled(false)
            .setSound(null, null)
            .build()
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun formatTime(time: LocalTime): String {
        return time.format(
            LocalTime.Format {
                hour()
                char(':')
                minute()
            }
        )
    }

    private fun getCurrentAndNextLesson(
        lessons: List<Lesson>?,
        today: LocalDate,
        now: LocalDateTime
    ): Pair<Lesson?, Lesson?> {
        if (lessons.isNullOrEmpty()) return null to null
        val current = lessons.firstOrNull { it.isOngoing(now, today) }
        val next = if (current != null) {
            val index = lessons.indexOf(current)
            lessons.getOrNull(index + 1)
        } else {
            lessons.firstOrNull { it.startAt.atDate(today) > now }
        }
        return current to next
    }

    private suspend fun computeNextTrigger(
        groupId: Int,
        today: LocalDate,
        now: LocalDateTime
    ): Instant? {
        val lessons = scheduleRepository.getLessonsByDate(groupId = groupId, date = today)

        val current = lessons?.firstOrNull { it.isOngoing(now, today) }
        if (current != null) {
            return current.endAt.atDate(today).toInstant(TimeZone.currentSystemDefault())
        }

        val upcoming = lessons?.firstOrNull { it.startAt.atDate(today) > now }
        if (upcoming != null) {
            val startInstant = upcoming.startAt.atDate(today).toInstant(TimeZone.currentSystemDefault())
            val soonTrigger = startInstant - 20.minutes
            val nowInstant = now.toInstant(TimeZone.currentSystemDefault())
            return if (nowInstant < soonTrigger) soonTrigger else startInstant
        }

        val tomorrow = adjustTomorrow(today)
        val nextDayLessons = scheduleRepository.getLessonsByDate(groupId, tomorrow)
        val firstTomorrow = nextDayLessons?.firstOrNull()
        return if (firstTomorrow != null) {
            firstTomorrow.startAt.atDate(tomorrow).toInstant(TimeZone.currentSystemDefault()) - 20.minutes
        } else {
            tomorrow.atTime(8, 0).toInstant(TimeZone.currentSystemDefault())
        }
    }

    private fun adjustTomorrow(today: LocalDate): LocalDate {
        val tomorrow = today.plus(1, DateTimeUnit.DAY)
        return if (tomorrow.dayOfWeek == DayOfWeek.SUNDAY) {
            tomorrow.plus(1, DateTimeUnit.DAY)
        } else {
            tomorrow
        }
    }
}

const val LIVE_UPDATE_NOTIFICATION_ID = 2
const val ACTION_DISABLE_FOR_TODAY = "ru.bgitu.app.core.data.liveupdate.ACTION_DISABLE_FOR_TODAY"
const val ACTION_TRIGGER_UPDATE = "ru.bgitu.app.core.data.liveupdate.ACTION_TRIGGER_UPDATE"
