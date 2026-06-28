package ru.bgitu.app.feature.groups

import android.os.Parcelable
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import ru.bgitu.app.navigation.Navigator
import ru.bgitu.app.ui.EntryProvider

@Parcelize
@Serializable
data object Groups : NavKey, Parcelable

@Parcelize
@Serializable
data object GroupsSearch : NavKey, Parcelable

fun EntryProvider.groupsGraph(navigator: Navigator) {
    entry<Groups>(
        clazzContentKey = { it },
        metadata = NavDisplay.transitionSpec {
            EnterTransition.None togetherWith ExitTransition.None
        }
    ) {
        GroupsScreen(
            viewModel = koinViewModel<GroupsViewModel>(),
            onBack = navigator::goBack,
            onNavigateToGroupSearch = { navigator.navigate(GroupsSearch) }
        )
    }

    entry<GroupsSearch>(
        clazzContentKey = { it },
        metadata = NavDisplay.transitionSpec {
            EnterTransition.None togetherWith ExitTransition.None
        }
    ) {
        GroupsSearchScreen(
            viewModel = koinViewModel<GroupsSearchViewModel>(),
            onBack = navigator::goBack
        )
    }
}
