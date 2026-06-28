@file:OptIn(ExperimentalMaterial3Api::class)

package ru.bgitu.app.feature.groups

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.toImmutableList
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.component.AppSearchBar
import ru.bgitu.app.core.designsystem.component.AppToolbar
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.designsystem.icon.ArrowBack
import ru.bgitu.app.core.designsystem.icon.Drag
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.taperedBorder
import ru.bgitu.app.feature.groups.component.GroupCard
import ru.bgitu.app.feature.groups.component.GroupDropDownMenu
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.util.Collections

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GroupsScreen(
    viewModel: GroupsViewModel,
    onBack: () -> Unit,
    onNavigateToGroupSearch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onBack()
    }

    if (uiState is GroupsUiState.Success) {
        GroupsScreenContent(
            uiState = uiState as GroupsUiState.Success,
            onBack = onBack,
            onSetGroups = viewModel::onSetGroups,
            onNavigateToGroupSearch = onNavigateToGroupSearch
        )
    } else {
        GroupsScreenContentLoading()
    }
}

@Composable
private fun GroupsScreenContent(
    uiState: GroupsUiState.Success,
    onBack: () -> Unit,
    onSetGroups: (List<Group>) -> Unit,
    onNavigateToGroupSearch: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    var allGroups by remember { mutableStateOf(uiState.groups) }
    LaunchedEffect(uiState.groups) {
        if (uiState.groups.size != allGroups.size) {
            allGroups = uiState.groups
        }
    }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        allGroups = allGroups.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }.toImmutableList()
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
        onSetGroups(allGroups)
    }
    val hazeState = rememberHazeState()
    val features = LocalFeatures.current

    fun modifyGroups(modify: MutableList<Group>.() -> Unit) {
        allGroups = allGroups
            .toMutableList()
            .apply(modify)
            .toImmutableList()
        onSetGroups(allGroups)
    }

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        contentWindowInsets = WindowInsets(),
        topBar = {
            AppToolbar(
                title = {
                    AppSearchBar(
                        state = rememberTextFieldState(),
                        readOnly = true,
                        leadingIcon = {
                            IconButton(
                                onClick = onBack
                            ) {
                                Icon(
                                    imageVector = Icons.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        onClick = onNavigateToGroupSearch,
                        placeholder = stringResource(R.string.feature_groups_addGroup),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .taperedBorder(
                                side = TaperedBorderSide.Bottom,
                                strokeWidth = 1.dp,
                                color = AppTheme.colorScheme.foreground1.copy(.25f),
                                shape = CircleShape
                            )
                            .taperedBorder(
                                side = TaperedBorderSide.Top,
                                strokeWidth = 1.dp,
                                color = AppTheme.colorScheme.foreground1.copy(.15f),
                                shape = CircleShape
                            )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .then(
                    if (features.blurEnabled) {
                        Modifier.hazeSource(hazeState)
                    } else Modifier
                )
        ) {
            GroupStyledText(
                text = stringResource(R.string.feature_groups_myGroups)
            )
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (allGroups.isEmpty()) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (SDK_INT >= 28) {
                                        Modifier.animateItem()
                                    } else Modifier
                                )
                                .padding(top = 16.dp)
                        ) {
                            AppContentButton(
                                onClick = onNavigateToGroupSearch,
                                modifier = Modifier
                                    .taperedBorder(
                                        side = TaperedBorderSide.Top,
                                        strokeWidth = 2.dp,
                                        color = AppTheme.colorScheme.onBrand.copy(.25f),
                                        shape = CircleShape
                                    )
                                    .taperedBorder(
                                        side = TaperedBorderSide.Bottom,
                                        strokeWidth = 2.dp,
                                        color = AppTheme.colorScheme.onBrand.copy(.15f),
                                        shape = CircleShape
                                    )
                            ) {
                                Text(text = stringResource(R.string.feature_groups_addGroup))
                            }
                        }
                    }
                }
                itemsIndexed(
                    items = allGroups,
                    key = { _, group -> group.id }
                ) { index, group ->
                    ReorderableItem(
                        state = reorderableLazyListState,
                        key = group.id,
                    ) { isDragging ->
                        val scale by animateFloatAsState(if (isDragging) 1.05f else 1f)
                        var expanded by rememberSaveable { mutableStateOf(false) }

                        GroupDropDownMenu(
                            groupIndex = index,
                            groupsSize = allGroups.size,
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            onDelete = {
                                modifyGroups { remove(group) }
                            },
                            onMoveUp = {
                                modifyGroups {
                                    if (index in 1..<size) {
                                        Collections.swap(this, index, index - 1)
                                    }
                                }
                            },
                            onMoveDown = {
                                modifyGroups {
                                    if (index >= 0 && index < size - 1) {
                                        Collections.swap(this, index, index + 1)
                                    }
                                }
                            },
                            hazeState = hazeState
                        ) {
                            GroupCard(
                                group = group,
                                selected = expanded,
                                shape = AppCardDefaults.getVerticalListShape(index = index, size = allGroups.size),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Drag,
                                        contentDescription = null,
                                        tint = AppTheme.colorScheme.foreground2,
                                        modifier = Modifier
                                            .draggableHandle(
                                                onDragStarted = {
                                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                                },
                                                onDragStopped = {
                                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                                },
                                            )
                                    )
                                },
                                onClick = { expanded = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .scale(scale)
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            )
                        }
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = !uiState.isOnline,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.feature_groups_cantAddGroup),
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppTheme.colorScheme.foreground2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupsScreenContentLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = AppTheme.colorScheme.brand
        )
    }
}