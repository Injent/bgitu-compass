package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Teacher: ImageVector
    get() {
        if (_Teacher != null) {
            return _Teacher!!
        }
        _Teacher = ImageVector.Builder(
            name = "Teacher",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(4.249f, 13.995f)
                horizontalLineToRelative(5.876f)
                curveToRelative(-0.349f, 0.423f, -0.574f, 0.952f, -0.62f, 1.53f)
                lineToRelative(-0.009f, 0.22f)
                verticalLineToRelative(4.5f)
                curveToRelative(0f, 0.665f, 0.236f, 1.275f, 0.63f, 1.75f)
                lineToRelative(-0.13f, 0.001f)
                curveToRelative(-3.42f, 0f, -5.943f, -1.072f, -7.486f, -3.236f)
                arcTo(2.75f, 2.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 17.164f)
                verticalLineToRelative(-0.92f)
                arcToRelative(2.249f, 2.249f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.249f, -2.249f)
                close()
                moveTo(14.746f, 11.495f)
                horizontalLineToRelative(3f)
                curveToRelative(0.648f, 0f, 1.18f, 0.492f, 1.244f, 1.123f)
                lineToRelative(0.007f, 0.127f)
                lineToRelative(-0.001f, 1.25f)
                horizontalLineToRelative(1.25f)
                curveToRelative(0.967f, 0f, 1.75f, 0.784f, 1.75f, 1.75f)
                verticalLineToRelative(4.5f)
                arcToRelative(1.75f, 1.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.75f, 1.75f)
                horizontalLineToRelative(-8f)
                arcToRelative(1.75f, 1.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.75f, -1.75f)
                verticalLineToRelative(-4.5f)
                curveToRelative(0f, -0.966f, 0.784f, -1.75f, 1.75f, -1.75f)
                horizontalLineToRelative(1.25f)
                verticalLineToRelative(-1.25f)
                curveToRelative(0f, -0.647f, 0.492f, -1.18f, 1.123f, -1.243f)
                lineToRelative(0.127f, -0.007f)
                horizontalLineToRelative(3f)
                horizontalLineToRelative(-3f)
                close()
                moveTo(17.496f, 12.995f)
                horizontalLineToRelative(-2.5f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(-1f)
                close()
                moveTo(9.997f, 2f)
                arcToRelative(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 10f)
                arcToRelative(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -10f)
                close()
            }
        }.build()

        return _Teacher!!
    }

@Suppress("ObjectPropertyName")
private var _Teacher: ImageVector? = null
