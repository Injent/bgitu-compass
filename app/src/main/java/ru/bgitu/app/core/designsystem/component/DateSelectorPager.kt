package ru.bgitu.app.core.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ru.bgitu.app.core.util.now
import ru.bgitu.app.core.util.previousOrSame
import kotlin.math.abs

@Stable
class DateSelectorPagerState(
    coroutineScope: CoroutineScope,
    val schedulePager: PagerState,
    val weekPager: PagerState,
    val animatable: Animatable<Float, AnimationVector1D>,
    private var selectedDateState: MutableState<LocalDate>,
    val anchorDate: LocalDate
) {
    var selectedDate: LocalDate
        get() = selectedDateState.value
        private set(value) { selectedDateState.value = value }

    init {
        coroutineScope.launch {
            snapshotFlow { schedulePager.currentPage }
                .drop(1)
                .conflate()
                .collect { currentPage ->
                    val date = calculateScheduleDate(currentPage, anchorDate)

                    selectedDate = date
                    weekPager.animateScrollToPage(
                        page = calculateWeekPage(date, currentPage, anchorDate),
                        animationSpec = tween(durationMillis = 600, easing = EaseInOutExpo)
                    )
                }
        }
    }

    suspend fun setDate(date: LocalDate, playAnimation: Boolean = true) = coroutineScope {
        selectedDate = adjustDate(date)
        val schedulePage = calculateSchedulePage(selectedDate, anchorDate)
        launch {
            if (schedulePage == schedulePager.currentPage) return@launch
            if (playAnimation) {
                animateProgressAndScroll(
                    pagerState = schedulePager,
                    progress = animatable,
                    page = schedulePage
                )
            } else {
                schedulePager.scrollToPage(
                    page = schedulePage
                )
            }
        }
        launch {
            val weekPagerPage =
                calculateWeekPage(selectedDate, schedulePage, anchorDate)
            if (weekPagerPage == weekPager.currentPage) return@launch

            weekPager.animateScrollToPage(
                page = weekPagerPage
            )
        }
    }

    private suspend fun animateProgressAndScroll(
        pagerState: PagerState,
        progress: Animatable<Float, AnimationVector1D>,
        page: Int,
    ) {
        progress.animateTo(
            targetValue = 0f,
            animationSpec = spring()
        )
        pagerState.scrollToPage(page)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        )
    }
}

private val LocalDateSaver = Saver<LocalDate, String>(
    save = { it.toString() },
    restore = { LocalDate.parse(it) }
)

@Composable
fun rememberDateSelectorState(
    initialDate: () -> LocalDate = { LocalDate.now() },
    onDateSelected: (LocalDate) -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): DateSelectorPagerState {
    val anchorDate = rememberSaveable(saver = LocalDateSaver) {
        adjustDate(date = initialDate())
    }
    val date = rememberSaveable(stateSaver = LocalDateSaver) {
        mutableStateOf(anchorDate)
    }
    val onDateSelected by rememberUpdatedState(onDateSelected)

    LaunchedEffect(date, onDateSelected) {
        snapshotFlow { date.value }
            .drop(1)
            .collectLatest { onDateSelected(date.value) }
    }
    val initialSchedulePage = remember {
        calculateSchedulePage(date.value, anchorDate)
    }
    val weekPagerState = rememberPagerState(
        initialPage = calculateWeekPage(date.value, initialSchedulePage, anchorDate),
        pageCount = { WEEK_PAGE_COUNT }
    )
    val schedulePagerState = rememberPagerState(
        initialPage = initialSchedulePage,
        pageCount = { SCHEDULE_PAGE_COUNT }
    )
    val scheduleAnimatable = remember {
        Animatable(1f)
    }

    return remember(coroutineScope, schedulePagerState, weekPagerState, scheduleAnimatable, date, anchorDate) {
        DateSelectorPagerState(
            coroutineScope = coroutineScope,
            schedulePager = schedulePagerState,
            weekPager = weekPagerState,
            selectedDateState = date,
            animatable = scheduleAnimatable,
            anchorDate = anchorDate
        )
    }
}

