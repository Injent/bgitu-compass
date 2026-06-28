package ru.bgitu.app.feature.schedule.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.DateSelectorPagerState
import ru.bgitu.app.core.designsystem.component.adjustDate
import ru.bgitu.app.core.designsystem.icon.CalendarDay
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.observeTime
import ru.bgitu.app.core.utilui.formatTimePassed
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTopAppBar(
    termStartDate: LocalDate,
    now: LocalDate,
    dateSelectorState: DateSelectorPagerState,
    scheduleUpdatedAt: Instant?,
    modifier: Modifier = Modifier
) {
    val timeZone = remember { TimeZone.currentSystemDefault() }
    val coroutineScope = rememberCoroutineScope()
    val resources = LocalResources.current
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            AnimatedVisibility(
                visible = remember(dateSelectorState.selectedDate, now) {
                    dateSelectorState.selectedDate != adjustDate(now)
                },
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it },
                modifier = Modifier
                    .offset(x = (-8).dp)
            ) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            dateSelectorState.setDate(now)
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppTheme.colorScheme.foreground1
                    )
                ) {
                    Text(
                        text = if (now.dayOfWeek == DayOfWeek.SUNDAY) {
                            stringResource(R.string.feature_schedule_forTomorrow)
                        } else {
                            stringResource(R.string.feature_schedule_forToday)
                        }
                    )
                }
            }
        }

        var showUpdateTime by rememberSaveable { mutableStateOf(false) }
        var hasShownAnimation by rememberSaveable { mutableStateOf(false) }
        var oldUpdateTime by rememberSaveable { mutableStateOf<Instant?>(null) }
        val time by rememberUpdatedState(scheduleUpdatedAt)

        LaunchedEffect(Unit) {
            snapshotFlow { time }
                .filter { it != null && !hasShownAnimation }
                .collect {
                    val oldTime = oldUpdateTime
                    if (oldTime == null) {
                        oldUpdateTime = it
                    } else if (it != null && oldTime < it) {
                        try {
                            oldUpdateTime = it
                            hasShownAnimation = true
                            showUpdateTime = true
                            delay(2.seconds)
                            showUpdateTime = false
                        } catch (_: CancellationException) {
                            showUpdateTime = false
                        }
                    }
                }
        }

        AnimatedContent(
            targetState = showUpdateTime,
            transitionSpec = {
                val tweenSpec = tween<Float>(easing = FastOutSlowInEasing)
                slideInVertically {
                    if (showUpdateTime) it
                    else -it
                } + fadeIn(tweenSpec) togetherWith slideOutVertically {
                    if (showUpdateTime) -it
                    else it
                } + fadeOut(tweenSpec)
            },
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { showUpdateTime = !showUpdateTime }
                )
        ) { showUpdateTime ->
            if (showUpdateTime) {
                val currentTime by observeTime { 1.minutes }
                val timePast = remember(scheduleUpdatedAt, currentTime) {
                    scheduleUpdatedAt?.formatTimePassed(
                        resources = resources,
                        from = currentTime.toInstant(timeZone)
                    ) ?: "..."
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            MaterialTheme.typography.titleMedium
                                .merge(color = AppTheme.colorScheme.foreground1)
                                .toSpanStyle()
                        ) {
                            append(stringResource(R.string.feature_schedule_refreshed))
                            appendLine()
                        }
                        append(timePast)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.colorScheme.foreground2,
                    softWrap = false,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                )
            } else {
                MonthWithWeekTitle(
                    selectedDate = dateSelectorState.selectedDate,
                    termStartDate = termStartDate
                )
            }
        }

        IconButton(
            onClick = { showDatePickerDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.CalendarDay,
                tint = AppTheme.colorScheme.foreground1,
                contentDescription = null,
            )
        }
    }

    if (showDatePickerDialog) {
        val state = rememberDatePickerState(
            initialSelectedDate = now.toJavaLocalDate(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return Instant.fromEpochMilliseconds(utcTimeMillis).toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    ).dayOfWeek != DayOfWeek.SUNDAY
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year in now.year..(now.year + 1)
                }
            }
        )

        ScheduleDatePickerDialog(
            state = state,
            onSelect = { date ->
                showDatePickerDialog = false
                coroutineScope.launch {
                    dateSelectorState.setDate(date)
                }
            },
            onDismissRequest = { showDatePickerDialog = false },
        )
    }
}