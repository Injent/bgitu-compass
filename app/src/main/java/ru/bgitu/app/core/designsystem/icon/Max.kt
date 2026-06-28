package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Max: ImageVector
    get() {
        if (_MAX != null) {
            return _MAX!!
        }
        _MAX = ImageVector.Builder(
            name = "MAX",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 1000f,
            viewportHeight = 1000f
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF44CCFF),
                        0.7f to Color(0xFF5533EE),
                        1f to Color(0xFF9933DD)
                    ),
                    start = Offset(117.8f, 760.5f),
                    end = Offset(1000f, 500f)
                )
            ) {
                moveTo(249.7f, 0f)
                lineTo(750.3f, 0f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1000f, 249.7f)
                lineTo(1000f, 750.3f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 750.3f, 1000f)
                lineTo(249.7f, 1000f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 750.3f)
                lineTo(0f, 249.7f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 249.7f, 0f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Blue,
                        1f to Color.Transparent.copy(alpha = 0f),
                        1f to Color.Transparent.copy(alpha = 0f)
                    ),
                    center = Offset(-23.8f, -439.2f),
                    radius = 1213.5f
                )
            ) {
                moveTo(249.7f, 0f)
                lineTo(750.3f, 0f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1000f, 249.7f)
                lineTo(1000f, 750.3f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 750.3f, 1000f)
                lineTo(249.7f, 1000f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 750.3f)
                lineTo(0f, 249.7f)
                arcTo(249.7f, 249.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 249.7f, 0f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(508.2f, 878.3f)
                curveToRelative(-75f, 0f, -109.9f, -10.9f, -170.5f, -54.8f)
                curveToRelative(-38.3f, 49.3f, -159.7f, 87.8f, -165f, 21.9f)
                curveToRelative(0f, -49.5f, -10.9f, -91.2f, -23.4f, -136.9f)
                curveToRelative(-14.8f, -56.2f, -31.6f, -118.8f, -31.6f, -209.5f)
                curveToRelative(0f, -216.6f, 177.8f, -379.6f, 388.4f, -379.6f)
                curveToRelative(210.8f, 0f, 375.9f, 171f, 375.9f, 381.6f)
                curveToRelative(0.7f, 207.3f, -166.6f, 376.1f, -373.9f, 377.2f)
                moveToRelative(3.1f, -571.6f)
                curveToRelative(-102.6f, -5.3f, -182.5f, 65.7f, -200.2f, 177f)
                curveToRelative(-14.6f, 92.2f, 11.3f, 204.4f, 33.4f, 210.2f)
                curveToRelative(10.6f, 2.6f, 37.2f, -19f, 53.8f, -35.6f)
                arcToRelative(189.8f, 189.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 92.7f, 33f)
                curveToRelative(106.3f, 5.1f, 197.1f, -75.8f, 204.2f, -181.9f)
                curveToRelative(4.2f, -106.4f, -77.7f, -196.5f, -184f, -202.6f)
                close()
            }
        }.build()

        return _MAX!!
    }

@Suppress("ObjectPropertyName")
private var _MAX: ImageVector? = null
