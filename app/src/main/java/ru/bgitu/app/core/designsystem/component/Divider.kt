package ru.bgitu.app.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun PerfectDashedDivider(
    modifier: Modifier = Modifier,
    dashColor: Color,
    backgroundColor: Color,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    thickness: Dp = 2.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        val width = size.width
        val height = size.height
        val dashPx = dashLength.toPx()
        val gapPx = gapLength.toPx()

        drawRect(
            color = backgroundColor,
            size = size
        )

        if (width <= dashPx) {
            drawLine(
                color = dashColor,
                start = Offset(0f, height / 2),
                end = Offset(width, height / 2),
                strokeWidth = height,
                cap = StrokeCap.Round
            )
        } else {
            val gapCount = ((width - dashPx) / (dashPx + gapPx)).roundToInt().coerceAtLeast(1)
            val dashCount = gapCount + 1

            val availableSpaceForGaps = width - (dashCount * dashPx)
            val actualGap = availableSpaceForGaps / gapCount

            for (i in 0 until dashCount) {
                val startX = i * (dashPx + actualGap)
                drawLine(
                    color = dashColor,
                    start = Offset(startX, height / 2),
                    end = Offset(startX + dashPx, height / 2),
                    strokeWidth = height,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}