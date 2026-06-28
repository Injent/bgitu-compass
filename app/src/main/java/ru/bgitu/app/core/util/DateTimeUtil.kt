package ru.bgitu.app.core.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDate.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDate.previousOrSame(dayOfWeek: DayOfWeek): LocalDate {
    val dowValue = dayOfWeek.isoDayNumber

    val calDow = this.dayOfWeek.isoDayNumber
    if (calDow == dowValue) {
        return this
    }
    val daysDiff = dowValue - calDow
    return this.minus(
        if (daysDiff >= 0) 7 - daysDiff else -daysDiff,
        DateTimeUnit.DAY
    )
}

fun LocalDate.getAcademicWeek(termStartDate: LocalDate): Int {
    val daysFromStart = termStartDate.daysUntil(this)
    return (daysFromStart / 7) + 1
}

fun dateFormat(locale: Locale): DateTimeFormat<LocalDateTime> {
    return LocalDateTime.Format {
        day()
        char(' ')
        monthName(MonthNames.getAbbreviatedByLocale(locale))
        chars(". ")
        hour()
        char(':')
        minute()
    }
}

fun LocalDate.isEvenAcademicWeek(termStartDate: LocalDate): Boolean {
    return getAcademicWeek(termStartDate) % 2 == 0
}

fun LocalDate.atStartOfWeek(): LocalDate {
    return this.minus(dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
}

fun LocalDate.inWeekOfOtherDate(other: LocalDate): Boolean {
    val startOfWeek = other.atStartOfWeek()
    val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY)

    return this in startOfWeek..endOfWeek
}

val DayOfWeekNames.Companion.RUSSIAN_ABBREVIATED
    get() = DayOfWeekNames(
        monday = "пн",
        tuesday = "вт",
        wednesday = "ср",
        thursday = "чт",
        friday = "пт",
        saturday = "сб",
        sunday = "вс"
    )

fun DayOfWeekNames.Companion.getAbbreviatedByLocale(locale: Locale): DayOfWeekNames {
    return when (locale.language) {
        "ru" -> RUSSIAN_ABBREVIATED
        "en" -> ENGLISH_ABBREVIATED
        else -> error("Week names for language '${locale.language}' not defined")
    }
}

fun MonthNames.Companion.getAbbreviatedByLocale(locale: Locale): MonthNames {
    return when (locale.language) {
        "ru" -> RUSSIAN_ABBREVIATED
        "en" -> ENGLISH_ABBREVIATED
        else -> error("Month names for language '${locale.language}' not defined")
    }
}

val MonthNames.Companion.RUSSIAN_ABBREVIATED
    get() = MonthNames(
        january = "янв",
        february = "фев",
        march = "мар",
        april = "апр",
        may = "май",
        june = "июн",
        july = "июл",
        august = "авг",
        september = "сен",
        october = "окт",
        november = "ноя",
        december = "дек"
    )

val MonthNames.Companion.RUSSIAN_FULL
    get() = MonthNames(
        january = "январь",
        february = "февраль",
        march = "март",
        april = "апрель",
        may = "май",
        june = "июнь",
        july = "июль",
        august = "август",
        september = "сентябрь",
        october = "октябрь",
        november = "ноябрь",
        december = "декабрь"
    )

fun MonthNames.Companion.getFullByLocale(locale: Locale): MonthNames {
    return when (locale.language) {
        "ru" -> RUSSIAN_FULL
        "en" -> ENGLISH_FULL
        else -> error("Month names for language '${locale.language}' not defined")
    }
}
