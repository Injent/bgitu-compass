package ru.bgitu.app.ui

import android.os.Build.VERSION.SDK_INT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.app.core.data.appupdater.AppUpdateManager
import ru.bgitu.app.core.data.appupdater.AppUpdateState
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.utilui.Features
import ru.bgitu.app.feature.update.UpdateNavKey
import ru.bgitu.app.navigation.NavigationState
import ru.bgitu.app.navigation.Navigator
import ru.bgitu.app.navigation.TOP_LEVEL_ROUTES
import ru.bgitu.app.navigation.rememberNavigationState

@Composable
fun rememberAppState(
    startRoute: NavKey,
    settingsRepository: SettingsRepository,
    appUpdateManager: AppUpdateManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState {
    val navigationState = rememberNavigationState(
        startRoute = startRoute,
        topLevelRoutes = TOP_LEVEL_ROUTES.map { it.key }
    )

    return remember(
        navigationState,
        settingsRepository,
        coroutineScope,
        appUpdateManager
    ) {
        AppState(
            navigationState = navigationState,
            settingsRepository = settingsRepository,
            coroutineScope = coroutineScope,
            appUpdateManager = appUpdateManager
        )
    }
}

@Stable
class AppState(
    val navigationState: NavigationState,
    appUpdateManager: AppUpdateManager,
    settingsRepository: SettingsRepository,
    coroutineScope: CoroutineScope
) {
    val features = settingsRepository.data
        .map { prefs ->
            Features(
                highQualityBlur = prefs.highQualityBlur,
                blurEnabled = prefs.blueEnabled && SDK_INT >= 31,
                userIsTeacher = prefs.userIsTeacher,
                hasUnreadNotifications = prefs.cachedNews.any { !it.read }
            )
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Features()
        )

    init {
        coroutineScope.launch {
            appUpdateManager.fetchAppUpdateInfo()?.let { updateInfo ->
                val navigator = Navigator(navigationState)
                fun navigate() {
                    if (navigator.currentBackStack.last() !is UpdateNavKey) {
                        navigator.replaceAll(UpdateNavKey)
                    }
                }

                if (updateInfo.isStandalone) {
                    appUpdateManager.appUpdateState.collectLatest { state ->
                        if (state is AppUpdateState.ReadyToInstall) navigate()
                    }
                } else {
                    appUpdateManager.startUpdateFlow()
                }
            }
        }
    }
}