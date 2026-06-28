package ru.bgitu.app.core.network.service

import eu.lepicekmichal.signalrkore.HubConnectionBuilder
import eu.lepicekmichal.signalrkore.HubConnectionState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.network.model.ListenPresenceRequest
import ru.bgitu.app.core.network.model.NetworkGroup
import ru.bgitu.app.core.network.model.NetworkPresence
import ru.bgitu.app.core.network.model.NetworkSchedule
import ru.bgitu.app.core.network.model.NetworkTeacher
import ru.bgitu.app.core.network.model.NetworkTeacherSchedule

class CompassApi(
    private val client: HttpClient,
) {
    suspend fun searchGroups(query: String): List<NetworkGroup> {
        return client.get("groups") {
            parameter("searchQuery", query)
        }.body()
    }

    suspend fun getSchedule(groupId: Int): NetworkSchedule {
        return client.get("v3/lessons") {
            parameter("groupId", groupId)
        }.body()
    }

    suspend fun getRemoteConfig(): HttpResponse {
        return client.get("remoteConfig")
    }

    suspend fun searchTeachers(query: String): List<NetworkTeacher> {
        return client.get("teacherSchedule") {
            parameter("teacherSearch", true)
            parameter("searchQuery", query.ifEmpty { " " })
        }.body()
    }

    suspend fun getTeacherSchedule(teacher: Teacher): NetworkTeacherSchedule {
        return client.get("teacherSchedule") {
            parameter("teacher", teacher.fullName)
        }.body()
    }

    fun listenLessonPresence(request: ListenPresenceRequest): Flow<Pair<HubConnectionState, NetworkPresence?>> {
        val connection = HubConnectionBuilder.create("https://maxim.pamagiti.site/api/hubs/grade")

        return combine(
            connection.connectionState,
            connection.on("GetPresenceCount", NetworkPresence.serializer())
        ) { state, message ->
            val (presence) = message
            state to presence
        }
            .onStart {
                connection.start()
                connection.send("GetPresenceCount", request)
            }
            .onCompletion { connection.stop() }
    }
}