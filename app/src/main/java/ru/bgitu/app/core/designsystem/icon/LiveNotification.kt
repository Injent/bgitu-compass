package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.LiveNotification: ImageVector
    get() {
        if (_LiveNotification != null) {
            return _LiveNotification!!
        }
        _LiveNotification = ImageVector.Builder(
            name = "LiveNotification",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(15.25f, 5f)
                arcToRelative(3.75f, 3.75f, 0f, isMoreThanHalf = true, isPositiveArc = true, 7.5f, 0f)
                arcToRelative(3.75f, 3.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -7.5f, 0f)
            }
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(14.527f, 2.25f)
                arcToRelative(5.25f, 5.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.223f, 7.223f)
                verticalLineTo(15f)
                arcTo(6.75f, 6.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15f, 21.75f)
                horizontalLineTo(9f)
                arcTo(6.75f, 6.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.25f, 15f)
                verticalLineTo(9f)
                arcTo(6.75f, 6.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 2.25f)
                close()
            }
        }.build()

        return _LiveNotification!!
    }

@Suppress("ObjectPropertyName")
private var _LiveNotification: ImageVector? = null
