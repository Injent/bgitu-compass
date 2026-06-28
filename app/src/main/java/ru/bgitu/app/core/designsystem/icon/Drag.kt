package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Drag: ImageVector
    get() {
        if (_Drag != null) {
            return _Drag!!
        }
        _Drag = ImageVector.Builder(
            name = "Drag",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(5f, 4f)
                curveToRelative(0f, -0.14f, 0f, -0.209f, 0.008f, -0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, -0.725f)
                curveTo(5.79f, 3f, 5.86f, 3f, 6f, 3f)
                reflectiveCurveToRelative(0.209f, 0f, 0.267f, 0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, 0.725f)
                curveTo(7f, 3.79f, 7f, 3.86f, 7f, 4f)
                reflectiveCurveToRelative(0f, 0.209f, -0.008f, 0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, 0.725f)
                curveTo(6.21f, 5f, 6.14f, 5f, 6f, 5f)
                reflectiveCurveToRelative(-0.209f, 0f, -0.267f, -0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, -0.725f)
                curveTo(5f, 4.21f, 5f, 4.14f, 5f, 4f)
                moveToRelative(0f, 4f)
                curveToRelative(0f, -0.14f, 0f, -0.209f, 0.008f, -0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, -0.725f)
                curveTo(5.79f, 7f, 5.86f, 7f, 6f, 7f)
                reflectiveCurveToRelative(0.209f, 0f, 0.267f, 0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, 0.725f)
                curveTo(7f, 7.79f, 7f, 7.86f, 7f, 8f)
                reflectiveCurveToRelative(0f, 0.209f, -0.008f, 0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, 0.725f)
                curveTo(6.21f, 9f, 6.14f, 9f, 6f, 9f)
                reflectiveCurveToRelative(-0.209f, 0f, -0.267f, -0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, -0.725f)
                curveTo(5f, 8.21f, 5f, 8.14f, 5f, 8f)
                moveToRelative(0f, 4f)
                curveToRelative(0f, -0.139f, 0f, -0.209f, 0.008f, -0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.724f, -0.724f)
                curveToRelative(0.059f, -0.008f, 0.128f, -0.008f, 0.267f, -0.008f)
                reflectiveCurveToRelative(0.21f, 0f, 0.267f, 0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.724f, 0.724f)
                curveToRelative(0.008f, 0.058f, 0.008f, 0.128f, 0.008f, 0.267f)
                reflectiveCurveToRelative(0f, 0.209f, -0.008f, 0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.724f, 0.724f)
                curveToRelative(-0.058f, 0.008f, -0.128f, 0.008f, -0.267f, 0.008f)
                reflectiveCurveToRelative(-0.209f, 0f, -0.267f, -0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.724f, -0.724f)
                curveTo(5f, 12.209f, 5f, 12.139f, 5f, 12f)
                moveToRelative(4f, -8f)
                curveToRelative(0f, -0.14f, 0f, -0.209f, 0.008f, -0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, -0.725f)
                curveTo(9.79f, 3f, 9.86f, 3f, 10f, 3f)
                reflectiveCurveToRelative(0.209f, 0f, 0.267f, 0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, 0.725f)
                curveTo(11f, 3.79f, 11f, 3.86f, 11f, 4f)
                reflectiveCurveToRelative(0f, 0.209f, -0.008f, 0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, 0.725f)
                curveTo(10.21f, 5f, 10.14f, 5f, 10f, 5f)
                reflectiveCurveToRelative(-0.209f, 0f, -0.267f, -0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, -0.725f)
                curveTo(9f, 4.21f, 9f, 4.14f, 9f, 4f)
                moveToRelative(0f, 4f)
                curveToRelative(0f, -0.14f, 0f, -0.209f, 0.008f, -0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, -0.725f)
                curveTo(9.79f, 7f, 9.86f, 7f, 10f, 7f)
                reflectiveCurveToRelative(0.209f, 0f, 0.267f, 0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.725f, 0.725f)
                curveTo(11f, 7.79f, 11f, 7.86f, 11f, 8f)
                reflectiveCurveToRelative(0f, 0.209f, -0.008f, 0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, 0.725f)
                curveTo(10.21f, 9f, 10.14f, 9f, 10f, 9f)
                reflectiveCurveToRelative(-0.209f, 0f, -0.267f, -0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.725f, -0.725f)
                curveTo(9f, 8.21f, 9f, 8.14f, 9f, 8f)
                moveToRelative(0f, 4f)
                curveToRelative(0f, -0.139f, 0f, -0.209f, 0.008f, -0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.724f, -0.724f)
                curveToRelative(0.059f, -0.008f, 0.128f, -0.008f, 0.267f, -0.008f)
                curveToRelative(0.14f, 0f, 0.21f, 0f, 0.267f, 0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.724f, 0.724f)
                curveToRelative(0.008f, 0.058f, 0.008f, 0.128f, 0.008f, 0.267f)
                reflectiveCurveToRelative(0f, 0.209f, -0.008f, 0.267f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.724f, 0.724f)
                curveToRelative(-0.058f, 0.008f, -0.128f, 0.008f, -0.267f, 0.008f)
                reflectiveCurveToRelative(-0.209f, 0f, -0.267f, -0.008f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.724f, -0.724f)
                curveTo(9f, 12.209f, 9f, 12.139f, 9f, 12f)
            }
        }.build()

        return _Drag!!
    }

@Suppress("ObjectPropertyName")
private var _Drag: ImageVector? = null
