package ru.bgitu.app.core.datastore

import androidx.datastore.core.DataStore
import ru.bgitu.app.core.datastore.model.CachedInAppNotification
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.model.UiTheme
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class SettingsRepository(
    private val datastore: DataStore<UserPrefs>
) {
    val data = datastore.data

    suspend fun setUiTheme(uiTheme: UiTheme) {
        datastore.updateData {
            it.copy(uiTheme = uiTheme)
        }
    }

    suspend fun markAllAsRead() {
        datastore.updateData { data ->
            data.copy(
                cachedNews = data.cachedNews.map { it.copy(read = true) }
            )
        }
    }

    suspend fun markAsRead(notificationId: Int) {
        datastore.updateData { data ->
            data.copy(
                cachedNews = data.cachedNews.toMutableList().apply {
                    val notificationIndex = indexOfFirst { it.id == notificationId }
                    if (notificationIndex == -1) return@updateData data
                    this[notificationIndex] = this[notificationIndex].copy(read = true)
                }
            )
        }
    }

    suspend fun cacheNotifications(notifications: List<CachedInAppNotification>) {
        datastore.updateData { data ->
            val filteredAppend = notifications.filterNot { notification ->
                data.cachedNews.any { it.id == notification.id }
            }
            if (filteredAppend.isEmpty()) return@updateData data

            val updatedNotifications = data.cachedNews + filteredAppend
            val now = Clock.System.now()
            data.copy(
                cachedNews = updatedNotifications.filter {
                    (now - it.postedAt) < 30.days
                }
            )
        }
    }

    suspend fun setLiveUpdateNotificationEnabled(enabled: Boolean) {
        datastore.updateData {
            it.copy(
                liveUpdateNotification = enabled,
                liveUpdateGroupId = it.liveUpdateGroupId ?: it.userGroups.firstOrNull()?.id
            )
        }
    }

    suspend fun setLiveUpdateNotificationTriggerTime(time: Instant?) {
        datastore.updateData {
            it.copy(liveUpdateNotificationTriggerTime = time)
        }
    }

    suspend fun setHighQualityBlur(highQualityBlur: Boolean) {
        datastore.updateData {
            it.copy(highQualityBlur = highQualityBlur)
        }
    }

    suspend fun setBlurEnabled(enabled: Boolean) {
        datastore.updateData {
            it.copy(blueEnabled = enabled)
        }
    }

    suspend fun resetData(lastResetTime: Instant) {
        datastore.updateData {
            it.copy(
                lastResetTimestamp = lastResetTime,
                userGroups = emptyList(),
                cachedSchedule = emptyMap(),
                cachedTeacherSchedule = emptyMap(),
                teacherSearchHistory = emptyList(),
                teacherModeTeacher = null,
                liveUpdateGroupId = null,
                liveUpdateNotificationTriggerTime = null,
                favoriteTeachers = emptyList(),
                widgetsPrefs = it.widgetsPrefs.toMutableMap().apply {
                    forEach { (appWidgetId, widgetPrefs) ->
                        this[appWidgetId] = widgetPrefs.copy(groupId = null, groupName = null)
                    }
                }
            )
        }
    }

    suspend fun hideNotificationsDialog() {
        datastore.updateData {
            it.copy(showNotificationsDialog = false)
        }
    }

    suspend fun setSyncAllGroups(syncAllGroups: Boolean) {
        datastore.updateData {
            it.copy(syncAllGroups = syncAllGroups)
        }
    }

    suspend fun setShouldShowTomorrowSchedule(shouldShowTomorrowSchedule: Boolean) {
        datastore.updateData {
            it.copy(shouldShowTomorrowSchedule = shouldShowTomorrowSchedule)
        }
    }
}