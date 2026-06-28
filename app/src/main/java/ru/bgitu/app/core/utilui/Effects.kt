package ru.bgitu.app.core.utilui

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

fun Modifier.horizontalFadingEdges(
    scrollState: ScrollState,
    color: Color,
    edgeWidth: Dp = 32.dp,
): Modifier = this.composed {
    this.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val width = size.width
            val edgeWidthPx = edgeWidth.toPx()

            val currentLeftEdgeWidth = min(scrollState.value.toFloat(), edgeWidthPx)

            val remainingScroll = scrollState.maxValue - scrollState.value
            val currentRightEdgeWidth = min(remainingScroll.toFloat(), edgeWidthPx)

            if (currentLeftEdgeWidth > 0f) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, color),
                        startX = 0f,
                        endX = currentLeftEdgeWidth
                    ),
                    blendMode = BlendMode.DstIn
                )
            }

            if (currentRightEdgeWidth > 0f) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(color, Color.Transparent),
                        startX = width - currentRightEdgeWidth,
                        endX = width
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
        }
}