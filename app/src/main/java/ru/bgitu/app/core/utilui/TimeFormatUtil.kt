package ru.bgitu.app.core.utilui

import android.content.res.Resources
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import ru.bgitu.app.R
import kotlin.time.Clock
import kotlin.time.Instant

fun Instant.formatTimePassed(
    resources: Resources,
    from: Instant,
): String {
    val duration = from - this

    if (duration.isNegative()) return resources.getString(R.string.time_now)

    val seconds = duration.inWholeSeconds
    val minutes = duration.inWholeMinutes
    val hours = duration.inWholeHours
    val days = duration.inWholeDays

    return when {
        seconds < 60 -> {
            resources.getString(R.string.time_now)
        }
        minutes < 60 -> {
            resources.getQuantityString(R.plurals.minutes_ago, minutes.toInt(), minutes)
        }
        hours < 24 -> {
            resources.getQuantityString(R.plurals.hours_ago, hours.toInt(), hours)
        }
        else -> {
            resources.getQuantityString(R.plurals.days_ago, days.toInt(), days)
        }
    }
}

fun Instant.formatAt(
    resources: Resources,
    timeZone: TimeZone
): String {
    val now = Clock.System.now()
    val todayDate = now.toLocalDateTime(timeZone).date
    
    val targetLocalDateTime = this.toLocalDateTime(timeZone)
    val targetDate = targetLocalDateTime.date
    
    val hour = targetLocalDateTime.hour
    val minute = targetLocalDateTime.minute.toString().padStart(2, '0')
    val timeStr = "$hour:$minute"
    
    val daysDifference = todayDate.daysUntil(targetDate)
    
    return when {
        daysDifference == 0 -> {
            resources.getString(R.string.today_at, timeStr)
        }
        daysDifference == 1 -> {
            resources.getString(R.string.tomorrow_at, timeStr)
        }
        daysDifference > 1 -> {
            resources.getQuantityString(R.plurals.in_days_at, daysDifference, daysDifference, timeStr)
        }
        else -> {
            resources.getString(R.string.today_at, timeStr)
        }
    }
}
