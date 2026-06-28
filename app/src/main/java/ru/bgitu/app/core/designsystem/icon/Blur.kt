package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Blur: ImageVector
    get() {
        if (_Blur != null) {
            return _Blur!!
        }
        _Blur = ImageVector.Builder(
            name = "Blur",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(3f, 12f)
                arcToRelative(9f, 9f, 0f, isMoreThanHalf = false, isPositiveArc = true, 13.977f, -7.5f)
                horizontalLineTo(12f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(6.225f)
                arcToRelative(9.05f, 9.05f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.26f, 1.5f)
                horizontalLineTo(12f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(8.064f)
                curveToRelative(0.238f, 0.477f, 0.434f, 0.979f, 0.584f, 1.5f)
                horizontalLineTo(12f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(8.876f)
                curveToRelative(0.081f, 0.488f, 0.124f, 0.989f, 0.124f, 1.5f)
                horizontalLineToRelative(-9f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(8.945f)
                arcToRelative(8.963f, 8.963f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.297f, 1.5f)
                horizontalLineTo(12f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(8.294f)
                arcToRelative(8.98f, 8.98f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.81f, 1.5f)
                horizontalLineTo(12f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(6.708f)
                arcTo(9f, 9f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, 12f)
                close()
            }
        }.build()

        return _Blur!!
    }

@Suppress("ObjectPropertyName")
private var _Blur: ImageVector? = null
