package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.VK: ImageVector
    get() {
        if (_VK != null) {
            return _VK!!
        }
        _VK = ImageVector.Builder(
            name = "VK",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(fill = SolidColor(Color(0xFF0077FF))) {
                moveTo(0f, 23.04f)
                curveTo(0f, 12.179f, 0f, 6.748f, 3.374f, 3.374f)
                curveTo(6.748f, 0f, 12.179f, 0f, 23.04f, 0f)
                horizontalLineTo(24.96f)
                curveTo(35.821f, 0f, 41.252f, 0f, 44.626f, 3.374f)
                curveTo(48f, 6.748f, 48f, 12.179f, 48f, 23.04f)
                verticalLineTo(24.96f)
                curveTo(48f, 35.821f, 48f, 41.252f, 44.626f, 44.626f)
                curveTo(41.252f, 48f, 35.821f, 48f, 24.96f, 48f)
                horizontalLineTo(23.04f)
                curveTo(12.179f, 48f, 6.748f, 48f, 3.374f, 44.626f)
                curveTo(0f, 41.252f, 0f, 35.821f, 0f, 24.96f)
                verticalLineTo(23.04f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(25.54f, 34.58f)
                curveTo(14.6f, 34.58f, 8.36f, 27.08f, 8.1f, 14.6f)
                horizontalLineTo(13.58f)
                curveTo(13.76f, 23.76f, 17.8f, 27.64f, 21f, 28.44f)
                verticalLineTo(14.6f)
                horizontalLineTo(26.16f)
                verticalLineTo(22.5f)
                curveTo(29.32f, 22.16f, 32.64f, 18.56f, 33.76f, 14.6f)
                horizontalLineTo(38.92f)
                curveTo(38.06f, 19.48f, 34.46f, 23.08f, 31.9f, 24.56f)
                curveTo(34.46f, 25.76f, 38.56f, 28.9f, 40.12f, 34.58f)
                horizontalLineTo(34.44f)
                curveTo(33.22f, 30.78f, 30.18f, 27.84f, 26.16f, 27.44f)
                verticalLineTo(34.58f)
                horizontalLineTo(25.54f)
                close()
            }
        }.build()

        return _VK!!
    }

@Suppress("ObjectPropertyName")
private var _VK: ImageVector? = null
