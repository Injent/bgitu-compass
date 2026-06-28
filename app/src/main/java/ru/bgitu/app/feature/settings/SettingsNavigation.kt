package ru.bgitu.app.feature.settings

import android.os.Parcelable
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.app.navigation.Navigator
import ru.bgitu.app.navigation.SharedViewModelStoreNavEntryDecorator
import ru.bgitu.app.ui.EntryProvider

@Parcelize
@Serializable
data object SettingsKey : NavKey, Parcelable

@Parcelize
@Serializable
data object DesignSettingsKey : NavKey, Parcelable

@Parcelize
@Serializable
data object NotificationsSettingsKey : NavKey, Parcelable

@Parcelize
@Serializable
data object DataSettingsKey : NavKey, Parcelable

@Parcelize
@Serializable
data object AboutAppKey : NavKey, Parcelable

fun EntryProvider.settingsGraph(navigator: Navigator) {
    entry<SettingsKey>(
        clazzContentKey = { it },
    ) {
        SettingsScreen(
            onBack = navigator::goBack,
            onNavigate = navigator::navigate
        )
    }
    entry<DesignSettingsKey>(
        metadata = SharedViewModelStoreNavEntryDecorator.parent(SettingsKey) + Transition,
        clazzContentKey = { it },
    ) {
        DesignScreen(
            viewModel = koinViewModel(),
            onBack = navigator::goBack
        )
    }
    entry<DataSettingsKey>(
        clazzContentKey = { it },
        metadata = SharedViewModelStoreNavEntryDecorator.parent(SettingsKey) + Transition
    ) {
        DataSettingsScreen(
            viewModel = koinViewModel(),
            onBack = navigator::goBack
        )
    }
    entry<NotificationsSettingsKey>(
        clazzContentKey = { it },
        metadata = SharedViewModelStoreNavEntryDecorator.parent(SettingsKey) + Transition
    ) {
        NotificationsScreen(
            viewModel = koinViewModel(),
            onBack = navigator::goBack
        )
    }
    entry<AboutAppKey>(
        clazzContentKey = { it },
        metadata = SharedViewModelStoreNavEntryDecorator.parent(SettingsKey) + Transition
    ) {
        AboutAppScreen(
            onBack = navigator::goBack
        )
    }
}

private val Transition =
    NavDisplay.transitionSpec {
        if (initialState.key is SettingsKey) {
            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
        } else {
            EnterTransition.None togetherWith ExitTransition.None
        }
    } +
    NavDisplay.popTransitionSpec {
        if (targetState.key is SettingsKey) {
            slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
        } else {
            EnterTransition.None togetherWith ExitTransition.None
        }
    }