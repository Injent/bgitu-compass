package ru.bgitu.app.core.datastore.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.bgitu.app.core.model.News
import kotlin.time.Instant

@Serializable
data class CachedInAppNotification(
    @SerialName("id") val id: Int,
    @SerialName("source") val source: String,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("url") val url: String?,
    @SerialName("read") val read: Boolean,
    @SerialName("postedAt") val postedAt: Instant
)

fun CachedInAppNotification.toExternalModel() = News(
    id = id,
    source = source,
    title = title,
    body = body,
    url = url,
    read = read,
    postedAt = postedAt
)