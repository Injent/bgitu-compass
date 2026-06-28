package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Help: ImageVector
    get() {
        if (_Help != null) {
            return _Help!!
        }
        _Help = ImageVector.Builder(
            name = "Help",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(12f, 2f)
                curveToRelative(5.523f, 0f, 10f, 4.477f, 10f, 10f)
                reflectiveCurveToRelative(-4.477f, 10f, -10f, 10f)
                arcToRelative(9.96f, 9.96f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4.644f, -1.142f)
                lineToRelative(-4.29f, 1.117f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.037f, -1.036f)
                lineToRelative(1.116f, -4.289f)
                arcTo(9.959f, 9.959f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 12f)
                curveTo(2f, 6.477f, 6.477f, 2f, 12f, 2f)
                close()
                moveTo(12f, 15.5f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, 2f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2f)
                close()
                moveTo(12f, 6.75f)
                arcTo(2.75f, 2.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.25f, 9.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.493f, 0.102f)
                lineToRelative(0.014f, -0.23f)
                arcToRelative(1.25f, 1.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.493f, 0.128f)
                curveToRelative(0f, 0.539f, -0.135f, 0.805f, -0.645f, 1.332f)
                lineToRelative(-0.304f, 0.31f)
                curveToRelative(-0.754f, 0.784f, -1.051f, 1.347f, -1.051f, 2.358f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.5f, 0f)
                curveToRelative(0f, -0.539f, 0.135f, -0.805f, 0.645f, -1.332f)
                lineToRelative(0.304f, -0.31f)
                curveToRelative(0.754f, -0.784f, 1.051f, -1.347f, 1.051f, -2.358f)
                arcTo(2.75f, 2.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 6.75f)
                close()
            }
        }.build()

        return _Help!!
    }

@Suppress("ObjectPropertyName")
private var _Help: ImageVector? = null
