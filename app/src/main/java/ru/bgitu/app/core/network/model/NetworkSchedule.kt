package ru.bgitu.app.core.network.model

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSchedule(
    @SerialName("first_week") val oddWeek: Map<DayOfWeek, List<NetworkLesson>>,
    @SerialName("second_week") val evenWeek: Map<DayOfWeek, List<NetworkLesson>>
)
