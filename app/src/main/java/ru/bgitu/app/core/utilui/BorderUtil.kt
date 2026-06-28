package ru.bgitu.app.core.utilui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp


enum class TaperedBorderSide {
    Top, Bottom, Start, End
}

fun Modifier.taperedBorder(
    side: TaperedBorderSide,
    strokeWidth: Dp,
    color: Color,
    shape: Shape,
    extensionSize: Dp = 32.dp
): Modifier = this.drawWithCache {
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path().apply { addOutline(outline) }

    val strokeWidthPx = strokeWidth.toPx()
    val extensionPx = extensionSize.toPx()

    val resolvedSide = when (side) {
        TaperedBorderSide.Start -> if (layoutDirection == LayoutDirection.Ltr) TaperedBorderSide.Start else TaperedBorderSide.End
        TaperedBorderSide.End -> if (layoutDirection == LayoutDirection.Ltr) TaperedBorderSide.End else TaperedBorderSide.Start
        else -> side
    }

    val brush = when (resolvedSide) {
        TaperedBorderSide.Top -> {
            val fadeEnd = extensionPx
            val solidStart = extensionPx * 0.2f
            Brush.verticalGradient(
                0.0f to color,
                (solidStart / size.height).coerceIn(0f, 1f) to color,
                (fadeEnd / size.height).coerceIn(0f, 1f) to Color.Transparent,
                1.0f to Color.Transparent
            )
        }
        TaperedBorderSide.Bottom -> {
            val fadeStart = size.height - extensionPx
            val solidEnd = size.height - (extensionPx * 0.2f)
            Brush.verticalGradient(
                0.0f to Color.Transparent,
                (fadeStart / size.height).coerceIn(0f, 1f) to Color.Transparent,
                (solidEnd / size.height).coerceIn(0f, 1f) to color,
                1.0f to color
            )
        }
        else -> {
            val isLeft = resolvedSide == TaperedBorderSide.Start
            if (isLeft) {
                val fadeEnd = extensionPx
                val solidStart = extensionPx * 0.2f
                Brush.horizontalGradient(
                    0.0f to color,
                    (solidStart / size.width).coerceIn(0f, 1f) to color,
                    (fadeEnd / size.width).coerceIn(0f, 1f) to Color.Transparent,
                    1.0f to Color.Transparent
                )
            } else { // Right
                val fadeStart = size.width - extensionPx
                val solidEnd = size.width - (extensionPx * 0.2f)
                Brush.horizontalGradient(
                    0.0f to Color.Transparent,
                    (fadeStart / size.width).coerceIn(0f, 1f) to Color.Transparent,
                    (solidEnd / size.width).coerceIn(0f, 1f) to color,
                    1.0f to color
                )
            }
        }
    }

    onDrawWithContent {
        drawContent()

        drawPath(
            path = path,
            brush = brush,
            style = Stroke(
                width = strokeWidthPx,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}