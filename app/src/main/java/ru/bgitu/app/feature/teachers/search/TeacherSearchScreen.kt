package ru.bgitu.app.feature.teachers.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.app.R
import ru.bgitu.app.core.common.exception.ApiException
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.StatusCard
import ru.bgitu.app.core.designsystem.component.StatusCardData
import ru.bgitu.app.core.designsystem.icon.ErrorIllustration
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.NoInternetIllustration
import ru.bgitu.app.core.designsystem.icon.ScheduleIllustration
import ru.bgitu.app.core.designsystem.icon.TrashIllustration
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.SearchResultTeacher
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.component.LoadingIndicatorBox
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.component.SearchTopAppBar
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.feature.teachers.SHARED_TRANSITION_KEY_SEARCHBAR
import ru.bgitu.app.feature.teachers.component.TeacherSearchResultCard
import ru.bgitu.app.navigation.LocalResultStore
import ru.bgitu.app.ui.LocalSharedTransitionScope

@Composable
fun TeacherSearchScreen(
    onBack: () -> Unit,
    onNavigateToTeacherSchedule: (Teacher) -> Unit,
    resultKey: String? = null
) {
    val viewModel = koinViewModel<TeacherSearchViewModel> {
        parametersOf(resultKey != null)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ime = LocalSoftwareKeyboardController.current
    val resultStore = LocalResultStore.current

    BackHandler(onBack = onBack)

    TeachersSearchScreenContent(
        uiState = uiState,
        searchState = viewModel.searchState,
        onBackWithResult = { teacher ->
            ime?.hide()
            if (teacher != null && resultKey == null) {
                onNavigateToTeacherSchedule(teacher)
            } else {
                if (resultKey != null) {
                    resultStore.setResult(resultKey, teacher?.fullName)
                }
                onBack()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalHazeMaterialsApi::class, ExperimentalLayoutApi::class
)
@Composable
private fun TeachersSearchScreenContent(
    uiState: TeacherSearchUiState,
    searchState: TextFieldState,
    onBackWithResult: (Teacher?) -> Unit
) {
    val hazeState = rememberHazeState()
    val features = LocalFeatures.current
    val isImeVisible = WindowInsets.isImeVisible

    Scaffold(
        contentWindowInsets = WindowInsets(),
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            with(LocalSharedTransitionScope.current) {
                SearchTopAppBar(
                    hazeState = hazeState,
                    searchState = searchState,
                    onBack = { onBackWithResult(null) },
                    searchBarPlaceholder = stringResource(R.string.feature_teachers_teacherLastName),
                    searchBarModifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = SHARED_TRANSITION_KEY_SEARCHBAR),
                            LocalNavAnimatedContentScope.current
                        )
                )
            }
        },
        modifier = Modifier
            .imePadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = LocalExternalPadding.current + innerPadding +
                        WindowInsets.navigationBars.asPaddingValues() +
                        PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (features.blurEnabled) {
                            Modifier.hazeSource(hazeState)
                        } else Modifier
                    )
            ) {
                uiState(
                    uiState = uiState,
                    onNavigateToTeacherSchedule = { onBackWithResult(it) }
                )
            }

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .zIndex(1f)
                    .height(LocalExternalPadding.current.calculateBottomPadding() + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    .then(
                        if (LocalFeatures.current.blurEnabled) {
                            Modifier.hazeEffect(
                                state = hazeState,
                                style = HazeMaterials.regular(AppTheme.colorScheme.background2)
                            ) {
                                blurRadius = 15.dp
                                progressive = HazeProgressive.verticalGradient(
                                    startIntensity = 0f,
                                    endIntensity = 1f,
                                    easing = EaseIn,
                                    preferPerformance = !features.highQualityBlur
                                )
                                blurEnabled = features.highQualityBlur
                                alpha = if (isImeVisible) 0f else 1f
                            }
                        } else Modifier.background(AppTheme.colorScheme.background2)
                    )
            )
        }
    }
}

private fun LazyListScope.uiState(
    uiState: TeacherSearchUiState,
    onNavigateToTeacherSchedule: (Teacher) -> Unit
) {
    when (uiState) {
        is TeacherSearchUiState.Loading -> {
            item(
                key = "loading",
                contentType = "loader"
            ) {
                LoadingIndicatorBox(
                    modifier = Modifier
                        .animateItem()
                )
            }
        }
        is TeacherSearchUiState.Success -> resultsList(
            results = uiState.results,
            onClick = onNavigateToTeacherSchedule
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
                            .animateItem()
                    )
                }
            }
        }
    }
}

private fun LazyListScope.resultsList(
    results: List<SearchResultTeacher>,
    onClick: (Teacher) -> Unit
) {
    itemsIndexed(
        items = results,
        contentType = { _, _ -> "teacher" }
    ) { index, result ->
        TeacherSearchResultCard(
            result = result,
            onClick = { onClick(result.teacher) },
            shape = AppCardDefaults.getVerticalListShape(index = index, size = results.size),
            modifier = Modifier
                .fillMaxWidth()
                .animateItem()
        )
    }
}

private fun TeacherSearchUiState.toStatusData(): StatusCardData? {
    return when (this) {
        is TeacherSearchUiState.Empty -> StatusCardData(
            titleResId = R.string.nothingFound,
            icon = Icons.TrashIllustration,
            key = "empty",
        )
        is TeacherSearchUiState.Error -> StatusCardData(
            titleResId = R.string.errorHappened,
            icon = Icons.ErrorIllustration,
            key = "error",
            description = if (e is ApiException) {
                "Code: ${e.code}, ${e.message}"
            } else e.message
        )
        is TeacherSearchUiState.HasNoInternet -> StatusCardData(
            titleResId = R.string.noInternet,
            icon = Icons.NoInternetIllustration,
            key = "noInternet"
        )
        is TeacherSearchUiState.NotPublishedYet -> StatusCardData(
            titleResId = R.string.scheduleIsNotPublishedYet,
            icon = Icons.ScheduleIllustration,
            key = "notPublished"
        )
        else -> null
    }
}