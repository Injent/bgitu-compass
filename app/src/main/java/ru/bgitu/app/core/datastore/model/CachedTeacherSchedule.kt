package ru.bgitu.app.core.datastore.model

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class CachedTeacherSchedule(
    @SerialName("first_week") val oddWeek: Map<DayOfWeek, List<CachedTeacherLesson>>,
    @SerialName("second_week") val evenWeek: Map<DayOfWeek, List<CachedTeacherLesson>>,
    @SerialName("updated_at") val updatedAt: Instant
)
