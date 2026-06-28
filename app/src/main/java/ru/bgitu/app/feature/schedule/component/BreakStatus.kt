package ru.bgitu.app.feature.schedule.component

import android.content.res.Configuration
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.toInstant
import ru.bgitu.app.R
import ru.bgitu.app.core.model.Lesson
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

sealed interface LessonBreakStatus {
    data class Waiting(val timeLeft: Duration) : LessonBreakStatus
    data class Ongoing(val timeLeft: Duration) : LessonBreakStatus
    data class Finished(val timeSince: Duration) : LessonBreakStatus
}

sealed interface LessonStartStatus {
    data class Future(val timeLeft: Duration) : LessonStartStatus
    data class Past(val timeSince: Duration) : LessonStartStatus
    data class Finished(val timeSince: Duration) : LessonStartStatus
}

fun calculateLessonStatus(
    lessonStart: LocalDateTime,
    lessonEnd: LocalDateTime,
    now: LocalDateTime,
    timeZone: TimeZone
): LessonStartStatus {
    val nowInstant = now.toInstant(timeZone)
    val startInstant = lessonStart.toInstant(timeZone)
    val endInstant = lessonEnd.toInstant(timeZone)

    return when {
        nowInstant < startInstant -> LessonStartStatus.Future(startInstant - nowInstant)
        nowInstant in startInstant..endInstant -> LessonStartStatus.Past(nowInstant - startInstant)
        else -> LessonStartStatus.Finished(timeSince = nowInstant - endInstant)
    }
}

fun calculateBreakStatus(
    lessonStart: LocalDateTime,
    now: LocalDateTime,
    timeZone: TimeZone
): LessonBreakStatus {
    val nowInstant = now.toInstant(timeZone)
    val startInstant = lessonStart.toInstant(timeZone)

    val breakStartInstant = startInstant.plus(45.minutes)
    val breakEndInstant = breakStartInstant.plus(5.minutes)

    val breakStatus = when {
        nowInstant < breakStartInstant -> {
            LessonBreakStatus.Waiting(breakStartInstant - nowInstant)
        }
        nowInstant < breakEndInstant -> {
            LessonBreakStatus.Ongoing(breakEndInstant - nowInstant)
        }
        else -> {
            LessonBreakStatus.Finished(nowInstant - breakEndInstant)
        }
    }
    return breakStatus
}

fun getScheduleStatus(
    lessons: List<Lesson>,
    now: LocalDateTime,
    scheduleDate: LocalDate,
    timeZone: TimeZone
): Pair<Int, Duration>? {
    if (lessons.isEmpty()) return null

    val nowInstant = now.toInstant(timeZone)
    val firstLesson = lessons.first()
    val firstLessonStart = firstLesson.startAt.atDate(scheduleDate).toInstant(timeZone)

    if (nowInstant < firstLessonStart) {
        return R.string.feature_schedule_status_before_start to (firstLessonStart - nowInstant)
    }

    lessons.forEachIndexed { index, lesson ->
        val lessonStart = lesson.startAt.atDate(scheduleDate).toInstant(timeZone)
        val lessonEnd = lesson.endAt.atDate(scheduleDate).toInstant(timeZone)
        val isLast = index == lessons.lastIndex

        if (nowInstant in lessonStart..lessonEnd) {
            val breakStart = lessonStart.plus(45.minutes)
            val breakEnd = breakStart.plus(5.minutes)

            return when {
                nowInstant < breakStart -> {
                    R.string.feature_schedule_status_break_in to (breakStart - nowInstant)
                }
                nowInstant < breakEnd -> {
                    R.string.feature_schedule_status_break_end_in to (breakEnd - nowInstant)
                }
                nowInstant > breakEnd -> {
                    R.string.feature_schedule_status_lesson_end_in to (lessonEnd - nowInstant)
                }
                else -> null
            }
        }

        // Check gap between lessons
        if (!isLast) {
            val nextLesson = lessons[index + 1]
            val nextLessonStart = nextLesson.startAt.atDate(scheduleDate).toInstant(timeZone)
            if (nowInstant in lessonEnd..nextLessonStart) {
                return R.string.feature_schedule_status_next_lesson_in to (nextLessonStart - nowInstant)
            }
        }
    }

    return null
}

fun formatDuration(duration: Duration, configuration: Configuration): String {
    val totalSeconds = duration.inWholeSeconds
    if (totalSeconds == 0L) return "0"

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    val currentLocale = configuration.locales[0] ?: Locale.getDefault()
    val fmt = MeasureFormat.getInstance(currentLocale, MeasureFormat.FormatWidth.NARROW)

    val measures = buildList {
        if (hours >= 24) {
            add(Measure(hours / 24, MeasureUnit.DAY))
            return@buildList
        }
        if (hours > 0) add(Measure(hours, MeasureUnit.HOUR))
        if (minutes > 0) add(Measure(minutes, MeasureUnit.MINUTE))
        if (seconds > 0 || isEmpty()) add(Measure(seconds, MeasureUnit.SECOND))
    }

    return fmt.formatMeasures(*measures.toTypedArray())
}

fun formatStatusDuration(duration: Duration, configuration: Configuration): String {
    val totalSeconds = duration.inWholeSeconds.coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    val currentLocale = configuration.locales[0] ?: Locale.getDefault()
    val fmt = MeasureFormat.getInstance(currentLocale, MeasureFormat.FormatWidth.NARROW)

    val measures = buildList {
        if (hours > 0) add(Measure(hours, MeasureUnit.HOUR))
        if (minutes > 0) {
            add(Measure(minutes, MeasureUnit.MINUTE))
        } else {
            add(Measure(seconds, MeasureUnit.SECOND))
        }
    }

    return fmt.formatMeasures(*measures.toTypedArray())
}