package ru.bgitu.app.core.designsystem.icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.TimeOrder: ImageVector
    get() {
        if (_TimeOrder != null) {
            return _TimeOrder!!
        }
        _TimeOrder = ImageVector.Builder(
            name = "TimeOrder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(3f, 1.5f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 0f)
                verticalLineToRelative(11.8f)
                lineTo(0.85f, 12.15f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.707f, 0.707f)
                lineToRelative(2f, 2f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.707f, 0f)
                lineToRelative(2f, -2f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.707f, -0.707f)
                lineToRelative(-1.15f, 1.15f)
                lineTo(2.993f, 1.5f)
                close()
                moveTo(11f, 4.5f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 0f)
                verticalLineToRelative(3f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.276f, 0.447f)
                lineToRelative(2f, 1f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0.447f, -0.895f)
                lineToRelative(-1.72f, -0.862f)
                lineTo(11.003f, 4.5f)
                close()
            }
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10.5f, 2f)
                curveTo(7.46f, 2f, 5f, 4.46f, 5f, 7.5f)
                reflectiveCurveTo(7.46f, 13f, 10.5f, 13f)
                reflectiveCurveTo(16f, 10.54f, 16f, 7.5f)
                reflectiveCurveTo(13.54f, 2f, 10.5f, 2f)
                moveTo(6f, 7.5f)
                curveTo(6f, 5.01f, 8.01f, 3f, 10.5f, 3f)
                reflectiveCurveTo(15f, 5.01f, 15f, 7.5f)
                reflectiveCurveTo(12.99f, 12f, 10.5f, 12f)
                reflectiveCurveTo(6f, 9.99f, 6f, 7.5f)
            }
        }.build()

        return _TimeOrder!!
    }

@Suppress("ObjectPropertyName")
private var _TimeOrder: ImageVector? = null
