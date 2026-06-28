package ru.bgitu.app.feature.teachers.schedule

import android.R.attr.alpha
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.HazeDefaults.blurEnabled
import dev.chrisbanes.haze.HazeDefaults.blurRadius
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.StatusCard
import ru.bgitu.app.core.designsystem.component.StatusCardData
import ru.bgitu.app.core.designsystem.icon.Bookmark
import ru.bgitu.app.core.designsystem.icon.ErrorIllustration
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.NoInternetIllustration
import ru.bgitu.app.core.designsystem.icon.TrashIllustration
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.MeasureComposable
import ru.bgitu.app.core.utilui.component.LoadingIndicatorBox
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.feature.teachers.schedule.component.TeacherLessonDate
import ru.bgitu.app.feature.teachers.schedule.component.TeacherLessonDayGroup
import ru.bgitu.app.feature.teachers.schedule.component.TeacherScheduleTopAppBar
import ru.bgitu.app.feature.teachers.schedule.model.LessonsGroupedByMonth

@Composable
fun TeacherScheduleScreen(
    teacher: Teacher,
    onBack: () -> Unit
) {
    val viewModel = koinViewModel<TeacherScheduleViewModel>(key = teacher.fullName) {
        parametersOf(teacher)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(onBack = onBack)

    TeacherScheduleScreenContent(
        uiState = uiState,
        onBack = onBack,
        onFavoriteChange = viewModel::updateFavorite
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalLayoutApi::class)
@Composable
private fun TeacherScheduleScreenContent(
    uiState: TeacherScheduleUiState,
    onBack: () -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
) {
    val hazeState = rememberHazeState()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isImeVisible = WindowInsets.isImeVisible
    val features = LocalFeatures.current

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            TeacherScheduleTopAppBar(
                hazeState = hazeState,
                teacher = uiState.teacher,
                onBack = onBack,
                actions = {
                    val scale = remember { Animatable(1f) }

                    IconButton(
                        onClick = {
                            onFavoriteChange(!uiState.isFavorite)
                            coroutineScope.launch {
                                scale.animateTo(1.1f, spring(Spring.DampingRatioMediumBouncy))
                                scale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy))
                            }
                        },
                        modifier = Modifier
                            .scale(scale.value)
                    ) {
                        val tint by animateColorAsState(
                            if (uiState.isFavorite) AppTheme.colorScheme.foreground1 else AppTheme.colorScheme.foreground3
                        )
                        Icon(
                            imageVector = Icons.Bookmark,
                            contentDescription = null,
                            tint = tint
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        MeasureComposable(
            composable = {
                TeacherLessonDate(date = LocalDate(2000, 1, 30))
            }
        ) { dateWidth ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = innerPadding + LocalExternalPadding.current + PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (features.blurEnabled) {
                                Modifier.hazeSource(hazeState)
                            } else Modifier
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    uiState(
                        uiState = uiState,
                        dateWidth = dateWidth.width + 16.dp,
                        listState = listState
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
}

private fun LazyListScope.uiState(
    uiState: TeacherScheduleUiState,
    dateWidth: Dp,
    listState: LazyListState,
) {
    when (uiState) {
        is TeacherScheduleUiState.Loading -> item(
            key = "loading",
            contentType = "loader"
        ) {
            LoadingIndicatorBox()
        }
        is TeacherScheduleUiState.Success -> lessons(
            lessonsByMonth = uiState.lessons,
            dateWidth = dateWidth,
            listState = listState
        )
        else -> {
            uiState.toStatusCard()?.let { data ->
                item {
                    StatusCard(
                        data = data,
                        modifier = Modifier
                            .animateItem()
                    )
                }
            }
        }
    }
}

private fun LazyListScope.lessons(
    lessonsByMonth: ImmutableList<LessonsGroupedByMonth>,
    dateWidth: Dp,
    listState: LazyListState
) {
    var itemIndex = 0
    lessonsByMonth.forEach { monthGroup ->
        monthGroup.days.forEachIndexed { dayIndex, dayGroup ->
            if (dayIndex > 0) {
                item(
                    key = dayGroup.hashCode(),
                    contentType = "spacer"
                ) {
                    Spacer(Modifier.height(20.dp))
                }
                itemIndex++
            }

            val currentIndex = itemIndex
            item(
                contentType = "day_group"
            ) {
                TeacherLessonDayGroup(
                    dayGroup = dayGroup,
                    dateWidth = dateWidth,
                    showMonth = dayIndex == 0,
                    index = currentIndex,
                    listState = listState
                )
            }
            itemIndex++
        }
    }
}

private fun TeacherScheduleUiState.toStatusCard(): StatusCardData? {
    return when (this) {
        is TeacherScheduleUiState.Error -> StatusCardData(
            titleResId = R.string.errorHappened,
            icon = Icons.ErrorIllustration,
            key = "error"
        )
        is TeacherScheduleUiState.Empty -> StatusCardData(
            titleResId = R.string.nothingFound,
            icon = Icons.TrashIllustration,
            key = "empty"
        )
        is TeacherScheduleUiState.HasNoInternet -> StatusCardData(
            titleResId = R.string.noInternet,
            icon = Icons.NoInternetIllustration,
            key = "internet"
        )
        else -> null
    }
}