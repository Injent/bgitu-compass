package ru.bgitu.app.core.data.schedule

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import ru.bgitu.app.core.datastore.model.CachedTeacherLesson
import ru.bgitu.app.core.datastore.model.CachedTeacherSchedule
import ru.bgitu.app.core.model.TeacherLesson
import ru.bgitu.app.core.model.TeacherSchedule
import ru.bgitu.app.core.network.model.NetworkTeacherLesson
import ru.bgitu.app.core.network.model.NetworkTeacherSchedule
import kotlin.time.Instant

fun CachedTeacherSchedule.asExternalModel() = TeacherSchedule(
    oddWeek = oddWeek.mapValues { (_, list) ->
        list.map(CachedTeacherLesson::asExternalModel).toImmutableList()
    }.toImmutableMap(),
    evenWeek = evenWeek.mapValues { (_, list) ->
        list.map(CachedTeacherLesson::asExternalModel).toImmutableList()
    }.toImmutableMap()
)

fun CachedTeacherLesson.asExternalModel() = TeacherLesson(
    subjectName = subjectName,
    building = building,
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    isLecture = isLecture,
    lessonDate = null,
    groups = groups
)

fun NetworkTeacherSchedule.asExternalModel() = TeacherSchedule(
    oddWeek = oddWeek.mapValues { (_, list) ->
        list.map(NetworkTeacherLesson::asExternalModel).toImmutableList()
    }.toImmutableMap(),
    evenWeek = evenWeek.mapValues { (_, list) ->
        list.map(NetworkTeacherLesson::asExternalModel).toImmutableList()
    }.toImmutableMap()
)

fun NetworkTeacherLesson.asExternalModel() = TeacherLesson(
    subjectName = subjectName,
    building = building,
    classroom = classroom,
    startAt = startAt,
    endAt = endAt,
    isLecture = isLecture,
    groups = groups,
    lessonDate = null
)

fun NetworkTeacherSchedule.asCached(updatedAt: Instant) = CachedTeacherSchedule(
    oddWeek = oddWeek.mapValues { (_, list) ->
        list.map(NetworkTeacherLesson::asCached).toImmutableList()
    }.toImmutableMap(),
    evenWeek = evenWeek.mapValues { (_, list) ->
        list.map(NetworkTeacherLesson::asCached).toImmutableList()
    }.toImmutableMap(),
    updatedAt = updatedAt
)

fun NetworkTeacherLesson.asCached() = CachedTeacherLesson(
    subjectName = subjectName,
    building = building,
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    isLecture = isLecture,
    groups = groups
)