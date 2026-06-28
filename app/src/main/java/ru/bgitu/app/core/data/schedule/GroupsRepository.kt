package ru.bgitu.app.core.data.schedule

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pro.respawn.apiresult.ApiResult
import ru.bgitu.app.core.data.remoteconfig.RemoteConfigRepository
import ru.bgitu.app.core.datastore.model.CachedGroup
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.network.model.NetworkGroup
import ru.bgitu.app.core.network.service.CompassApi

class GroupsRepository(
    private val compassApi: CompassApi,
    private val datastore: DataStore<UserPrefs>,
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    val groups = datastore.data.map { it.userGroups.map(CachedGroup::asExternalModel) }

    suspend fun search(query: String): ApiResult<List<Group>> = ApiResult {
        compassApi.searchGroups(query.trim().replace("-", "")).map(NetworkGroup::asExternalModel)
    }

    suspend fun setGroups(groups: List<Group>) {
        datastore.updateData { prefs ->
            // Если пользователь еще не выбирал группы, то нет смысла показывать что появилось новое расписание
            val lastResetTimestamp = if (prefs.userGroups.isEmpty()) {
                remoteConfigRepository.config.first().lastResetTimestamp
            } else prefs.lastResetTimestamp

            val userGroups = groups.map(Group::asCached)
            prefs.copy(
                lastResetTimestamp = lastResetTimestamp,
                userGroups = userGroups,
                cachedSchedule = prefs.cachedSchedule
                    .filter { (groupId, _) -> userGroups.any { it.id == groupId } }
            )
        }
    }

    suspend fun addGroup(group: Group) {
        datastore.updateData { prefs ->
            // Если пользователь еще не выбирал группы, то нет смысла показывать что появилось новое расписание
            val lastResetTimestamp = if (prefs.userGroups.isEmpty()) {
                remoteConfigRepository.config.first().lastResetTimestamp
            } else prefs.lastResetTimestamp

            prefs.copy(
                lastResetTimestamp = lastResetTimestamp,
                userGroups = prefs.userGroups + group.asCached()
            )
        }
    }

    suspend fun setLiveUpdateGroup(group: Group) {
        datastore.updateData { prefs ->
            prefs.copy(liveUpdateGroupId = group.id)
        }
    }
}

private fun CachedGroup.asExternalModel() = Group(
    id = id,
    name = name
)

private fun NetworkGroup.asExternalModel() = Group(
    id = id,
    name = name
)

private fun Group.asCached() = CachedGroup(
    id = id,
    name = name
)