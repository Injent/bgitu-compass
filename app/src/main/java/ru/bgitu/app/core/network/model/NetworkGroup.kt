package ru.bgitu.app.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkGroup(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)