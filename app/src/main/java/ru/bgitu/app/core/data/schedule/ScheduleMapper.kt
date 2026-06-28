package ru.bgitu.app.core.data.schedule

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import ru.bgitu.app.core.datastore.model.CachedLesson
import ru.bgitu.app.core.model.Lesson
import ru.bgitu.app.core.model.Schedule
import ru.bgitu.app.core.network.model.NetworkLesson
import ru.bgitu.app.core.datastore.model.CachedSchedule
import ru.bgitu.app.core.network.model.NetworkSchedule
import kotlin.time.Instant

fun CachedSchedule.asExternalModel() = Schedule(
    oddWeek = oddWeek.mapValues { (_, list) ->
        list.map(CachedLesson::asExternalModel).toImmutableList()
    }.toImmutableMap(),
    evenWeek = evenWeek.mapValues { (_, list) ->
        list.map(CachedLesson::asExternalModel).toImmutableList()
    }.toImmutableMap()
)

fun CachedLesson.asExternalModel() = Lesson(
    subjectName = subjectName,
    building = building,
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    teacher = teacher,
    teacherFullName = teacherFullName,
    isLecture = isLecture
)

fun NetworkSchedule.asCached(updatedAt: Instant) = CachedSchedule(
    oddWeek = oddWeek.mapValues { (_, list) ->
        list.map(NetworkLesson::asCached).toImmutableList()
    }.toImmutableMap(),
    evenWeek = evenWeek.mapValues { (_, list) ->
        list.map(NetworkLesson::asCached).toImmutableList()
    }.toImmutableMap(),
    updatedAt = updatedAt
)

fun NetworkLesson.asCached() = CachedLesson(
    subjectName = subjectName,
    building = building,
    startAt = startAt,
    endAt = endAt,
    classroom = classroom,
    teacher = teacher,
    teacherFullName = teacherFullName,
    isLecture = isLecture
)