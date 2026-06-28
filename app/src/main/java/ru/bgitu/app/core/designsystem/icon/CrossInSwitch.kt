package ru.bgitu.app.core.designsystem.icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.CrossInSwitch: ImageVector
    get() {
        if (_SwitchClose != null) {
            return _SwitchClose!!
        }
        _SwitchClose = ImageVector.Builder(
            name = "SwitchClose",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveToRelative(12f, 14.122f)
                lineToRelative(5.303f, 5.303f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.122f, -2.122f)
                lineTo(14.12f, 12f)
                lineToRelative(5.304f, -5.303f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, -2.122f, -2.121f)
                lineTo(12f, 9.879f)
                lineTo(6.697f, 4.576f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, -2.122f, 2.12f)
                lineTo(9.88f, 12f)
                lineToRelative(-5.304f, 5.304f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 2.122f, 2.12f)
                close()
            }
        }.build()

        return _SwitchClose!!
    }

@Suppress("ObjectPropertyName")
private var _SwitchClose: ImageVector? = null
