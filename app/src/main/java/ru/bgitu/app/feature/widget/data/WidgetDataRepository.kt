package ru.bgitu.app.feature.widget.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import ru.bgitu.app.core.data.schedule.asExternalModel
import ru.bgitu.app.core.data.util.AcademicWeekProvider
import ru.bgitu.app.core.datastore.model.CachedSchedule
import ru.bgitu.app.core.datastore.model.CachedWidgetPrefs
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.model.Schedule
import ru.bgitu.app.core.model.WidgetPrefs
import ru.bgitu.app.core.model.WidgetScheduleState
import ru.bgitu.app.core.util.now
import kotlin.apply

class WidgetDataRepository(
    private val dataStore: DataStore<UserPrefs>,
    private val academicWeekProvider: AcademicWeekProvider
) {

    fun getPrefsFlow(appWidgetId: Int): Flow<WidgetPrefs> {
        return dataStore.data
            .map { it.widgetsPrefs[appWidgetId]?.asExternalModel() ?: WidgetPrefs() }
            .distinctUntilChanged()
    }

    suspend fun getPrefs(appWidgetId: Int): WidgetPrefs {
        return getPrefsFlow(appWidgetId).first()
    }

    suspend fun updatePrefs(appWidgetId: Int, block: (WidgetPrefs) -> WidgetPrefs) {
        dataStore.updateData { prefs ->
            prefs.copy(
                widgetsPrefs = prefs.widgetsPrefs.toMutableMap().apply {
                    val widgetPrefs = this[appWidgetId]?.asExternalModel() ?: WidgetPrefs()
                    this[appWidgetId] = block(widgetPrefs).asCachedModel()
                }
            )
        }
    }

    fun getSelectedDateForWidgetFlow(appWidgetId: Int): Flow<LocalDate> {
        return dataStore.data.map { it.widgetsPrefs[appWidgetId]?.selectedDate ?: LocalDate.now() }
    }

    suspend fun selectDateForWidget(appWidgetId: Int, date: LocalDate) {
        dataStore.updateData { prefs ->
            prefs.copy(
                widgetsPrefs = prefs.widgetsPrefs.toMutableMap().apply {
                    val oldWidgetOptions = getOrElse(appWidgetId) { return@apply }
                    put(appWidgetId, oldWidgetOptions.copy(selectedDate = date))
                }
            )
        }
    }

    fun getScheduleForWidgetFlow(appWidgetId: Int): Flow<WidgetScheduleState> {
        data class Data(
            val prefs: CachedWidgetPrefs?,
            val schedule: CachedSchedule?,
            val termStartDate: LocalDate,
            val noGroupsFound: Boolean
        )

        return combine(
            dataStore.data,
            academicWeekProvider.termStartDate
        ) { data, termStartDate ->
            val prefs = data.widgetsPrefs[appWidgetId]
            val schedule = prefs?.group?.let { group -> data.cachedSchedule[group.id] }
            Data(prefs, schedule, termStartDate, data.userGroups.isEmpty())
        }
            .distinctUntilChanged()
            .map { (prefs, schedule, termStartDate, noGroupsFound) ->
                if (prefs?.group == null) return@map WidgetScheduleState.GroupNotSelected(noGroupsFound)
                if (schedule == null) return@map WidgetScheduleState.Error

                WidgetScheduleState.Success(
                    groupName = prefs.group?.name ?: "",
                    lessons = schedule.asExternalModel().getDaySchedule(
                        date = prefs.selectedDate,
                        termStartDate = termStartDate,
                    ),
                    lastRefreshed = schedule.updatedAt
                )
            }
    }

    suspend fun deletePrefs(appWidgetId: Int) {
        dataStore.updateData {
            it.copy(
                widgetsPrefs = it.widgetsPrefs.toMutableMap().apply {
                    remove(appWidgetId)
                }
            )
        }
    }
}

private fun CachedWidgetPrefs.asExternalModel() = WidgetPrefs(
    group = group,
    selectedDate = selectedDate,
    alpha = alpha,
    uiTheme = uiTheme
)

private fun WidgetPrefs.asCachedModel() = CachedWidgetPrefs(
    alpha = alpha,
    selectedDate = selectedDate,
    groupId = group?.id ?: -1,
    groupName = group?.name ?: "",
    uiTheme = uiTheme
)