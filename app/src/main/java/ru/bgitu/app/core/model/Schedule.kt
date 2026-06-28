package ru.bgitu.app.core.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import ru.bgitu.app.core.util.isEvenAcademicWeek

data class Schedule(
    val oddWeek: ImmutableMap<DayOfWeek, ImmutableList<Lesson>>,
    val evenWeek: ImmutableMap<DayOfWeek, ImmutableList<Lesson>>,
) {
    fun getDaySchedule(date: LocalDate, termStartDate: LocalDate): ImmutableList<Lesson> {
        val isEvenAcademicWeek = date.isEvenAcademicWeek(termStartDate)
        return if (isEvenAcademicWeek) {
            evenWeek
        } else {
            oddWeek
        }[date.dayOfWeek] ?: persistentListOf()
    }
}