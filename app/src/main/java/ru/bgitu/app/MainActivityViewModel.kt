package ru.bgitu.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.bgitu.app.core.data.liveupdate.LiveUpdateNotificationManager
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.model.UiTheme
import ru.bgitu.app.feature.schedule.ScheduleKey
import ru.bgitu.app.feature.teachers.TeachersKey
import ru.bgitu.app.sync.WorkManagerSyncManager

class MainActivityViewModel(
    settingsRepository: SettingsRepository,
    workManagerSyncManager: WorkManagerSyncManager,
    liveUpdateNotificationManager: LiveUpdateNotificationManager
) : ViewModel() {
    val uiState = settingsRepository.data.map {
        MainActivityUiState(
            uiTheme = it.uiTheme,
            loading = false,
            startRoute = if (it.userIsTeacher) TeachersKey else ScheduleKey
        )
    }
        .onStart {
            workManagerSyncManager.requestSync()
            liveUpdateNotificationManager.restart()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState()
        )
}

data class MainActivityUiState(
    val loading: Boolean = true,
    val uiTheme: UiTheme = UiTheme.AUTO,
    val startRoute: NavKey = ScheduleKey
) {
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean {
        return if (uiTheme == UiTheme.AUTO) {
            isSystemDarkTheme
        } else {
            uiTheme == UiTheme.DARK
        }
    }
}
