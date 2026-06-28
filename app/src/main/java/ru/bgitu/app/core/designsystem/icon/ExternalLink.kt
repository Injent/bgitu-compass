package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.ExternalLink: ImageVector
    get() {
        if (_ExternalLink != null) {
            return _ExternalLink!!
        }
        _ExternalLink = ImageVector.Builder(
            name = "ExternalLink",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19.5f, 4.5f)
                horizontalLineToRelative(-7f)
                lineTo(12.5f, 6f)
                horizontalLineToRelative(4.44f)
                lineToRelative(-5.97f, 5.97f)
                lineToRelative(1.06f, 1.06f)
                lineTo(18f, 7.06f)
                verticalLineToRelative(4.44f)
                horizontalLineToRelative(1.5f)
                close()
                moveTo(6.5f, 5.5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2f, 2f)
                verticalLineToRelative(10f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 2f)
                horizontalLineToRelative(10f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, -2f)
                verticalLineToRelative(-3f)
                lineTo(17f, 14.5f)
                verticalLineToRelative(3f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.5f, 0.5f)
                horizontalLineToRelative(-10f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.5f, -0.5f)
                verticalLineToRelative(-10f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.5f, -0.5f)
                horizontalLineToRelative(3f)
                lineTo(9.5f, 5.5f)
                close()
            }
        }.build()

        return _ExternalLink!!
    }

@Suppress("ObjectPropertyName")
private var _ExternalLink: ImageVector? = null
