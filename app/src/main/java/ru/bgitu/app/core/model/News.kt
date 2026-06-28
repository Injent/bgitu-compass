package ru.bgitu.app.core.model

import kotlin.time.Instant

data class News(
    val id: Int,
    val source: String,
    val title: String,
    val body: String,
    val url: String?,
    val read: Boolean,
    val postedAt: Instant
)
