package ru.bgitu.app.core.datastore.model

import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import ru.bgitu.app.core.model.SubscribeTopic
import ru.bgitu.app.core.model.UiTheme
import java.io.InputStream
import java.io.OutputStream
import kotlin.time.Instant

@Serializable
data class UserPrefs(
    val uiTheme: UiTheme = UiTheme.AUTO,
    val highQualityBlur: Boolean = true,
    val blueEnabled: Boolean = true,
    val userIsTeacher: Boolean = false,
    val showTeacherSwitchSuggestion: Boolean = true,
    val userGroups: List<CachedGroup> = emptyList(),
    val cachedSchedule: Map<Int, CachedSchedule> = emptyMap(),
    val cachedTeacherSchedule: Map<String, CachedTeacherSchedule> = emptyMap(),
    val lastResetTimestamp: Instant? = null,
    val remoteConfig: JsonObject = buildJsonObject { },
    val teacherSearchHistory: List<String> = emptyList(),
    val favoriteTeachers: List<String> = emptyList(),
    val widgetsPrefs: Map<Int, CachedWidgetPrefs> = emptyMap(),
    val liveUpdateNotification: Boolean = false,
    val liveUpdateGroupId: Int? = null,
    val subscribedTopics: Set<SubscribeTopic> = emptySet(),
    val wasSubscribedToAllTopicsOn: Boolean = false,
    val teacherModeTeacher: String? = null,
    val syncAllGroups: Boolean = true,
    val liveUpdateNotificationTriggerTime: Instant? = null,
    val showNotificationsDialog: Boolean = true,
    val shouldShowTomorrowSchedule: Boolean = true,
    val cachedNews: List<CachedInAppNotification> = emptyList()
)

object UserPrefsSerializer : Serializer<UserPrefs> {

    private val format = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    override val defaultValue: UserPrefs = UserPrefs()

    override suspend fun readFrom(input: InputStream): UserPrefs {
        return format.decodeFromString(input.readBytes().decodeToString())
    }

    override suspend fun writeTo(
        t: UserPrefs,
        output: OutputStream
    ) {
        output.write(format.encodeToString(t).encodeToByteArray())
    }
}