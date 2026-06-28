package ru.bgitu.app.feature.teachers.teachers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import org.koin.compose.viewmodel.koinActivityViewModel
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.DateSelectorPager
import ru.bgitu.app.core.designsystem.component.calculateScheduleDate
import ru.bgitu.app.core.designsystem.component.rememberDateSelectorState
import ru.bgitu.app.core.designsystem.icon.CatInBox
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.WonderingCat
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.LessonItem
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.model.TeacherLesson
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.MeasureComposable
import ru.bgitu.app.core.utilui.component.LargeStatusCard
import ru.bgitu.app.core.utilui.component.LoadingIndicatorBox
import ru.bgitu.app.core.utilui.observeTime
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.feature.schedule.component.LessonItem
import ru.bgitu.app.feature.schedule.component.LessonVisualData
import ru.bgitu.app.feature.schedule.component.ScheduleTopAppBar
import ru.bgitu.app.feature.schedule.component.SleepyCatAnimation
import ru.bgitu.app.feature.teachers.teachers.component.TeacherModeBottomBar
import ru.bgitu.app.navigation.LocalResultStore
import kotlin.time.Duration.Companion.minutes

@Composable
fun TeacherModeScheduleScreen(
    onNavigateToTeacherSearch: (resultKey: String) -> Unit
) {
    val viewModel = koinActivityViewModel<TeacherModeScheduleViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val resultStore = LocalResultStore.current
    val resultTeacher = resultStore.getResultState<String?>(RESULT_KEY_TEACHER)
    LaunchedEffect(resultTeacher) {
        resultTeacher?.let {
            resultStore.removeResult<String?>(RESULT_KEY_TEACHER)
            viewModel.setTeacher(Teacher(it))
        }
    }

    TeacherModeScheduleScreenContent(
        uiState = uiState,
        startDate = startDate,
        onSelectDate = viewModel::onDateSelected,
        onNavigateToTeacherSearch = { onNavigateToTeacherSearch(RESULT_KEY_TEACHER) }
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun TeacherModeScheduleScreenContent(
    uiState: TeacherScheduleUiState,
    startDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    onNavigateToTeacherSearch: () -> Unit
) {
    val dateSelectorState = rememberDateSelectorState(
        initialDate = { startDate },
        onDateSelected = onSelectDate
    )
    val now by observeTime { 1.minutes }
    val hazeState = rememberHazeState()
    var blurTopAppBar by remember { mutableStateOf(false) }
    val blurAlpha by animateFloatAsState(
        if (blurTopAppBar) 1f else 0f,
    )
    val features = LocalFeatures.current

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            Column(
                modifier = Modifier
                    .then(
                        if (features.blurEnabled) {
                            Modifier.hazeEffect(
                                state = hazeState,
                                style = HazeMaterials.ultraThick(AppTheme.colorScheme.background2)
                            ) {
                                alpha = blurAlpha
                                progressive = HazeProgressive.verticalGradient(
                                    startIntensity = 1f,
                                    endIntensity = 0f,
                                    easing = CubicBezierEasing(0.9f, 0f, 1f, 1f),
                                    preferPerformance = !features.highQualityBlur
                                )
                                blurEnabled = true
                            }
                        } else Modifier.background(AppTheme.colorScheme.background2)
                    )
                    .statusBarsPadding()
            ) {
                ScheduleTopAppBar(
                    termStartDate = uiState.termStartDate,
                    now = now.date,
                    dateSelectorState = dateSelectorState,
                    scheduleUpdatedAt = null
                )

                DateSelectorPager(
                    state = dateSelectorState,
                    contentPadding = PaddingValues(16.dp),
                    showGlance = blurTopAppBar,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        bottomBar = {
            TeacherModeBottomBar(
                teacher = uiState.teacher,
                onClickSettings = onNavigateToTeacherSearch,
                hazeState = hazeState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = LocalExternalPadding.current.calculateBottomPadding() + 10.dp
                    )
            )
        },
        modifier = Modifier
            .navigationBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .then(
                    if (features.blurEnabled) {
                        Modifier.hazeSource(hazeState)
                    } else Modifier
                )
        ) {
            CompositionLocalProvider(LocalExternalPadding provides innerPadding) {
                when (uiState.scheduleState) {
                    is TeacherScheduleState.Loading -> LoadingContent()
                    is TeacherScheduleState.TeacherNotSelected -> {
                        TeacherNotSelectedContent(onNavigateToTeacher = onNavigateToTeacherSearch)
                    }
                    is TeacherScheduleState.Failed -> FailedToLoadContent()
                    else -> {
                        HorizontalPager(
                            state = dateSelectorState.schedulePager,
                            beyondViewportPageCount = 1,
                            modifier = Modifier
                                .fillMaxSize()
                        ) { page ->
                            val scheduleDate = remember(page, dateSelectorState.anchorDate) {
                                calculateScheduleDate(page, dateSelectorState.anchorDate)
                            }

                            AnimatedContent(
                                targetState = uiState.scheduleState,
                            ) { scheduleState ->
                                when (scheduleState) {
                                    is TeacherScheduleState.Success -> {
                                        val listState = rememberLazyListState()

                                        LaunchedEffect(listState, page) {
                                            snapshotFlow {
                                                listState.canScrollBackward to (dateSelectorState.schedulePager.settledPage == page)
                                            }
                                                .collectLatest { (canScroll, isCurrent) ->
                                                    if (isCurrent) blurTopAppBar = canScroll
                                                }
                                        }

                                        SuccessContent(
                                            state = listState,
                                            lessons = scheduleState.schedule.getDaySchedule(
                                                scheduleDate,
                                                uiState.termStartDate
                                            ),
                                            scheduleDate = scheduleDate,
                                            now = now,
                                            animationProgress = dateSelectorState.animatable.value
                                        )
                                    }

                                    else -> Unit
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessContent(
    lessons: ImmutableList<TeacherLesson>,
    scheduleDate: LocalDate,
    animationProgress: Float,
    now: LocalDateTime,
    state: LazyListState
) {
    val theModifier = Modifier
        .alpha(
            getValueFromProgress(
                progress = animationProgress,
                min = 0.2f,
                max = 1f
            )
        )
        .scale(
            getValueFromProgress(
                progress = animationProgress,
                min = 0.95f,
                max = 1f
            )
        )

    if (lessons.isNotEmpty()) {
        MeasureComposable(
            composable = {
                Text(text = "0".repeat(5), style = MaterialTheme.typography.bodyLarge)
            }
        ) { minTimeSize ->
            LazyColumn(
                state = state,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = LocalExternalPadding.current + PaddingValues(bottom = 16.dp),
                modifier = theModifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(
                    items = lessons
                ) { index, lesson ->
                    val data = remember(lesson, index, scheduleDate) {
                        LessonVisualData(
                            isFirst = index == 0,
                            isLast = index == lessons.lastIndex,
                            isPassed = scheduleDate.atTime(lesson.endAt) <= now,
                            isOngoing = lesson.isOngoing(now, scheduleDate),
                            minTimeWidth = minTimeSize.width,
                            nextLessonStartTime = lessons.getOrNull(index + 1)?.startAt
                        )
                    }

                    LessonItem(
                        lesson = LessonItem.Teacher(lesson),
                        lessonDate = scheduleDate,
                        cardShape = AppCardDefaults.getVerticalListShape(
                            index = index,
                            size = lessons.size
                        ),
                        visualData = data,
                        onNavigateToTeacher = {},
                        modifier = Modifier
                    )
                }
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = theModifier
                .fillMaxSize()
                .padding(LocalExternalPadding.current)
                .navigationBarsPadding()
        ) {
            SleepyCatAnimation()
            Text(
                text = stringResource(R.string.feature_schedule_weekend),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colorScheme.foreground1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 20.dp)
            )
        }
    }
}

@Composable
private fun FailedToLoadContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalExternalPadding.current)
            .navigationBarsPadding()
    ) {
        Icon(
            imageVector = Icons.CatInBox,
            contentDescription = null,
            tint = AppTheme.colorScheme.cat,
            modifier = Modifier
                .size(192.dp)
        )
        Text(
            text = stringResource(R.string.feature_teachers_failedToLoadSchedule),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colorScheme.foreground1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(250.dp)
                .padding(top = 20.dp)
        )
    }
}

@Composable
private fun TeacherNotSelectedContent(
    onNavigateToTeacher: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalExternalPadding.current)
            .navigationBarsPadding()
    ) {
        LargeStatusCard(
            title = stringResource(R.string.feature_teachers_selectTeacher),
            subtitle = stringResource(R.string.feature_teachers_selectTeacher_description),
            action = stringResource(R.string.feature_teachers_selectTeacher),
            onClick = onNavigateToTeacher
        ) {
            Icon(
                imageVector = Icons.WonderingCat,
                contentDescription = null,
                modifier = Modifier
                    .size(192.dp)
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    LoadingIndicatorBox(
        modifier = Modifier
            .fillMaxSize()
    )
}

private fun getValueFromProgress(progress: Float, min: Float, max: Float): Float {
    return min + (max - min) * progress
}

private const val RESULT_KEY_TEACHER = "teacher"