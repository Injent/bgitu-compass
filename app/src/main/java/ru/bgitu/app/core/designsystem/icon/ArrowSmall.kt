package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.ArrowSmall: ImageVector
    get() {
        if (_Arrow != null) {
            return _Arrow!!
        }
        _Arrow = ImageVector.Builder(
            name = "Arrow",
            defaultWidth = 12.dp,
            defaultHeight = 24.dp,
            viewportWidth = 12f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveToRelative(2.452f, 6.58f)
                lineToRelative(1.061f, -1.06f)
                lineTo(9.292f, 11.297f)
                arcToRelative(0.996f, 0.996f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0f, 1.413f)
                lineTo(3.513f, 18.49f)
                lineToRelative(-1.06f, -1.06f)
                lineToRelative(5.424f, -5.425f)
                close()
            }
        }.build()

        return _Arrow!!
    }

@Suppress("ObjectPropertyName")
private var _Arrow: ImageVector? = null
