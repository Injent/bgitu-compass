package ru.bgitu.app.feature.schedule

import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
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
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinActivityViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.DateSelectorPager
import ru.bgitu.app.core.designsystem.component.DateSelectorPagerState
import ru.bgitu.app.core.designsystem.component.DynamicAsyncImage
import ru.bgitu.app.core.designsystem.component.calculateScheduleDate
import ru.bgitu.app.core.designsystem.component.rememberDateSelectorState
import ru.bgitu.app.core.designsystem.icon.CatInBox
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.WonderingCat
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.model.Lesson
import ru.bgitu.app.core.model.LessonItem
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.util.CollectEvent
import ru.bgitu.app.core.util.checkNotificationsEnabled
import ru.bgitu.app.core.util.now
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.MeasureComposable
import ru.bgitu.app.core.utilui.component.LargeStatusCard
import ru.bgitu.app.core.utilui.component.LoadingIndicatorBox
import ru.bgitu.app.core.utilui.observeTime
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.feature.groups.Groups
import ru.bgitu.app.feature.groups.GroupsSearch
import ru.bgitu.app.feature.schedule.component.GroupTabs
import ru.bgitu.app.feature.schedule.component.LessonItem
import ru.bgitu.app.feature.schedule.component.LessonVisualData
import ru.bgitu.app.feature.schedule.component.ScheduleTopAppBar
import ru.bgitu.app.feature.schedule.component.SleepyCatAnimation
import ru.bgitu.app.feature.schedule.component.StatusBar
import ru.bgitu.app.feature.schedule.component.TurnOnNotificationsDialog
import ru.bgitu.app.feature.schedule.component.formatStatusDuration
import ru.bgitu.app.feature.schedule.component.getScheduleStatus
import ru.bgitu.app.feature.teachers.TeacherScheduleKey
import ru.bgitu.app.navigation.Navigator
import ru.bgitu.app.ui.EntryProvider
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Parcelize
@Serializable
data object ScheduleKey : NavKey, Parcelable

fun EntryProvider.scheduleScreen(
    navigator: Navigator,
    groupId: Int? = null,
    date: LocalDate? = null
) {
    entry<ScheduleKey>(
        clazzContentKey = { it }
    ) {
        val viewModel = koinActivityViewModel<ScheduleViewModel> {
            parametersOf(ScheduleViewModel.Params(date = date, groupId = groupId))
        }
        ScheduleScreen(
            viewModel = viewModel,
            onNavigateToGroups = { navigator.navigate(Groups) },
            onNavigateToGroupsSearch = {
                navigator.replaceAll(Groups, GroupsSearch)
            },
            onNavigateToTeacher = {
                navigator.navigate(TeacherScheduleKey(teacherName = it.fullName))
            }
        )
    }
}

