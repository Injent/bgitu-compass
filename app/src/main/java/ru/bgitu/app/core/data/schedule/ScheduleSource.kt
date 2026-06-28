package ru.bgitu.app.core.data.schedule

import ru.bgitu.app.core.model.Schedule
import kotlin.time.Instant

sealed interface ScheduleSource {

    data class Success(
        val schedule: Schedule,
        val refreshing: Boolean,
        val updatedAt: Instant
    ) : ScheduleSource

    class Failed(val throwable: Throwable = NullPointerException()) : ScheduleSource

    data object Loading : ScheduleSource

    data object Dropped : ScheduleSource
}