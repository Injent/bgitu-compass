package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Settings: ImageVector
    get() {
        if (_Settings != null) {
            return _Settings!!
        }
        _Settings = ImageVector.Builder(
            name = "SettingsKey",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(22.994f, 11.954f)
                curveToRelative(0.006f, -1.206f, -0.452f, -2.662f, -2.571f, -3.05f)
                curveToRelative(-0.11f, -0.21f, -0.231f, -0.421f, -0.364f, -0.633f)
                curveToRelative(1.068f, -1.479f, 0.986f, -2.851f, -0.248f, -4.085f)
                curveToRelative(-1.235f, -1.236f, -2.607f, -1.317f, -4.087f, -0.245f)
                curveToRelative(-0.209f, -0.132f, -0.418f, -0.252f, -0.627f, -0.36f)
                curveToRelative(-0.382f, -2.112f, -1.824f, -2.58f, -3.027f, -2.58f)
                curveToRelative(-1.687f, 0f, -2.771f, 0.891f, -3.086f, 2.624f)
                curveToRelative(-0.208f, 0.109f, -0.418f, 0.23f, -0.629f, 0.362f)
                curveToRelative(-1.473f, -1.064f, -2.846f, -0.973f, -4.093f, 0.272f)
                curveToRelative(-1.243f, 1.244f, -1.333f, 2.618f, -0.272f, 4.091f)
                curveToRelative(-0.133f, 0.212f, -0.255f, 0.423f, -0.366f, 0.633f)
                curveToRelative(-1.729f, 0.316f, -2.609f, 1.345f, -2.618f, 3.063f)
                curveToRelative(-0.006f, 1.206f, 0.452f, 2.662f, 2.571f, 3.05f)
                curveToRelative(0.11f, 0.21f, 0.231f, 0.421f, 0.365f, 0.633f)
                curveToRelative(-1.069f, 1.479f, -0.987f, 2.851f, 0.247f, 4.085f)
                curveToRelative(1.235f, 1.236f, 2.608f, 1.317f, 4.087f, 0.245f)
                curveToRelative(0.209f, 0.132f, 0.418f, 0.252f, 0.627f, 0.36f)
                curveToRelative(0.382f, 2.112f, 1.824f, 2.58f, 3.027f, 2.58f)
                curveToRelative(1.785f, 0f, 2.771f, -0.891f, 3.086f, -2.624f)
                curveToRelative(0.208f, -0.109f, 0.418f, -0.23f, 0.629f, -0.362f)
                curveToRelative(2.351f, 1.769f, 4.092f, -0.272f, 4.093f, -0.272f)
                curveToRelative(1.214f, -1.149f, 1.334f, -2.618f, 0.272f, -4.092f)
                curveToRelative(0.133f, -0.212f, 0.255f, -0.423f, 0.366f, -0.632f)
                curveToRelative(1.729f, -0.316f, 2.609f, -1.345f, 2.618f, -3.063f)
                close()
                moveTo(14.462f, 14.462f)
                curveToRelative(-0.817f, 0.818f, -1.593f, 1.274f, -2.67f, 1.21f)
                curveToRelative(-1.016f, -0.061f, -1.773f, -0.728f, -2.254f, -1.21f)
                curveToRelative(-1.634f, -1.633f, -1.634f, -3.291f, 0f, -4.925f)
                curveToRelative(0.882f, -0.884f, 1.754f, -1.286f, 2.67f, -1.209f)
                curveToRelative(1.015f, 0.078f, 1.773f, 0.728f, 2.254f, 1.21f)
                curveToRelative(1.633f, 1.633f, 1.634f, 3.291f, 0f, 4.925f)
                close()
            }
        }.build()

        return _Settings!!
    }

@Suppress("ObjectPropertyName")
private var _Settings: ImageVector? = null
