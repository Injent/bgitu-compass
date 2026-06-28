package ru.bgitu.app.core.data.schedule

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.LocalDate
import kotlinx.io.IOException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.app.core.common.exception.NetworkException
import ru.bgitu.app.core.data.remoteconfig.RemoteConfigRepository
import ru.bgitu.app.core.data.util.AcademicWeekProvider
import ru.bgitu.app.core.data.util.NetworkMonitor
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.model.Lesson
import ru.bgitu.app.core.model.PresenceState
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.network.service.CompassApi
import kotlin.time.Clock

class ScheduleRepository : KoinComponent {
    // Ленивая загрузка для того чтобы меньше нагружать приложение, когда используется только виджет
    private val api by inject<CompassApi>()
    private val dataStore by inject<DataStore<UserPrefs>>()
    private val networkMonitor by inject<NetworkMonitor>()
    private val remoteConfigRepository by inject<RemoteConfigRepository>()
    private val academicWeekProvider by inject<AcademicWeekProvider>()

    /**
     * Отправляет состояние расписания.
     * Автоматически отправляет новое расписание группы при появлении интернета и кеширует его
     */
    fun getScheduleFlow(groupId: Int): Flow<ScheduleSource> {
        val isInternetConnected = networkMonitor.isOnline
            .filter { it }
            .onStart { emit(false) }
            .distinctUntilChanged()

        val dropData = remoteConfigRepository.config
            .map { remoteConfig ->
                if (remoteConfig.lastResetTimestamp == null) return@map false
                val localData = dataStore.data.first()
                if (localData.userGroups.isEmpty()) return@map false
                remoteConfig.lastResetTimestamp != localData.lastResetTimestamp
            }
            .distinctUntilChanged()

        // Заставляет Flow перезапускаться при появлении интернета или если
        // дата последнего сброса несоответствует текущей
        return combine(
            isInternetConnected,
            dropData
        ) { (isConnected, dropData) ->
            isConnected to dropData
        }
            .flatMapLatest { (isConnected, dropData) ->
                dataStore.data
                    .mapNotNull { it.cachedSchedule[groupId] }
                    .distinctUntilChanged()
                    .map { cachedSchedule ->
                        if (dropData) return@map ScheduleSource.Dropped

                        ScheduleSource.Success(
                            schedule = cachedSchedule.asExternalModel(),
                            refreshing = false,
                            updatedAt = cachedSchedule.updatedAt
                        )
                    }
                    .onStart {
                        if (dropData) {
                            emit(ScheduleSource.Dropped)
                            return@onStart
                        }

                        val cachedSchedule = dataStore.data.first().cachedSchedule[groupId]

                        if (cachedSchedule != null) {
                            val last = cachedSchedule.updatedAt
                            val difference = Clock.System.now() - last

                            // Отменяем фетчинг расписания если интервал обновления не достигнут
                            if (difference.inWholeSeconds < SCHEDULE_REFRESH_INTERVAL_SECONDS) {
                                Log.d(TAG, "[groupId=$groupId] ScheduleKey refreshing is cancelled. " +
                                        "Next update available in " +
                                        "${SCHEDULE_REFRESH_INTERVAL_SECONDS - difference.inWholeSeconds} seconds")
                                return@onStart
                            }

                            ScheduleSource.Success(
                                schedule = cachedSchedule.asExternalModel(),
                                refreshing = true,
                                updatedAt = cachedSchedule.updatedAt
                            ).let { emit(it) }
                        } else {
                            if (isConnected) {
                                emit(ScheduleSource.Loading)
                            } else {
                                emit(ScheduleSource.Failed(NetworkException()))
                                return@onStart
                            }
                        }
                        if (!isConnected) return@onStart

                        val updatedSchedule = try {
                            api.getSchedule(groupId = groupId)
                                .asCached(updatedAt = Clock.System.now())
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to download schedule", e)
                            // Если кешированного расписания нет, то отправляем ошибку
                            if (cachedSchedule == null) {
                                emit(ScheduleSource.Failed(throwable = e))
                            }
                            return@onStart
                        }
                        dataStore.updateData { prefs ->
                            prefs.copy(
                                cachedSchedule = prefs.cachedSchedule.toMutableMap().apply {
                                    put(groupId, updatedSchedule)
                                }
                            )
                        }
                    }
            }
    }

