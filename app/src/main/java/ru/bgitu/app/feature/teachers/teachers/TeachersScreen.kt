package ru.bgitu.app.feature.teachers.teachers

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.AppSearchBar
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.Search
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.core.utilui.taperedBorder
import ru.bgitu.app.feature.teachers.SHARED_TRANSITION_KEY_SEARCHBAR
import ru.bgitu.app.feature.teachers.component.TeacherSearchResultCard
import ru.bgitu.app.feature.teachers.teachers.component.FavoriteTeacherCard
import ru.bgitu.app.feature.teachers.teachers.component.SwitchToTeacherSuggestionDialog
import ru.bgitu.app.ui.LocalSharedTransitionScope

@Composable
fun TeachersScreen(
    viewModel: TeachersViewModel,
    onNavigateToTeacherSearch: () -> Unit,
    onTeacherClick: (Teacher) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler(onBack = onBack)

    TeachersScreenContent(
        uiState = uiState,
        onNavigateToTeacherSearch = onNavigateToTeacherSearch,
        onTeacherClick = onTeacherClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onDismissSwitchToTeacherDialog = viewModel::dismissSwitchToTeacherDialog,
        onSwitchToTeacher = viewModel::switchToTeacher,
        onDeleteTeacher = viewModel::onDeleteTeacher
    )
}

@Composable
private fun TeachersScreenContent(
    uiState: TeachersUiState,
    onNavigateToTeacherSearch: () -> Unit,
    onDismissSwitchToTeacherDialog: () -> Unit,
    onTeacherClick: (Teacher) -> Unit,
    onFavoriteClick: (Teacher) -> Unit,
    onSwitchToTeacher: () -> Unit,
    onDeleteTeacher: (Teacher) -> Unit
) {
    if (uiState is TeachersUiState.Success && uiState.showTeacherSuggestion) {
        SwitchToTeacherSuggestionDialog(
            onDismissRequest = onDismissSwitchToTeacherDialog,
            onConfirm = onSwitchToTeacher
        )
    }

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(LocalExternalPadding.current)
                    .navigationBarsPadding()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
            ) {
                with(LocalSharedTransitionScope.current) {
                    TeacherSearchField(
                        onClick = onNavigateToTeacherSearch,
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = SHARED_TRANSITION_KEY_SEARCHBAR),
                                LocalNavAnimatedContentScope.current
                            )
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true,
            contentPadding = innerPadding + PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (uiState) {
                TeachersUiState.Loading -> Unit
                is TeachersUiState.Success -> {
                    itemsIndexed(
                        items = uiState.favoriteTeachers,
                        key = { _, teacher -> "fav_${teacher.fullName}" }
                    ) { index, teacher ->
                        FavoriteTeacherCard(
                            teacher = teacher,
                            onClick = { onTeacherClick(teacher) },
                            onFavoriteClick = { onFavoriteClick(teacher) },
                            modifier = Modifier
                                .animateItem()
                                .then(
                                    if (index > 0) {
                                        Modifier
                                            .padding(top = 4.dp)
                                    } else {
                                        Modifier
                                    }
                                )
                        )
                    }
                    item(
                        span = { GridItemSpan(2) }
                    ) {
                        Column {
                            GroupStyledText(
                                text = stringResource(R.string.feature_teachers_bookmarks)
                            )
                            if (uiState.favoriteTeachers.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.feature_teachers_empty),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AppTheme.colorScheme.foreground3,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            AppTheme.colorScheme.background1,
                                            AppCardDefaults.SingleCard
                                        )
                                        .padding(32.dp)
                                        .animateItem()
                                )
                            }
                        }
                    }
                    itemsIndexed(
                        items = uiState.history,
                        key = { _, result -> "hist_${result.teacher.fullName}" },
                        span = { _, _ -> GridItemSpan(2) }
                    ) { index, result ->
                        TeacherSearchResultCard(
                            result = result,
                            shape = AppCardDefaults.getVerticalListShape(
                                index = index,
                                size = uiState.history.size,
                                inverted = true
                            ),
                            onClick = { onTeacherClick(result.teacher) },
                            onDelete = { onDeleteTeacher(result.teacher) },
                            modifier = Modifier
                                .animateItem()
                                .then(
                                    if (index < uiState.history.size) {
                                        Modifier
                                            .padding(top = 2.dp)
                                    } else {
                                        Modifier
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TeacherSearchField(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppSearchBar(
        state = rememberTextFieldState(),
        readOnly = true,
        onClick = onClick,
        placeholder = stringResource(R.string.feature_teachers_teacherLastName),
        leadingIcon = {
            IconButton(
                onClick = {},
                enabled = false
            ) {
                Icon(
                    imageVector = Icons.Search,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.foreground1
                )
            }
        },
        modifier = modifier
            .taperedBorder(
                side = TaperedBorderSide.Top,
                strokeWidth = 1.dp,
                color = AppTheme.colorScheme.foreground1.copy(.15f),
                shape = CircleShape
            )
            .taperedBorder(
                side = TaperedBorderSide.Bottom,
                strokeWidth = 1.dp,
                color = AppTheme.colorScheme.foreground1.copy(.25f),
                shape = CircleShape
            )
    )
}