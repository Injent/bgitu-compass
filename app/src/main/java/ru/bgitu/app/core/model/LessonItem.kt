package ru.bgitu.app.core.model

import kotlinx.datetime.LocalTime

sealed interface LessonItem {

    val subjectName: String
        get() = when (this) {
            is Student -> lesson.subjectName
            is Teacher -> lesson.subjectName
        }

    val building: String
        get() = when (this) {
            is Student -> lesson.building
            is Teacher -> lesson.building
        }

    val classroom: String
        get() = when (this) {
            is Student -> lesson.classroom
            is Teacher -> lesson.classroom
        }

    val startAt: LocalTime
        get() = when (this) {
            is Student -> lesson.startAt
            is Teacher -> lesson.startAt
        }

    val endAt: LocalTime
        get() = when (this) {
            is Student -> lesson.endAt
            is Teacher -> lesson.endAt
        }

    val isLecture: Boolean
        get() = when (this) {
            is Student -> lesson.isLecture
            is Teacher -> lesson.isLecture
        }

    @JvmInline
    value class Teacher(val lesson: TeacherLesson) : LessonItem

    @JvmInline
    value class Student(val lesson: Lesson) : LessonItem
}