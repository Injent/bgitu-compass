package ru.bgitu.app.feature.schedule.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import ru.bgitu.app.core.designsystem.icon.Bell
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.SmallClock
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
internal fun LessonIndicator(
    visualData: LessonVisualData,
    progress: () -> Float,
    modifier: Modifier = Modifier
) {
    val progressUpdatedState by rememberUpdatedState(progress)
    val showProgressBar by remember(progressUpdatedState) {
        derivedStateOf {
            progressUpdatedState().let { it > 0f && it < 1f }
        }
    }
    Box(
        modifier = modifier
            .padding(top = if (visualData.isFirst) 18.dp else 8.dp, bottom = 13.dp)
    ) {
        if (showProgressBar) {
            Layout(
                content = {
                    LinearWavyProgressIndicator(
                        progress = progressUpdatedState,
                        color = AppTheme.colorScheme.brand,
                        trackColor = AppTheme.colorScheme.stroke1,
                        waveSpeed = 15.dp,
                        amplitude = {
                            if (it <= 0.1f || it >= 0.95f) {
                                0f
                            } else {
                                0.4f
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(90f)
                    )
                }
            ) { measurables, constraints ->
                val indicatorPlaceable = measurables.first().measure(
                    constraints.copy(
                        minWidth = constraints.minHeight,
                        maxWidth = constraints.maxHeight,
                        minHeight = constraints.minWidth,
                        maxHeight = constraints.maxWidth
                    )
                )

                layout(indicatorPlaceable.height, indicatorPlaceable.width) {
                    indicatorPlaceable.placeRelativeWithLayer(
                        x = (indicatorPlaceable.height - indicatorPlaceable.width) / 2,
                        y = (indicatorPlaceable.width - indicatorPlaceable.height) / 2
                    )
                }
            }
        } else {
            val strokeWidth = 4.dp
            val gap = 4.dp
            val color = if (visualData.isPassed) AppTheme.colorScheme.brand else AppTheme.colorScheme.foreground3

            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 2.dp)
                    .drawBehind {
                        val strokeWidthPx = strokeWidth.toPx()
                        val gapPx = gap.toPx()
                        val segmentHeight = size.height / 2

                        drawLine(
                            color = color,
                            start = Offset(size.width / 2, 0f),
                            end = Offset(size.width / 2, segmentHeight - gapPx),
                            strokeWidth = strokeWidthPx,
                            cap = StrokeCap.Round
                        )

                        drawLine(
                            color = color,
                            start = Offset(size.width / 2, segmentHeight + gapPx),
                            end = Offset(size.width / 2, size.height),
                            strokeWidth = strokeWidthPx,
                            cap = StrokeCap.Round
                        )
                    }
            )
        }
    }
}


@Composable
internal fun LessonBreakPoint(
    isBreakPoint: Boolean,
    modifier: Modifier = Modifier,
    animated: Boolean = false,
    enabled: Boolean = true,
    highlighted: Boolean = false,
    popup: @Composable () -> Unit,
) {
    var isPopupVisible by rememberSaveable { mutableStateOf(false) }
    val pointSize = if (isBreakPoint) 24.dp else 22.dp
    val pointBorderWidth = if (isBreakPoint) 3.5.dp else 2.8.dp

    val pointFillColor = if (animated) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        infiniteTransition.animateColor(
            initialValue = Color.White,
            targetValue = AppTheme.colorScheme.background2,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pointFillColor"
        ).value
    } else Color.White

    val circleColor = if (enabled || highlighted) {
        pointFillColor
    } else AppTheme.colorScheme.foreground3

    val strokeColor = if (enabled && highlighted) {
        AppTheme.colorScheme.brand
    } else AppTheme.colorScheme.foreground3
    val breakStrokeColor = AppTheme.colorScheme.background2

    Box(
        modifier = modifier
            .size(if (enabled || highlighted) pointSize else 10.dp)
            .drawBehind {
                if (false) {
                    drawCircle(
                        color = circleColor,
                        radius = size.minDimension / 2f - pointBorderWidth.toPx() / 3f
                    )

                    if (enabled || highlighted) {
                        drawCircle(
                            color = strokeColor,
                            radius = size.minDimension / 2f - pointBorderWidth.toPx() / 2f,
                            style = Stroke(
                                width = pointBorderWidth.toPx()
                            )
                        )
                    }
                } else {
                    drawCircle(
                        color = strokeColor,
                        radius = size.minDimension / 2f - pointBorderWidth.toPx() / 2f
                    )
                    drawCircle(
                        color = breakStrokeColor,
                        radius = size.minDimension / 1.75f - pointBorderWidth.toPx() / 1.75f,
                        style = Stroke(
                            width = pointBorderWidth.toPx()
                        )
                    )
                }
            }
            .clickable(
                interactionSource = null,
                indication = null,
            ) { isPopupVisible = !isPopupVisible }
    ) {
        Icon(
            imageVector = if (isBreakPoint) Icons.SmallClock else Icons.Bell,
            contentDescription = null,
            tint = AppTheme.colorScheme.onBrand,
            modifier = Modifier.align(Alignment.Center)
        )
        if (isPopupVisible && enabled) {
            val density = LocalDensity.current
            val popOffsetX = if (isBreakPoint) {
                0
            } else with(density) {
                -2.5.dp.roundToPx()
            }

            Popup(
                alignment = Alignment.CenterStart,
                offset = IntOffset(x = popOffsetX, y = 0),
                onDismissRequest = { isPopupVisible = false }
            ) {
                popup()
            }
        }
    }
}

@Composable
fun Ticker(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .background(
                color = AppTheme.colorScheme.brand,
                shape = CircleShape
            )
            .padding(
                horizontal = 8.dp,
                vertical = 6.dp
            )
    ) {
        Spacer(
            modifier = Modifier
                .size(12.dp)
                .background(Color.White, CircleShape)
        )
        Text(
            text = text,
            color = AppTheme.colorScheme.onBrand,
            style = MaterialTheme.typography.bodyMedium
                .copy(lineHeight = 14.sp),
            modifier = Modifier.widthIn(max = 300.dp)
        )
    }
}
