package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Sync: ImageVector
    get() {
        if (_Sync != null) {
            return _Sync!!
        }
        _Sync = ImageVector.Builder(
            name = "Sync",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(16.052f, 5.029f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.189f, 1.401f)
                arcToRelative(7.002f, 7.002f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.157f, 12.487f)
                lineToRelative(0.709f, -0.71f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.414f, -1.414f)
                lineToRelative(-2.5f, 2.5f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.414f)
                lineToRelative(2.5f, 2.5f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.414f, -1.414f)
                lineToRelative(-0.843f, -0.842f)
                arcTo(9.001f, 9.001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 17.453f, 4.84f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.401f, 0.189f)
                close()
                moveTo(14.122f, 3.293f)
                lineTo(11.622f, 0.793f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.498f, 1.32f)
                lineToRelative(0.083f, 0.094f)
                lineToRelative(0.843f, 0.843f)
                arcToRelative(9.001f, 9.001f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4.778f, 15.892f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.545f, 17.4f)
                arcToRelative(7.002f, 7.002f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.37f, -12.316f)
                lineToRelative(-0.708f, 0.709f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.32f, 1.497f)
                lineToRelative(0.094f, -0.083f)
                lineToRelative(2.5f, -2.5f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.083f, -1.32f)
                lineToRelative(-0.083f, -0.094f)
                close()
            }
        }.build()

        return _Sync!!
    }

@Suppress("ObjectPropertyName")
private var _Sync: ImageVector? = null
