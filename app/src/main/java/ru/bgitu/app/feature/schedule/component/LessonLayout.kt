package ru.bgitu.app.feature.schedule.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.LessonItem
import ru.bgitu.app.core.utilui.observeTime
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

enum class LessonComponentId {
    TIME,
    INDICATOR,
    LONG_BREAK,
    SHORT_BREAK,
    LESSON_CARD,
    INDICATOR_GRADIENT
}

data class LessonLayoutParams(
    val indicatorSpaceWidth: Dp,
    val gap: Dp,
    val verticalGap: Dp,
    val firstItem: Boolean
)


data class LessonVisualData(
    val isFirst: Boolean,
    val isLast: Boolean,
    val isPassed: Boolean,
    val isOngoing: Boolean,
    val minTimeWidth: Dp,
    val nextLessonStartTime: LocalTime?,
)

@Composable
fun LessonItem(
    lesson: LessonItem,
    lessonDate: LocalDate,
    cardShape: RoundedCornerShape,
    visualData: LessonVisualData,
    onNavigateToTeacher: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val timeZone = remember { TimeZone.currentSystemDefault() }
    val now by observeTime { 1.seconds }
    val progress = remember(lesson.startAt, lesson.endAt, now, lessonDate, timeZone) {
        val start = lessonDate.atTime(lesson.startAt).toInstant(timeZone)
        val end = lessonDate.atTime(lesson.endAt).toInstant(timeZone)
        val current = now.toInstant(timeZone)

        when {
            current < start -> 0f
            current > end -> 1f
            else -> {
                val total = (end - start).inWholeSeconds.toFloat()
                if (total > 0) (current - start).inWholeSeconds.toFloat() / total else 1f
            }
        }
    }

    val layoutParams = remember(visualData.isFirst) {
        LessonLayoutParams(
            indicatorSpaceWidth = 12.dp,
            gap = 16.dp,
            verticalGap = 8.dp,
            firstItem = visualData.isFirst
        )
    }
    LessonLayout(
        modifier = modifier,
        params = layoutParams
    ) {
        val startTime = remember(lesson.startAt) {
            lesson.startAt.format(
                LocalTime.Format {
                    hour()
                    char(':')
                    minute()
                }
            )
        }
        val endTime = remember(lesson.endAt) {
            lesson.endAt.format(
                LocalTime.Format {
                    hour()
                    char(':')
                    minute()
                }
            )
        }

        Text(
            text = buildAnnotatedString {
                append(startTime)
                withStyle(
                    MaterialTheme.typography.bodyMedium.toSpanStyle()
                        .copy(
                            color = AppTheme.colorScheme.foreground2,
                            fontWeight = FontWeight.Medium
                        )
                ) {
                    appendLine()
                    append(endTime)
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colorScheme.foreground1,
            maxLines = 2,
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .width(visualData.minTimeWidth)
                .layoutId(LessonComponentId.TIME)
        )
        LessonIndicator(
            visualData = visualData,
            progress = { progress },
            modifier = Modifier.layoutId(LessonComponentId.INDICATOR)
        )
        LessonBreakPoint(
            isBreakPoint = false,
            animated = visualData.isOngoing,
            highlighted = visualData.isPassed || visualData.isOngoing,
            modifier = Modifier.layoutId(LessonComponentId.LONG_BREAK),
        ) {
            val startStatus = remember(lesson.startAt, now, lessonDate, timeZone) {
                calculateLessonStatus(
                    lessonStart = lessonDate.atTime(lesson.startAt),
                    lessonEnd = lessonDate.atTime(lesson.endAt),
                    now = now,
                    timeZone = timeZone
                )
            }

            Ticker(
                text = when (startStatus) {
                    is LessonStartStatus.Future -> {
                        val timeStr = formatDuration(startStatus.timeLeft, configuration)
                        stringResource(R.string.feature_schedule_lesson_starts_in, timeStr)
                    }
                    is LessonStartStatus.Finished -> {
                        val timeStr = formatDuration(startStatus.timeSince, configuration)
                        stringResource(R.string.feature_schedule_lesson_finished, timeStr)
                    }
                    is LessonStartStatus.Past -> {
                        val timeStr = formatDuration(startStatus.timeSince, configuration)
                        stringResource(R.string.feature_schedule_lesson_started_ago, timeStr)
                    }
                }
            )
        }
        if (visualData.isOngoing) {
            LessonBreakPoint(
                isBreakPoint = true,
                highlighted = true,
                modifier = Modifier.layoutId(LessonComponentId.SHORT_BREAK)
            ) {
                val breakStatus = remember(lesson.startAt, now, lessonDate, timeZone) {
                    calculateBreakStatus(
                        lessonStart = lessonDate.atTime(lesson.startAt),
                        now = now,
                        timeZone = timeZone
                    )
                }

                Ticker(
                    text = when (breakStatus) {
                        is LessonBreakStatus.Waiting -> {
                            val timeStr = formatDuration(breakStatus.timeLeft, configuration)
                            stringResource(R.string.break_starts_in, timeStr)
                        }
                        is LessonBreakStatus.Ongoing -> {
                            val timeStr = formatDuration(breakStatus.timeLeft, configuration)
                            stringResource(R.string.break_ends_in, timeStr)
                        }
                        is LessonBreakStatus.Finished -> {
                            val timeStr = formatDuration(breakStatus.timeSince, configuration)
                            stringResource(R.string.break_finished_ago, timeStr)
                        }
                    }
                )
            }
        }

        LessonCard(
            lesson = lesson,
            date = lessonDate,
            shape = cardShape,
            isOngoing = visualData.isOngoing,
            onNavigateToTeacher = onNavigateToTeacher,
            modifier = Modifier
                .layoutId(LessonComponentId.LESSON_CARD)
                .clip(
                    when {
                        visualData.isFirst && visualData.isLast -> SingleItemShape
                        visualData.isFirst -> TopShape
                        visualData.isLast -> BottomShape
                        else -> RegularShape
                    }
                )
        )

        if (visualData.isLast) {
            Box(
                modifier = Modifier
                    .layoutId(LessonComponentId.INDICATOR_GRADIENT)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, AppTheme.colorScheme.background2),
                        )
                    )
            )
        }
    }
}

