package ru.bgitu.app.core.datastore.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CachedGroup(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)