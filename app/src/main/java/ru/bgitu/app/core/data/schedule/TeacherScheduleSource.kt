package ru.bgitu.app.core.data.schedule

import ru.bgitu.app.core.model.TeacherSchedule
import kotlin.time.Instant

sealed interface TeacherScheduleSource {

    data class Success(
        val schedule: TeacherSchedule,
        val refreshing: Boolean,
        val updatedAt: Instant
    ) : TeacherScheduleSource

    class Failed(val throwable: Throwable = NullPointerException()) : TeacherScheduleSource

    data object Loading : TeacherScheduleSource

    data object Dropped : TeacherScheduleSource
}