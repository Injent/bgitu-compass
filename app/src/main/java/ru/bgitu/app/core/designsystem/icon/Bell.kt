package ru.bgitu.app.core.designsystem.icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Bell: ImageVector
    get() {
        if (_Bell != null) {
            return _Bell!!
        }
        _Bell = ImageVector.Builder(
            name = "Bell",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.12285f
            ) {
                moveToRelative(9.938f, 6.462f)
                arcToRelative(5.263f, 5.263f, 89.985f, isMoreThanHalf = false, isPositiveArc = true, 9.108f, 5.146f)
                lineToRelative(-0.843f, 1.976f)
                lineToRelative(-0.021f, 0.05f)
                arcToRelative(7.537f, 7.537f, 89.991f, isMoreThanHalf = false, isPositiveArc = false, -0.593f, 3.106f)
                lineToRelative(0.001f, 0.055f)
                lineToRelative(0.027f, 1.057f)
                curveToRelative(0.026f, 0.96f, 0.038f, 1.44f, -0.175f, 1.708f)
                arcToRelative(0.942f, 0.942f, 89.991f, isMoreThanHalf = false, isPositiveArc = true, -0.566f, 0.34f)
                curveTo(16.539f, 19.963f, 16.12f, 19.726f, 15.284f, 19.254f)
                lineTo(5.33f, 13.629f)
                curveToRelative(-0.837f, -0.473f, -1.255f, -0.709f, -1.375f, -1.029f)
                arcTo(0.942f, 0.942f, 89.991f, isMoreThanHalf = false, isPositiveArc = true, 3.954f, 11.939f)
                curveToRelative(0.12f, -0.32f, 0.538f, -0.557f, 1.373f, -1.032f)
                lineToRelative(0.922f, -0.522f)
                lineToRelative(0.048f, -0.027f)
                arcTo(7.537f, 7.537f, 89.991f, isMoreThanHalf = false, isPositiveArc = false, 8.65f, 8.248f)
                lineToRelative(0.031f, -0.044f)
                close()
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.12285f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveToRelative(7.026f, 14.588f)
                arcToRelative(3.769f, 3.768f, 89.991f, isMoreThanHalf = true, isPositiveArc = false, 6.562f, 3.708f)
            }
        }.build()

        return _Bell!!
    }

@Suppress("ObjectPropertyName")
private var _Bell: ImageVector? = null
