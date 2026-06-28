package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Widget: ImageVector
    get() {
        if (_Widget != null) {
            return _Widget!!
        }
        _Widget = ImageVector.Builder(
            name = "Widget",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(4.75f, 20.5f)
                horizontalLineToRelative(14.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.102f, 1.493f)
                lineTo(19.25f, 22f)
                horizontalLineTo(4.75f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.102f, -1.493f)
                lineToRelative(0.102f, -0.007f)
                horizontalLineToRelative(14.5f)
                horizontalLineToRelative(-14.5f)
                close()
                moveTo(16.25f, 3f)
                arcTo(3.75f, 3.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 6.75f)
                verticalLineToRelative(8.5f)
                arcTo(3.75f, 3.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16.25f, 19f)
                horizontalLineToRelative(-8.5f)
                arcTo(3.75f, 3.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 15.25f)
                verticalLineToRelative(-8.5f)
                arcTo(3.75f, 3.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.75f, 3f)
                horizontalLineToRelative(8.5f)
                close()
            }
        }.build()

        return _Widget!!
    }

@Suppress("ObjectPropertyName")
private var _Widget: ImageVector? = null
