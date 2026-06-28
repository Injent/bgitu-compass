package ru.bgitu.app.core.data.notifications

import androidx.datastore.core.DataStore
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.datastore.model.toExternalModel
import ru.bgitu.app.core.model.News
import ru.bgitu.app.core.model.SubscribeTopic
import kotlin.coroutines.resume

class FirebaseNotificationsRepository(
    private val dataStore: DataStore<UserPrefs>,
    private val settingsRepository: SettingsRepository,
) {

    private val mutex = Mutex()
    private val messaging: FirebaseMessaging get() = Firebase.messaging

    val subscribedTopics = dataStore.data.map { it.subscribedTopics }

    val isNotificationsEnabled = MutableStateFlow(messaging.isNotificationDelegationEnabled)

    val cachedNews: Flow<List<News>> = dataStore.data.mapLatest { data ->
        data.cachedNews.map { it.toExternalModel() }
    }

    suspend fun markAsRead(notificationId: Int) {
        settingsRepository.markAsRead(notificationId)
    }

    suspend fun markAllAsRead() {
        settingsRepository.markAllAsRead()
    }

    fun enableNotifications() {
        messaging.isNotificationDelegationEnabled = true
        isNotificationsEnabled.value = true
    }

    fun disableNotifications() {
        messaging.isNotificationDelegationEnabled = false
        isNotificationsEnabled.value = false
    }

    suspend fun sync(): Boolean {
        if (dataStore.data.first().wasSubscribedToAllTopicsOn.not()) {
            enableNotifications()
            val success = SubscribeTopic.entries.all { subscribeToTopic(it) }

            if (success) {
                dataStore.updateData { prefs ->
                    prefs.copy(wasSubscribedToAllTopicsOn = true)
                }
            }
        }
        return true
    }

    suspend fun subscribeToTopic(topic: SubscribeTopic): Boolean {
        return try {
            mutex.withLock(owner = topic) {
                suspendCancellableCoroutine { continuation ->
                    messaging.subscribeToTopic(topic.name)
                        .addOnSuccessListener { continuation.resume(Result.success(Unit)) }
                        .addOnFailureListener { continuation.resume(Result.failure(it)) }
                }
                    .onSuccess {
                        dataStore.updateData { prefs ->
                            prefs.copy(subscribedTopics = prefs.subscribedTopics + topic)
                        }
                    }
                    .onFailure { it.printStackTrace() }
                true
            }
        } catch (_: Exception) {
            false
        }
    }

    suspend fun unsubscribeFromTopic(topic: SubscribeTopic): Boolean {
        return try {
            mutex.withLock(owner = topic) {
                messaging.unsubscribeFromTopic(topic.name).await()
                dataStore.updateData { prefs ->
                    prefs.copy(subscribedTopics = prefs.subscribedTopics - topic)
                }
                true
            }
        } catch (_: Exception) {
            false
        }
    }
}