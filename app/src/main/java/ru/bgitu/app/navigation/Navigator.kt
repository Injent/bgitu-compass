package ru.bgitu.app.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlin.time.Clock

@Stable
class Navigator(val state: NavigationState) {

    private var lastNavigationActionAt = mutableMapOf<NavKey, Long>()
    private var lastBackActionAt = 0L

    val currentBackStack: NavBackStack<NavKey>
        get() = state.currentSubStack

    fun navigate(route: NavKey, debounce: Long = 0) = withInterval(route, debounce) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun replaceLast(route: NavKey, debounce: Long = 0) = withInterval(route, debounce) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.apply {
                this[lastIndex] = route
            }
        }
    }

    fun replaceAll(vararg route: NavKey, debounce: Long = 0) = withInterval(route.last(), debounce) {
        val firstRoute = route.first()
        if (firstRoute in state.backStacks.keys) {
            state.topLevelRoute = firstRoute
            state.backStacks[state.topLevelRoute]?.apply {
                clear()
                addAll(route)
            }
        } else {
            state.backStacks[state.topLevelRoute]?.addAll(route)
        }
    }

    fun goBack(debounce: Long = 0) = withInterval(null, debounce) {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }

    private fun withInterval(key: NavKey?, interval: Long, action: () -> Unit) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (key == null) {
            if (currentTime - lastBackActionAt >= interval) {
                lastBackActionAt = currentTime
                action()
            }
        } else if (currentTime - lastNavigationActionAt.getOrDefault(key, 0L) >= interval) {
            lastNavigationActionAt[key] = currentTime
            action()
        }
    }
}