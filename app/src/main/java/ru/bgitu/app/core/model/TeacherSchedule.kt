package ru.bgitu.app.core.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import ru.bgitu.app.core.util.isEvenAcademicWeek

data class TeacherSchedule(
    val oddWeek: ImmutableMap<DayOfWeek, ImmutableList<TeacherLesson>>,
    val evenWeek: ImmutableMap<DayOfWeek, ImmutableList<TeacherLesson>>,
) {
    fun getDaySchedule(date: LocalDate, termStartDate: LocalDate): ImmutableList<TeacherLesson> {
        return if (date.isEvenAcademicWeek(termStartDate)) {
            evenWeek
        } else {
            oddWeek
        }[date.dayOfWeek] ?: persistentListOf()
    }
}