@Composable
fun DateSelectorPager(
    state: DateSelectorPagerState,
    modifier: Modifier = Modifier,
    pageSpacing: Dp = 16.dp,
    showGlance: Boolean = false,
    contentPadding: PaddingValues = PaddingValues()
) {
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = state.weekPager,
        pageSpacing = pageSpacing,
        contentPadding = contentPadding,
        modifier = modifier
    ) { page ->
        val dates = remember(page) {
            getDays(calculateWeekStartDate(page, state.anchorDate))
        }

        WeekedDateSelector(
            dates = dates,
            selectedDate = state.selectedDate,
            showGlance = showGlance,
            onDateSelected = { date ->
                scope.launch {
                    state.setDate(date)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

fun calculateScheduleDate(schedulePage: Int, anchorDate: LocalDate = LocalDate.now()): LocalDate {
    val startDate = adjustDate(anchorDate)
    val daysOffset = schedulePage - SCHEDULE_PAGE_COUNT / 2
    var currentDate = startDate
    var actualDaysCount = 0
    val direction = if (daysOffset > 0) 1 else -1

    while (actualDaysCount < abs(daysOffset)) {
        currentDate = currentDate.plus(direction, DateTimeUnit.DAY)

        if (currentDate.dayOfWeek.isoDayNumber != 7) {
            actualDaysCount++
        }
    }
    return currentDate
}

private fun getDays(startWeekDate: LocalDate): List<LocalDate> {
    return (0 until STUDY_DAYS_IN_WEEK)
        .map { weekDayOffset ->
            startWeekDate.plus(weekDayOffset, DateTimeUnit.DAY)
        }
}

private fun calculateWeekPage(date: LocalDate, schedulePage: Int, anchorDate: LocalDate = LocalDate.now()): Int {
    val now = adjustDate(anchorDate)
    val startOfWeek = date.previousOrSame(DayOfWeek.MONDAY)
    val diff = startOfWeek.daysUntil(date)
    return ((schedulePage - diff) / STUDY_DAYS_IN_WEEK).let { page ->
        if (now.dayOfWeek > DayOfWeek.WEDNESDAY) {
            page + 1
        } else page
    }
}

private fun calculateWeekStartDate(weekPage: Int, anchorDate: LocalDate = LocalDate.now()): LocalDate {
    val weekOffset = weekPage - WEEK_PAGE_COUNT / 2
    var weekDate = adjustDate(anchorDate).plus(weekOffset, DateTimeUnit.WEEK)

    while (weekDate.dayOfWeek != DayOfWeek.MONDAY) {
        weekDate = weekDate.minus(1, DateTimeUnit.DAY)
    }
    return weekDate
}

private fun calculateSchedulePage(date: LocalDate, anchorDate: LocalDate = LocalDate.now()): Int {
    val startDate = adjustDate(anchorDate)
    var currentDate = startDate
    var currentPage = SCHEDULE_PAGE_COUNT / 2
    val direction = if (date > startDate) 1 else -1

    while (currentDate != date) {
        currentDate = currentDate.plus(direction, DateTimeUnit.DAY)

        if (currentDate.dayOfWeek.isoDayNumber != 7) {
            currentPage += direction
        }

        if (currentDate == date) break
    }
    return currentPage
}

fun adjustDate(date: LocalDate): LocalDate {
    return if (date.dayOfWeek == DayOfWeek.SUNDAY) {
        date.plus(1, DateTimeUnit.DAY)
    } else date
}

private const val STUDY_DAYS_IN_WEEK = 6
private const val SCHEDULE_PAGE_COUNT = 1000
private const val WEEK_PAGE_COUNT = SCHEDULE_PAGE_COUNT / STUDY_DAYS_IN_WEEK