    fun getTeacherScheduleFlow(teacher: Teacher): Flow<TeacherScheduleSource> {
        val isInternetConnected = networkMonitor.isOnline
            .filter { it }
            .onStart { emit(false) }
            .distinctUntilChanged()

        val dropData = remoteConfigRepository.config
            .map { remoteConfig ->
                if (remoteConfig.lastResetTimestamp == null) return@map false
                val localData = dataStore.data.first()
                if (localData.userGroups.isEmpty()) return@map false
                remoteConfig.lastResetTimestamp != localData.lastResetTimestamp
            }
            .distinctUntilChanged()

        // Заставляет Flow перезапускаться при появлении интернета или если
        // дата последнего сброса несоответствует текущей
        return combine(
            isInternetConnected,
            dropData
        ) { (isConnected, dropData) ->
            isConnected to dropData
        }
            .flatMapLatest { (isConnected, dropData) ->
                dataStore.data
                    .mapNotNull { it.cachedTeacherSchedule[teacher.fullName] }
                    .distinctUntilChanged()
                    .map { cachedSchedule ->
                        if (dropData) return@map TeacherScheduleSource.Dropped

                        TeacherScheduleSource.Success(
                            schedule = cachedSchedule.asExternalModel(),
                            refreshing = false,
                            updatedAt = cachedSchedule.updatedAt
                        )
                    }
                    .onStart {
                        if (dropData) {
                            emit(TeacherScheduleSource.Dropped)
                            return@onStart
                        }

                        val cachedSchedule = dataStore.data.first().cachedTeacherSchedule[teacher.fullName]

                        if (cachedSchedule != null) {
                            val last = cachedSchedule.updatedAt
                            val difference = Clock.System.now() - last

                            // Отменяем фетчинг расписания если интервал обновления не достигнут
                            if (difference.inWholeSeconds < SCHEDULE_REFRESH_INTERVAL_SECONDS) {
                                Log.d(TAG, "[teacher=${teacher.fullName}] Schedule refreshing is cancelled. " +
                                        "Next update available in " +
                                        "${SCHEDULE_REFRESH_INTERVAL_SECONDS - difference.inWholeSeconds} seconds")
                                return@onStart
                            }

                            TeacherScheduleSource.Success(
                                schedule = cachedSchedule.asExternalModel(),
                                refreshing = true,
                                updatedAt = cachedSchedule.updatedAt
                            ).let { emit(it) }
                        } else {
                            if (isConnected) {
                                emit(TeacherScheduleSource.Loading)
                            } else {
                                emit(TeacherScheduleSource.Failed(IOException("No internet connection")))
                                return@onStart
                            }
                        }
                        if (!isConnected) return@onStart

                        val updatedSchedule = try {
                            api.getTeacherSchedule(teacher = teacher)
                                .asCached(updatedAt = Clock.System.now())
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to download schedule", e)
                            // Если кешированного расписания нет, то отправляем ошибку
                            if (cachedSchedule == null) {
                                emit(TeacherScheduleSource.Failed(throwable = e))
                            }
                            return@onStart
                        }
                        dataStore.updateData { prefs ->
                            prefs.copy(
                                cachedTeacherSchedule = prefs.cachedTeacherSchedule.toMutableMap().apply {
                                    put(teacher.fullName, updatedSchedule)
                                }
                            )
                        }
                    }
            }
    }

    suspend fun getLessonsByDate(groupId: Int, date: LocalDate): List<Lesson>? {
        return dataStore.data.first()
            .cachedSchedule[groupId]
            ?.asExternalModel()
            ?.getDaySchedule(
                date = date,
                termStartDate = academicWeekProvider.termStartDate.first()
            )
    }

    /**
     * Возвращает true если синхронизация прошла успешно.
     * Синхронизирует расписания всех групп
     */
    suspend fun sync(groupId: Int?): Boolean = coroutineScope {
        val data = dataStore.data.first()
        val syncAllGroups = data.syncAllGroups
        val groupIds = if (syncAllGroups) {
            data.userGroups.map { it.id }
        } else {
            listOf(groupId ?: return@coroutineScope true)
        }
        if (groupIds.isEmpty()) return@coroutineScope true

        val results = groupIds.map { id ->
            async {
                try {
                    id to api.getSchedule(groupId = id).asCached(updatedAt = Clock.System.now())
                } catch (_: Exception) {
                    id to null
                }
            }
        }.awaitAll()

        if (results.all { it.second == null }) return@coroutineScope false

        dataStore.updateData { prefs ->
            prefs.copy(
                cachedSchedule = prefs.cachedSchedule.toMutableMap().apply {
                    for ((id, schedule) in results) {
                        this[id] = schedule ?: continue
                    }
                }
            )
        }
        return@coroutineScope true
    }

    companion object {
        private const val TAG = "ScheduleRepository"
    }
}

private const val SCHEDULE_REFRESH_INTERVAL_SECONDS = 10 * 60