@Composable
private fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onNavigateToGroups: () -> Unit,
    onNavigateToGroupsSearch: () -> Unit,
    onNavigateToTeacher: (Teacher) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()

    CollectEvent(viewModel.navigateToGroupSearchEvent) {
        onNavigateToGroupsSearch()
    }

    val dateSelectorState = rememberDateSelectorState(
        initialDate = { startDate },
        onDateSelected = viewModel::onDateSelected
    )
    CollectEvent(viewModel.setSelectedDateEvent) { date ->
        dateSelectorState.setDate(date = date)
    }

    ScheduleScreenContent(
        uiState = uiState,
        dateSelectorState = dateSelectorState,
        onSelectGroup = viewModel::onSelectGroup,
        onNavigateToGroups = onNavigateToGroups,
        onNavigateToGroupsSearch = onNavigateToGroupsSearch,
        onScheduleDropped = viewModel::onScheduleDropped,
        onNavigateToTeacher = onNavigateToTeacher,
        onHideNotificationsDialog = viewModel::dismissNotificationsDialog
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun ScheduleScreenContent(
    uiState: ScheduleUiState,
    dateSelectorState: DateSelectorPagerState,
    onSelectGroup: (Group) -> Unit,
    onNavigateToGroups: () -> Unit,
    onNavigateToGroupsSearch: () -> Unit,
    onScheduleDropped: () -> Unit,
    onNavigateToTeacher: (Teacher) -> Unit,
    onHideNotificationsDialog: () -> Unit
) {
    val context = LocalContext.current
    val now by observeTime { 1.minutes }
    val hazeState = rememberHazeState()
    var blurTopAppBar by rememberSaveable { mutableStateOf(false) }
    val blurAlpha by animateFloatAsState(
        if (blurTopAppBar) 1f else 0f,
    )
    val features = LocalFeatures.current
    val showGroups = uiState.groups.isNotEmpty() && uiState.scheduleState !is ScheduleState.Dropped

    val areNotificationsEnabled = remember { context.checkNotificationsEnabled() }
    if (SDK_INT >= 33 && !areNotificationsEnabled && uiState.showNotificationsDialog) {
        TurnOnNotificationsDialog(onDismissRequest = onHideNotificationsDialog)
    }

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
                    scheduleUpdatedAt = uiState.updatedAt
                )

                DateSelectorPager(
                    state = dateSelectorState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    showGlance = blurTopAppBar,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        bottomBar = {
            if (showGroups) {
                GroupTabs(
                    selectedGroup = uiState.selectedGroup,
                    groups = uiState.groups,
                    onSelect = onSelectGroup,
                    onClickSettings = onNavigateToGroups,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = LocalExternalPadding.current.calculateBottomPadding() + 10.dp
                        )
                        .navigationBarsPadding()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = uiState.loading
            ) { isLoading ->
                when {
                    isLoading -> LoadingContent()
                    uiState.scheduleState is ScheduleState.Dropped -> {
                        ScheduleDroppedContent(
                            onScheduleDropped = onScheduleDropped,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    uiState.groups.isEmpty() -> {
                        GroupNotSelectedContent(
                            onNavigateToGroups = onNavigateToGroupsSearch,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    uiState.scheduleState is ScheduleState.Failed -> {
                        FailedToLoadContent(modifier = Modifier.padding(innerPadding))
                    }
                    else -> {
                        HorizontalPager(
                            state = dateSelectorState.schedulePager,
                            beyondViewportPageCount = 1,
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (features.blurEnabled) {
                                        Modifier.hazeSource(hazeState)
                                    } else Modifier
                                )
                        ) { page ->
                            val scheduleDate = remember(page, dateSelectorState.anchorDate) {
                                calculateScheduleDate(page, dateSelectorState.anchorDate)
                            }

                            AnimatedContent(
                                targetState = uiState.scheduleState,
                            ) { scheduleState ->
                                when (scheduleState) {
                                    is ScheduleState.Loading -> LoadingContent()
                                    is ScheduleState.Success -> {
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
                                            innerPadding = innerPadding,
                                            state = listState,
                                            lessons = scheduleState.schedule.getDaySchedule(
                                                date = scheduleDate,
                                                termStartDate = uiState.termStartDate
                                            ),
                                            scheduleDate = scheduleDate,
                                            now = now,
                                            animationProgress = dateSelectorState.animatable.value,
                                            onNavigateToTeacher = onNavigateToTeacher
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
    lessons: ImmutableList<Lesson>,
    scheduleDate: LocalDate,
    animationProgress: Float,
    now: LocalDateTime,
    state: LazyListState,
    innerPadding: PaddingValues,
    onNavigateToTeacher: (Teacher) -> Unit,
) {
    val configuration = LocalConfiguration.current
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
        val time by observeTime { 1.seconds }
        val timeZone = remember { TimeZone.currentSystemDefault() }
        val status = remember(lessons, scheduleDate, timeZone, time) {
            if (scheduleDate == now.date) {
                getScheduleStatus(
                    lessons = lessons,
                    now = now,
                    scheduleDate = scheduleDate,
                    timeZone = timeZone
                )
            } else null
        }

        MeasureComposable(
            composable = {
                Text(text = "0".repeat(5), style = MaterialTheme.typography.bodyLarge)
            }
        ) { minTimeSize ->
            LazyColumn(
                state = state,
                verticalArrangement = Arrangement.spacedBy(3.dp),
                contentPadding = innerPadding + PaddingValues(bottom = 16.dp),
                modifier = theModifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (status != null) {
                    item {
                        val (stringRes, duration) = status
                        StatusBar(
                            text = stringResource(stringRes, formatStatusDuration(duration, configuration)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                    }
                }

                itemsIndexed(
                    items = lessons
                ) { index, lesson ->
                    val data = remember(lesson, index, scheduleDate, now, minTimeSize) {
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
                        lesson = LessonItem.Student(lesson),
                        lessonDate = scheduleDate,
                        cardShape = AppCardDefaults.getVerticalListShape(
                            index = index,
                            size = lessons.size
                        ),
                        visualData = data,
                        onNavigateToTeacher = {
                            lesson.teacherFullName?.let { onNavigateToTeacher(Teacher(it)) }
                        },
                        modifier = Modifier
                    )
                }

                if (now.date.isMay9() || scheduleDate.isMay9()) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
                        ) {
                            SleepyCatAnimation(isFireZ = true,)
                        }
                    }
                }
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = theModifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            val isJokeDay = remember {
                LocalDate.now().let { it.month == Month.APRIL && it.day == 1 }
            }

            if (isJokeDay) {
                DynamicAsyncImage(
                    imageUrl = "https://validator.bgitu-compass.ru/static/coolcat.gif",
                    contentDescription = null,
                    placeholder = {
                        SleepyCatAnimation()
                    },
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(32.dp)
                        .clip(RoundedCornerShape(34.dp))
                )
            } else {
                SleepyCatAnimation(isFireZ = now.date.isMay9() || scheduleDate.isMay9())
            }
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
private fun LoadingContent() {
    LoadingIndicatorBox(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun FailedToLoadContent(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
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
            text = stringResource(R.string.feature_schedule_failedToLoadSchedule),
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
private fun ScheduleDroppedContent(
    onScheduleDropped: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = LocalExternalPadding.current.calculateBottomPadding())
    ) {
        LargeStatusCard(
            title = stringResource(R.string.feature_schedule_dropped),
            subtitle = stringResource(R.string.feature_schedule_dropped_description),
            action = stringResource(R.string.feature_schedule_button_selectGroup),
            onClick = onScheduleDropped,
            icon = {
                Icon(
                    imageVector = Icons.WonderingCat,
                    contentDescription = null,
                    modifier = Modifier
                        .size(192.dp)
                )
            }
        )
    }
}

@Composable
private fun GroupNotSelectedContent(
    onNavigateToGroups: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = LocalExternalPadding.current.calculateBottomPadding())
    ) {
        LargeStatusCard(
            title = stringResource(R.string.feature_schedule_selectGroup),
            subtitle = stringResource(R.string.feature_schedule_selectGroup_description),
            action = stringResource(R.string.feature_schedule_button_selectGroup),
            onClick = onNavigateToGroups,
            icon = {
                Icon(
                    imageVector = Icons.WonderingCat,
                    contentDescription = null,
                    modifier = Modifier
                        .size(192.dp)
                )
            }
        )
    }
}

private fun getValueFromProgress(progress: Float, min: Float, max: Float): Float {
    return min + (max - min) * progress
}

private fun LocalDate.isMay9(): Boolean = day == 9 && month == Month.MAY