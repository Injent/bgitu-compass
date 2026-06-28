package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Bookmark: ImageVector
    get() {
        if (_Bookmark != null) {
            return _Bookmark!!
        }
        _Bookmark = ImageVector.Builder(
            name = "Bookmark",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveToRelative(12f, 18f)
                lineToRelative(-4.2f, 1.8f)
                quadToRelative(-1f, 0.425f, -1.9f, -0.162f)
                reflectiveQuadTo(5f, 17.975f)
                verticalLineTo(5f)
                quadToRelative(0f, -0.825f, 0.588f, -1.412f)
                reflectiveQuadTo(7f, 3f)
                horizontalLineToRelative(10f)
                quadToRelative(0.825f, 0f, 1.413f, 0.588f)
                reflectiveQuadTo(19f, 5f)
                verticalLineToRelative(12.975f)
                quadToRelative(0f, 1.075f, -0.9f, 1.663f)
                reflectiveQuadToRelative(-1.9f, 0.162f)
                close()
            }
        }.build()

        return _Bookmark!!
    }

@Suppress("ObjectPropertyName")
private var _Bookmark: ImageVector? = null
