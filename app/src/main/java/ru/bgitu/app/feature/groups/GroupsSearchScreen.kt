package ru.bgitu.app.feature.groups

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.ImmutableList
import ru.bgitu.app.R
import ru.bgitu.app.core.common.exception.ApiException
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.StatusCard
import ru.bgitu.app.core.designsystem.component.StatusCardData
import ru.bgitu.app.core.designsystem.icon.ErrorIllustration
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.NoInternetIllustration
import ru.bgitu.app.core.designsystem.icon.ScheduleIllustration
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.util.CollectEvent
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.component.LoadingIndicatorBox
import ru.bgitu.app.core.utilui.component.SearchTopAppBar
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.feature.groups.component.GroupCard

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GroupsSearchScreen(
    viewModel: GroupsSearchViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEvent(viewModel.navigateBackEvent) {
        onBack()
    }

    BackHandler {
        onBack()
    }

    GroupsSearchScreenContent(
        uiState = uiState,
        searchState = viewModel.searchState,
        onBackWithResult = { group ->
            if (group != null) {
                viewModel.addGroup(group)
            } else {
                onBack()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun GroupsSearchScreenContent(
    uiState: GroupsSearchUiState,
    searchState: TextFieldState,
    onBackWithResult: (Group?) -> Unit
) {
    val hazeState = rememberHazeState()
    val features = LocalFeatures.current

    Scaffold(
        contentWindowInsets = WindowInsets(),
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            SearchTopAppBar(
                hazeState = hazeState,
                searchState = searchState,
                onBack = { onBackWithResult(null) }
            )
        },
        modifier = Modifier
            .imePadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .then(
                    if (features.blurEnabled) {
                        Modifier.hazeSource(hazeState)
                    } else Modifier
                )
        ) {
            LazyColumn(
                contentPadding = LocalExternalPadding.current + innerPadding +
                        WindowInsets.navigationBars.asPaddingValues() +
                        PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                uiState(
                    uiState = uiState,
                    onBackWithResult = onBackWithResult
                )
            }
        }
    }
}

private fun LazyListScope.uiState(
    uiState: GroupsSearchUiState,
    onBackWithResult: (Group) -> Unit
) {
    when (uiState) {
        is GroupsSearchUiState.Loading -> {
            item(
                key = "loading",
                contentType = "loader"
            ) {
                LoadingIndicatorBox(
                    modifier = Modifier
                        .then(
                            if (SDK_INT >= 28) Modifier.animateItem() else Modifier
                        )
                )
            }
        }
        is GroupsSearchUiState.Success -> groupsList(
            groups = uiState.results,
            onClick = onBackWithResult
        )
        else -> {
            uiState.toStatusData()?.let { status ->
                item(
                    key = status.key,
                    contentType = "statusCard"
                ) {
                    StatusCard(
                        data = status,
                        modifier = Modifier
                            .then(
                                if (SDK_INT >= 28) Modifier.animateItem() else Modifier
                            )
                    )
                }
            }
        }
    }
}

private fun LazyListScope.groupsList(
    groups: ImmutableList<Group>,
    onClick: (Group) -> Unit
) {
    itemsIndexed(
        items = groups,
        contentType = { _, _ -> "group" }
    ) { index, group ->
        GroupCard(
            group = group,
            onClick = { onClick(group) },
            shape = AppCardDefaults.getVerticalListShape(index = index, size = groups.size),
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (SDK_INT >= 28) Modifier.animateItem() else Modifier
                )
        )
    }
}

private fun GroupsSearchUiState.toStatusData(): StatusCardData? {
    return when (this) {
        is GroupsSearchUiState.Error -> StatusCardData(
            titleResId = R.string.errorHappened,
            icon = Icons.ErrorIllustration,
            key = "error",
            description = if (e is ApiException) {
                "Code: ${e.code}, ${e.message}"
            } else e.message
        )
        is GroupsSearchUiState.HasNoInternet -> StatusCardData(
            titleResId = R.string.noInternet,
            icon = Icons.NoInternetIllustration,
            key = "noInternet"
        )
        is GroupsSearchUiState.NotPublishedYet -> StatusCardData(
            titleResId = R.string.scheduleIsNotPublishedYet,
            icon = Icons.ScheduleIllustration,
            key = "notPublished"
        )
        else -> null
    }
}