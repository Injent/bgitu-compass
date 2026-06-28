package ru.bgitu.app.feature.teachers.schedule.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate
import ru.bgitu.app.core.model.TeacherLesson

data class LessonsGroupedByDay(
    val date: LocalDate,
    val lessons: ImmutableList<TeacherLesson>
)
