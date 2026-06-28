package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Edit: ImageVector
    get() {
        if (_Edit != null) {
            return _Edit!!
        }
        _Edit = ImageVector.Builder(
            name = "Edit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Red)) {
                moveTo(16.291f, 9.12f)
                curveToRelative(0.39f, -0.39f, 0.39f, -1.023f, 0f, -1.414f)
                curveToRelative(-0.39f, -0.39f, -1.024f, -0.39f, -1.414f, 0f)
                lineTo(12.69f, 9.894f)
                curveToRelative(-0.39f, 0.39f, -0.39f, 1.024f, 0f, 1.414f)
                curveToRelative(0.39f, 0.39f, 1.024f, 0.39f, 1.414f, 0f)
                lineToRelative(2.186f, -2.186f)
                close()
            }
            path(
                fill = SolidColor(Color.Red),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(19.848f, 4.144f)
                curveToRelative(1.53f, 1.53f, 1.524f, 4.009f, -0.006f, 5.538f)
                lineToRelative(-8.474f, 8.474f)
                lineToRelative(-0.149f, 0.148f)
                curveToRelative(-2.026f, 1.99f, -4.717f, 3.16f, -7.554f, 3.285f)
                lineToRelative(-0.21f, 0.008f)
                lineToRelative(-0.046f, 0.001f)
                curveToRelative(-0.564f, 0.009f, -1.024f, -0.451f, -1.015f, -1.015f)
                lineToRelative(0.002f, -0.045f)
                lineToRelative(0.007f, -0.211f)
                curveToRelative(0.125f, -2.837f, 1.295f, -5.528f, 3.285f, -7.554f)
                lineToRelative(0.148f, -0.149f)
                lineToRelative(8.475f, -8.474f)
                curveToRelative(1.53f, -1.53f, 4.008f, -1.535f, 5.537f, -0.006f)
                close()
                moveTo(18.434f, 5.56f)
                curveToRelative(0.745f, 0.744f, 0.746f, 1.957f, -0.006f, 2.709f)
                lineToRelative(-8.474f, 8.474f)
                lineToRelative(-0.136f, 0.135f)
                curveToRelative(-1.456f, 1.43f, -3.328f, 2.348f, -5.333f, 2.63f)
                curveToRelative(0.282f, -2.005f, 1.2f, -3.877f, 2.63f, -5.333f)
                lineToRelative(0.135f, -0.136f)
                lineToRelative(8.475f, -8.474f)
                curveToRelative(0.752f, -0.752f, 1.964f, -0.75f, 2.709f, -0.005f)
                close()
            }
        }.build()

        return _Edit!!
    }

@Suppress("ObjectPropertyName")
private var _Edit: ImageVector? = null
