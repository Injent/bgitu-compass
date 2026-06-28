package ru.bgitu.app.core.datastore.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CachedTeacherLesson(
    @SerialName("subjectName")
    val subjectName: String,
    @SerialName("building")
    val building: String,
    @SerialName("classroom")
    val classroom: String,
    @SerialName("startAt")
    val startAt: LocalTime,
    @SerialName("endAt")
    val endAt: LocalTime,
    @SerialName("isLecture")
    val isLecture: Boolean,
    @SerialName("groupName")
    val groups: String,
)
