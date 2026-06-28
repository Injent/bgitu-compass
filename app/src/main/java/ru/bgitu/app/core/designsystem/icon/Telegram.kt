package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Telegram: ImageVector
    get() {
        if (_Telegram != null) {
            return _Telegram!!
        }
        _Telegram = ImageVector.Builder(
            name = "Telegram",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 240f,
            viewportHeight = 240f
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF1D93D2),
                        1f to Color(0xFF38B0E3)
                    ),
                    start = Offset(120f, 240f),
                    end = Offset(120f, 0f)
                )
            ) {
                moveTo(120f, 120f)
                moveToRelative(-120f, 0f)
                arcToRelative(120f, 120f, 0f, isMoreThanHalf = true, isPositiveArc = true, 240f, 0f)
                arcToRelative(120f, 120f, 0f, isMoreThanHalf = true, isPositiveArc = true, -240f, 0f)
            }
            path(fill = SolidColor(Color(0xFFC8DAEA))) {
                moveTo(81.23f, 128.77f)
                lineToRelative(14.24f, 39.41f)
                reflectiveCurveToRelative(1.78f, 3.69f, 3.69f, 3.69f)
                reflectiveCurveToRelative(30.25f, -29.49f, 30.25f, -29.49f)
                lineToRelative(31.52f, -60.89f)
                lineTo(81.74f, 118.6f)
                close()
            }
            path(fill = SolidColor(Color(0xFFA9C6D8))) {
                moveTo(100.11f, 138.88f)
                lineToRelative(-2.73f, 29.05f)
                reflectiveCurveToRelative(-1.14f, 8.9f, 7.75f, 0f)
                reflectiveCurveToRelative(17.42f, -15.76f, 17.42f, -15.76f)
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(81.49f, 130.18f)
                lineTo(52.2f, 120.64f)
                reflectiveCurveToRelative(-3.5f, -1.42f, -2.37f, -4.64f)
                curveToRelative(0.23f, -0.66f, 0.7f, -1.23f, 2.1f, -2.2f)
                curveToRelative(6.49f, -4.52f, 120.11f, -45.36f, 120.11f, -45.36f)
                reflectiveCurveToRelative(3.21f, -1.08f, 5.1f, -0.36f)
                arcToRelative(2.77f, 2.77f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.88f, 2.06f)
                arcToRelative(9.36f, 9.36f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.25f, 2.59f)
                curveToRelative(-0.01f, 0.75f, -0.1f, 1.45f, -0.17f, 2.54f)
                curveToRelative(-0.69f, 11.16f, -21.4f, 94.49f, -21.4f, 94.49f)
                reflectiveCurveToRelative(-1.24f, 4.88f, -5.68f, 5.04f)
                arcTo(8.13f, 8.13f, 0f, isMoreThanHalf = false, isPositiveArc = true, 146.1f, 172.5f)
                curveToRelative(-8.71f, -7.49f, -38.82f, -27.73f, -45.47f, -32.18f)
                arcToRelative(1.27f, 1.27f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.55f, -0.9f)
                curveToRelative(-0.09f, -0.47f, 0.42f, -1.05f, 0.42f, -1.05f)
                reflectiveCurveToRelative(52.43f, -46.6f, 53.82f, -51.49f)
                curveToRelative(0.11f, -0.38f, -0.3f, -0.57f, -0.85f, -0.4f)
                curveToRelative(-3.48f, 1.28f, -63.84f, 39.4f, -70.51f, 43.61f)
                arcTo(3.21f, 3.21f, 0f, isMoreThanHalf = false, isPositiveArc = true, 81.49f, 130.18f)
                close()
            }
        }.build()

        return _Telegram!!
    }

@Suppress("ObjectPropertyName")
private var _Telegram: ImageVector? = null
