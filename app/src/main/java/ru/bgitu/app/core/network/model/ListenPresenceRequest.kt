package ru.bgitu.app.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListenPresenceRequest(
    @SerialName("groupName") val groupName: String,
    @SerialName("disciplineName") val subjectName: String,
    @SerialName("date") val date: LocalDate,
    @SerialName("startTime") val startTime: LocalTime,
)