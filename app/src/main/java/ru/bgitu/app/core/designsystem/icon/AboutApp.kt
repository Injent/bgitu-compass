package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.AboutApp: ImageVector
    get() {
        if (_AboutApp != null) {
            return _AboutApp!!
        }
        _AboutApp = ImageVector.Builder(
            name = "AboutAppKey",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(15.75f, 2f)
                arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18f, 4.25f)
                verticalLineToRelative(15.5f)
                arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15.75f, 22f)
                horizontalLineToRelative(-7.5f)
                arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 19.75f)
                lineTo(6f, 4.25f)
                arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.25f, 2f)
                horizontalLineToRelative(7.5f)
                close()
                moveTo(13.25f, 18f)
                horizontalLineToRelative(-2.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.5f)
                horizontalLineToRelative(2.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1.5f)
                close()
            }
        }.build()

        return _AboutApp!!
    }

@Suppress("ObjectPropertyName")
private var _AboutApp: ImageVector? = null
