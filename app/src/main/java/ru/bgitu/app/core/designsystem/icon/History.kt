package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.History: ImageVector
    get() {
        if (_History != null) {
            return _History!!
        }
        _History = ImageVector.Builder(
            name = "History",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 21f)
                quadToRelative(-3.15f, 0f, -5.575f, -1.912f)
                reflectiveQuadTo(3.275f, 14.2f)
                quadToRelative(-0.1f, -0.375f, 0.15f, -0.687f)
                reflectiveQuadToRelative(0.675f, -0.363f)
                quadToRelative(0.4f, -0.05f, 0.725f, 0.15f)
                reflectiveQuadToRelative(0.45f, 0.6f)
                quadToRelative(0.6f, 2.25f, 2.475f, 3.675f)
                reflectiveQuadTo(12f, 19f)
                quadToRelative(2.925f, 0f, 4.963f, -2.037f)
                reflectiveQuadTo(19f, 12f)
                reflectiveQuadToRelative(-2.037f, -4.962f)
                reflectiveQuadTo(12f, 5f)
                quadToRelative(-1.725f, 0f, -3.225f, 0.8f)
                reflectiveQuadTo(6.25f, 8f)
                horizontalLineTo(8f)
                quadToRelative(0.425f, 0f, 0.713f, 0.288f)
                reflectiveQuadTo(9f, 9f)
                reflectiveQuadToRelative(-0.288f, 0.713f)
                reflectiveQuadTo(8f, 10f)
                horizontalLineTo(4f)
                quadToRelative(-0.425f, 0f, -0.712f, -0.288f)
                reflectiveQuadTo(3f, 9f)
                verticalLineTo(5f)
                quadToRelative(0f, -0.425f, 0.288f, -0.712f)
                reflectiveQuadTo(4f, 4f)
                reflectiveQuadToRelative(0.713f, 0.288f)
                reflectiveQuadTo(5f, 5f)
                verticalLineToRelative(1.35f)
                quadToRelative(1.275f, -1.6f, 3.113f, -2.475f)
                reflectiveQuadTo(12f, 3f)
                quadToRelative(1.875f, 0f, 3.513f, 0.713f)
                reflectiveQuadToRelative(2.85f, 1.924f)
                reflectiveQuadToRelative(1.925f, 2.85f)
                reflectiveQuadTo(21f, 12f)
                reflectiveQuadToRelative(-0.712f, 3.513f)
                reflectiveQuadToRelative(-1.925f, 2.85f)
                reflectiveQuadToRelative(-2.85f, 1.925f)
                reflectiveQuadTo(12f, 21f)
                moveToRelative(1f, -9.4f)
                lineToRelative(2.5f, 2.5f)
                quadToRelative(0.275f, 0.275f, 0.275f, 0.7f)
                reflectiveQuadToRelative(-0.275f, 0.7f)
                reflectiveQuadToRelative(-0.7f, 0.275f)
                reflectiveQuadToRelative(-0.7f, -0.275f)
                lineToRelative(-2.8f, -2.8f)
                quadToRelative(-0.15f, -0.15f, -0.225f, -0.337f)
                reflectiveQuadTo(11f, 11.975f)
                verticalLineTo(8f)
                quadToRelative(0f, -0.425f, 0.288f, -0.712f)
                reflectiveQuadTo(12f, 7f)
                reflectiveQuadToRelative(0.713f, 0.288f)
                reflectiveQuadTo(13f, 8f)
                close()
            }
        }.build()

        return _History!!
    }

@Suppress("ObjectPropertyName")
private var _History: ImageVector? = null
