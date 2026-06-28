package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Hd: ImageVector
    get() {
        if (_Hd != null) {
            return _Hd!!
        }
        _Hd = ImageVector.Builder(
            name = "HD",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(14.5f, 14.5f)
                verticalLineToRelative(-5f)
                horizontalLineToRelative(0.25f)
                curveToRelative(0.966f, 0f, 1.75f, 0.784f, 1.75f, 1.75f)
                verticalLineToRelative(1.5f)
                arcToRelative(1.75f, 1.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.75f, 1.75f)
                horizontalLineToRelative(-0.25f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(6.25f, 3f)
                arcTo(3.25f, 3.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, 6.25f)
                verticalLineToRelative(11.5f)
                arcTo(3.25f, 3.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.25f, 21f)
                horizontalLineToRelative(11.5f)
                arcTo(3.25f, 3.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 21f, 17.75f)
                lineTo(21f, 6.25f)
                arcTo(3.25f, 3.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 17.75f, 3f)
                lineTo(6.25f, 3f)
                close()
                moveTo(7.25f, 8f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.75f, 0.75f)
                verticalLineToRelative(2.75f)
                horizontalLineToRelative(2f)
                lineTo(10f, 8.75f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.5f, 0f)
                verticalLineToRelative(6.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.5f, 0f)
                lineTo(10f, 13f)
                lineTo(8f, 13f)
                verticalLineToRelative(2.25f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.5f, 0f)
                verticalLineToRelative(-6.5f)
                arcTo(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.25f, 8f)
                close()
                moveTo(13.75f, 8f)
                horizontalLineToRelative(1f)
                arcTo(3.25f, 3.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18f, 11.25f)
                verticalLineToRelative(1.5f)
                arcTo(3.25f, 3.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14.75f, 16f)
                horizontalLineToRelative(-1f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.75f, -0.75f)
                verticalLineToRelative(-6.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.75f, -0.75f)
                close()
            }
        }.build()

        return _Hd!!
    }

@Suppress("ObjectPropertyName")
private var _Hd: ImageVector? = null
