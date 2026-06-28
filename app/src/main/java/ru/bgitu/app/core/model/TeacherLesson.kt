package ru.bgitu.app.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate

data class TeacherLesson(
    val subjectName: String,
    val building: String,
    val classroom: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val isLecture: Boolean,
    val groups: String,
    val lessonDate: LocalDate?,
) {
    val groupNames: List<String> = groups.split(",").map(String::trim)

    fun isOngoing(now: LocalDateTime, lessonDate: LocalDate): Boolean {
        val start = startAt.atDate(lessonDate)
        val end = endAt.atDate(lessonDate)
        return now in start..end
    }
}