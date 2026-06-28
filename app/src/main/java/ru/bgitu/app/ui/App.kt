package ru.bgitu.app.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ru.bgitu.app.core.analytics.CrashAnalytics
import ru.bgitu.app.core.designsystem.component.AppNavItem
import ru.bgitu.app.core.designsystem.component.AppNavigationBar
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.minus
import ru.bgitu.app.feature.groups.groupsGraph
import ru.bgitu.app.feature.news.newsScreen
import ru.bgitu.app.feature.schedule.scheduleScreen
import ru.bgitu.app.feature.settings.settingsGraph
import ru.bgitu.app.feature.teachers.teachersGraph
import ru.bgitu.app.feature.update.updateScreen
import ru.bgitu.app.navigation.LocalResultStore
import ru.bgitu.app.navigation.Navigator
import ru.bgitu.app.navigation.TEACHERS_TOP_LEVEL_ROUTES
import ru.bgitu.app.navigation.TOP_LEVEL_ROUTES
import ru.bgitu.app.navigation.rememberResultStore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(
    appState: AppState,
    launchParams: LaunchParams,
) {
    val hazeState = rememberHazeState()
    val features = LocalFeatures.current
    val density = LocalDensity.current
    var externalPadding by remember { mutableStateOf(PaddingValues()) }
    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()
    val navigator = remember { Navigator(appState.navigationState) }
    val resultStore = rememberResultStore()

    val entryProvider = entryProvider {
        scheduleScreen(
            navigator = navigator,
            groupId = launchParams.groupId,
            date = launchParams.date
        )
        newsScreen(navigator = navigator)
        updateScreen(navigator = navigator)
        groupsGraph(navigator = navigator)
        teachersGraph(navigator = navigator)
        settingsGraph(navigator = navigator)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        bottomBar = {
            AnimatedVisibility(
                visible = appState.navigationState.showNavigationBar,
                enter = slideInVertically(
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) { it },
                exit = slideOutVertically { it },
                modifier = Modifier
                    .onSizeChanged { size ->
                        externalPadding = PaddingValues(
                            bottom = with(density) { size.height.toDp() }
                        ) - navigationBarsPadding
                    }
            ) {
                val routes = if (features.userIsTeacher) {
                    TEACHERS_TOP_LEVEL_ROUTES
                } else {
                    TOP_LEVEL_ROUTES
                }

                AppNavigationBar(
                    hazeState = hazeState,
                    selectedTabIndex = routes.indexOfFirst { it.key == appState.navigationState.topLevelRoute }.coerceAtLeast(0),
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    routes.forEach { navBarItem ->
                        val isSelected = navBarItem.key == appState.navigationState.topLevelRoute
                        AppNavItem(
                            selected = isSelected,
                            onClick = { navigator.navigate(navBarItem.key) },
                            icon = navBarItem.provideIcon,
                            label = stringResource(navBarItem.label),
                            modifier = Modifier
                        )
                    }
                }
            }
        },
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .focusable()
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                )
        ) {
            CompositionLocalProvider(
                LocalExternalPadding provides externalPadding,
                LocalFeatures provides features,
                LocalResultStore provides resultStore
            ) {
                SharedTransitionLayout {
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this
                    ) {
                        NavDisplay(
                            entries = appState.navigationState.toDecoratedEntries(entryProvider),
                            transitionSpec = basicTransition(),
                            popTransitionSpec = basicTransition(),
                            sharedTransitionScope = this,
                            onBack = { navigator.goBack() },
                            modifier = Modifier.then(
                                if (features.blurEnabled) {
                                    Modifier.hazeSource(state = hazeState)
                                } else Modifier
                            )
                        )
                        LaunchedEffect(appState.navigationState.currentKey) {
                            CrashAnalytics.setCurrentScreen(appState.navigationState.currentKey.toString())
                        }
                    }
                }
            }
        }
    }
}

typealias EntryProvider = EntryProviderScope<NavKey>

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    error("SharedTransitionScope is not provided")
}

private fun <T : Any> basicTransition(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform {
    return { EnterTransition.None togetherWith ExitTransition.None }
}