package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Alert: ImageVector
    get() {
        if (_Alert != null) {
            return _Alert!!
        }
        _Alert = ImageVector.Builder(
            name = "Alert",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(9.042f, 19.003f)
                horizontalLineToRelative(5.916f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5.916f, 0f)
                close()
                moveTo(12f, 2.003f)
                arcToRelative(7.5f, 7.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.5f, 7.5f)
                verticalLineToRelative(4f)
                lineToRelative(1.418f, 3.16f)
                arcTo(0.95f, 0.95f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20.052f, 18f)
                horizontalLineToRelative(-16.1f)
                arcToRelative(0.95f, 0.95f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.867f, -1.338f)
                lineToRelative(1.415f, -3.16f)
                lineTo(4.5f, 9.49f)
                lineToRelative(0.005f, -0.25f)
                arcTo(7.5f, 7.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 2.004f)
                close()
            }
        }.build()

        return _Alert!!
    }

@Suppress("ObjectPropertyName")
private var _Alert: ImageVector? = null
