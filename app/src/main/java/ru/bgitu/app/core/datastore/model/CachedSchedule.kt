package ru.bgitu.app.core.datastore.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.bgitu.app.core.network.model.NetworkLesson
import kotlin.time.Instant

@Serializable
data class CachedSchedule(
    @SerialName("first_week") val oddWeek: Map<DayOfWeek, List<CachedLesson>>,
    @SerialName("second_week") val evenWeek: Map<DayOfWeek, List<CachedLesson>>,
    @SerialName("updated_at") val updatedAt: Instant
)