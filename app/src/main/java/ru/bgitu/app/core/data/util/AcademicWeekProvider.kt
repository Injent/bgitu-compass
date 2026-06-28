package ru.bgitu.app.core.data.util

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlinx.datetime.plus
import ru.bgitu.app.core.data.remoteconfig.RemoteConfigRepository
import ru.bgitu.app.core.util.now

class AcademicWeekProvider(configRepository: RemoteConfigRepository) {

    val termStartDate = configRepository.config
        .map { it.termStartDate ?: getStartYearDate(LocalDate.now()) }
        .distinctUntilChanged()

    private fun getStartYearDate(date: LocalDate): LocalDate {
        var academicYearStartYear = date.year
        if (date.month.number < Month.SEPTEMBER.number) {
            academicYearStartYear--
        }

        var academicYearStartDate = LocalDate(academicYearStartYear, Month.SEPTEMBER.number, 1)

        if (academicYearStartDate.dayOfWeek == DayOfWeek.SUNDAY) {
            academicYearStartDate = academicYearStartDate.plus(1, DateTimeUnit.DAY)
        }
        return academicYearStartDate
    }
}