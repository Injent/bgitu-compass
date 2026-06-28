package ru.bgitu.app.core.designsystem.icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.News: ImageVector
    get() {
        if (_News != null) {
            return _News!!
        }
        _News = ImageVector.Builder(
            name = "News",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                strokeLineWidth = 1f
            ) {
                moveToRelative(17.7f, 2f)
                arcToRelative(2.7f, 2.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.694f, 2.515f)
                lineTo(20.4f, 4.7f)
                verticalLineToRelative(13.5f)
                arcToRelative(0.6f, 0.6f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.19f, 0.108f)
                lineTo(21.6f, 18.2f)
                lineTo(21.6f, 5.617f)
                arcToRelative(2.7f, 2.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.393f, 2.486f)
                lineTo(24f, 8.3f)
                verticalLineToRelative(9f)
                arcToRelative(3.9f, 3.9f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.679f, 3.894f)
                lineTo(20.1f, 21.2f)
                lineTo(3.9f, 21.2f)
                arcTo(3.9f, 3.9f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.006f, 17.521f)
                lineTo(0f, 17.3f)
                lineTo(0f, 4.7f)
                arcTo(2.7f, 2.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.515f, 2.006f)
                lineTo(2.7f, 2f)
                close()
                moveTo(8.698f, 10.4f)
                horizontalLineToRelative(-4.2f)
                arcToRelative(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.9f, 0.9f)
                verticalLineToRelative(4.2f)
                curveToRelative(0f, 0.497f, 0.403f, 0.9f, 0.9f, 0.9f)
                horizontalLineToRelative(4.2f)
                arcToRelative(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.9f, -0.9f)
                verticalLineToRelative(-4.2f)
                arcToRelative(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.9f, -0.9f)
                close()
                moveTo(15.902f, 14.6f)
                horizontalLineToRelative(-2.998f)
                lineToRelative(-0.122f, 0.008f)
                arcTo(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.905f, 16.4f)
                horizontalLineToRelative(2.998f)
                lineToRelative(0.122f, -0.008f)
                arcTo(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.902f, 14.6f)
                close()
                moveTo(7.798f, 12.2f)
                verticalLineToRelative(2.4f)
                horizontalLineToRelative(-2.4f)
                lineTo(5.398f, 12.2f)
                close()
                moveTo(15.9f, 10.4f)
                lineTo(12.902f, 10.406f)
                lineTo(12.78f, 10.413f)
                arcToRelative(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.125f, 1.793f)
                lineTo(15.904f, 12.2f)
                lineTo(16.025f, 12.192f)
                arcTo(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.901f, 10.4f)
                close()
                moveTo(15.901f, 6.205f)
                lineTo(4.498f, 6.205f)
                lineTo(4.375f, 6.213f)
                arcTo(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.498f, 8.005f)
                lineTo(15.902f, 8.005f)
                lineTo(16.025f, 7.998f)
                arcTo(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.902f, 6.205f)
                close()
            }
        }.build()

        return _News!!
    }

@Suppress("ObjectPropertyName")
private var _News: ImageVector? = null
