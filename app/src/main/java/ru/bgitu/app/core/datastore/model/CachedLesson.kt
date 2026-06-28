package ru.bgitu.app.core.datastore.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CachedLesson(
    @SerialName("subjectName")
    val subjectName: String,
    @SerialName("building")
    val building: String,
    @SerialName("startAt")
    val startAt: LocalTime,
    @SerialName("endAt")
    val endAt: LocalTime,
    @SerialName("classroom")
    val classroom: String,
    @SerialName("teacher")
    val teacher: String,
    @SerialName("teacherFullName")
    val teacherFullName: String? = null,
    @SerialName("isLecture")
    val isLecture: Boolean
)