@Composable
private fun LessonLayout(
    params: LessonLayoutParams,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    val indicatorSpaceWidthPx = with(density) {
        params.indicatorSpaceWidth.toPx().roundToInt()
    }
    val gap = with(density) {
        params.gap.toPx().roundToInt()
    }
    val verticalGap = with(density) {
        params.verticalGap.toPx().roundToInt()
    }

    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->

        // Time column
        val time = measurables.createPlaceable(LessonComponentId.TIME).measure(constraints)

        val indicatorOffsetX = time.width + gap

        val lessonCardConstraints = constraints.copy(
            maxWidth = (constraints.maxWidth - (indicatorOffsetX + gap)).coerceAtLeast(0)
        )
        val lessonCard = measurables.createPlaceable(LessonComponentId.LESSON_CARD)
            .measure(lessonCardConstraints)

        // Indicator
        val indicatorConstraints = constraints.copy(
            maxWidth = indicatorSpaceWidthPx,
        )
        val indicator = measurables.createPlaceable(LessonComponentId.INDICATOR)
            .measure(indicatorConstraints.copy(
                minHeight = lessonCard.height,
                maxHeight = lessonCard.height
            ))
        val longBreak = measurables.createPlaceable(LessonComponentId.LONG_BREAK)
            .measure(constraints)
        val shortBreak = measurables.createOptionalPlaceable(LessonComponentId.SHORT_BREAK)
            ?.measure(constraints)

        val breakPointOffsetY = -(longBreak.height + verticalGap) / 2

        val leftTimeIndicator = measurables.createOptionalPlaceable(LessonComponentId.INDICATOR_GRADIENT)
            ?.measure(indicatorConstraints.copy(
                minWidth = indicatorConstraints.maxWidth,
                maxWidth = indicatorConstraints.maxWidth,
                minHeight = lessonCard.height / 3,
                maxHeight = lessonCard.height / 3
            ))

        layout(constraints.maxWidth, lessonCard.height) {
            time.placeRelative(x = 0, y = 0)

            indicator.placeRelative(
                x = indicatorOffsetX - (indicator.width / 2),
                y = 0
            )
            longBreak.placeRelative(
                x = indicatorOffsetX - (longBreak.width / 2),
                y = if (params.firstItem) 0 else breakPointOffsetY
            )
            shortBreak?.placeRelative(
                x = indicatorOffsetX - (shortBreak.width / 2),
                y = (lessonCard.height / 2) - (shortBreak.height / 2)
            )
            lessonCard.placeRelative(
                x = indicatorOffsetX + gap,
                y = 0
            )

            leftTimeIndicator?.placeRelative(
                x = indicatorOffsetX - leftTimeIndicator.width / 2,
                y = indicator.height - leftTimeIndicator.height + 15
            )
        }
    }
}

private fun List<Measurable>.createOptionalPlaceable(layoutId: LessonComponentId): Measurable? {
    return this.firstOrNull { it.layoutId == layoutId }
}

private fun List<Measurable>.createPlaceable(layoutId: LessonComponentId): Measurable {
    return requireNotNull(this.firstOrNull { it.layoutId == layoutId }) {
        "Element with layoutId = $layoutId not found in compose tree"
    }
}

private val RegularShape = RoundedCornerShape(4.dp)

private val TopShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = 4.dp,
    bottomEnd = 4.dp
)

private val BottomShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 4.dp,
    bottomStart = 20.dp,
    bottomEnd = 20.dp
)

private val SingleItemShape = RoundedCornerShape(20.dp)