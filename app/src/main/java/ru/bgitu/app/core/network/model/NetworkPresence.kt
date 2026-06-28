package ru.bgitu.app.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPresence(
    @SerialName("present") val present: Int,
    @SerialName("total") val total: Int
)
