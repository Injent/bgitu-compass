package ru.bgitu.app.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate

data class Lesson(
    val subjectName: String,
    val building: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val classroom: String,
    val teacher: String,
    val teacherFullName: String?,
    val isLecture: Boolean
) {
    fun isOngoing(now: LocalDateTime, lessonDate: LocalDate): Boolean {
        val start = startAt.atDate(lessonDate)
        val end = endAt.atDate(lessonDate)
        return now in start..end
    }
}