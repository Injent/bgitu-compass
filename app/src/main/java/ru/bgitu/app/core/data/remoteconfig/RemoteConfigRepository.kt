package ru.bgitu.app.core.data.remoteconfig

import android.util.Log
import androidx.datastore.core.DataStore
import io.ktor.client.call.body
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import ru.bgitu.app.BuildConfig
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.network.service.CompassApi
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class RemoteConfigRepository(
    private val dataStore: DataStore<UserPrefs>,
    private val service: CompassApi,
    private val settingsRepository: SettingsRepository,
) {
    private val format = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val config = dataStore.data
        .map { it.remoteConfig }
        .distinctUntilChanged()
        .map { config ->
            format.decodeFromJsonElement<RemoteConfig>(config)
        }

    suspend fun fetch(): Boolean {
        config.first().lastFetchAt?.let { lastFetch ->
            if (lastFetch.plus(FETCHING_INTERVAL) > Clock.System.now()) {
                Log.d("RemoteConfigRepository", "Cancelling schedule fetching")
                // Отменяем фетчинг, если интервал не пройден
                return true
            }
        }
        return try {
            val responseBody = service.getRemoteConfig().body<JsonObject>()
            val remoteConfig = format.decodeFromJsonElement<RemoteConfig>(responseBody)

            val currentPrefs = dataStore.data.first()
            if (remoteConfig.lastResetTimestamp != null && remoteConfig.lastResetTimestamp != currentPrefs.lastResetTimestamp) {
                settingsRepository.resetData(remoteConfig.lastResetTimestamp)
            }

            val mutableRemoteConfig = responseBody.toMutableMap()
            mutableRemoteConfig["lastFetchAt"] = JsonPrimitive(Clock.System.now().toString())
            dataStore.updateData {
                it.copy(remoteConfig = JsonObject(mutableRemoteConfig))
            }
            true
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            false
        }
    }
}

private val FETCHING_INTERVAL = if (BuildConfig.DEBUG) 30.seconds else 15.minutes