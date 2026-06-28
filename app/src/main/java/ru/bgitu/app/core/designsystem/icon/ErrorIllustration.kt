package ru.bgitu.app.core.designsystem.icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.ErrorIllustration: ImageVector
    get() {
        if (_ErrorIllustration != null) {
            return _ErrorIllustration!!
        }
        _ErrorIllustration = ImageVector.Builder(
            name = "ErrorIllustration",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(24f)
                    verticalLineToRelative(24f)
                    horizontalLineToRelative(-24f)
                    close()
                }
            ) {
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(17.37f, 21.29f)
                    curveTo(17.472f, 21.391f, 17.594f, 21.469f, 17.728f, 21.521f)
                    curveTo(17.862f, 21.573f, 18.005f, 21.597f, 18.148f, 21.591f)
                    curveTo(18.291f, 21.586f, 18.432f, 21.551f, 18.561f, 21.489f)
                    curveTo(18.691f, 21.426f, 18.806f, 21.338f, 18.9f, 21.23f)
                    curveTo(19.004f, 21.026f, 19.035f, 20.793f, 18.987f, 20.569f)
                    curveTo(18.94f, 20.345f, 18.817f, 20.144f, 18.64f, 20f)
                    curveTo(17.69f, 19.48f, 16.52f, 20.44f, 17.37f, 21.29f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(24f, 21.09f)
                    curveTo(23.317f, 18.763f, 22.35f, 16.53f, 21.12f, 14.44f)
                    curveTo(20.788f, 13.929f, 20.414f, 13.447f, 20f, 13f)
                    curveTo(19.84f, 12.93f, 19.36f, 12.93f, 19.55f, 13.44f)
                    curveTo(19.8f, 14.19f, 19.76f, 13.44f, 21.02f, 15.88f)
                    curveTo(21.673f, 17.263f, 22.237f, 18.686f, 22.71f, 20.14f)
                    curveTo(22.86f, 20.57f, 23.17f, 21.39f, 22.95f, 21.8f)
                    curveTo(22.5f, 22.66f, 21.23f, 22.72f, 20.25f, 22.75f)
                    curveTo(18.07f, 22.87f, 15.45f, 22.89f, 13.45f, 22f)
                    curveTo(12.35f, 21.52f, 13.16f, 20.59f, 13.64f, 19.48f)
                    curveTo(14.41f, 17.65f, 17.56f, 12.15f, 18.64f, 12.59f)
                    curveTo(18.71f, 12.606f, 18.782f, 12.596f, 18.846f, 12.563f)
                    curveTo(18.909f, 12.53f, 18.958f, 12.475f, 18.984f, 12.409f)
                    curveTo(19.011f, 12.342f, 19.013f, 12.269f, 18.99f, 12.201f)
                    curveTo(18.967f, 12.134f, 18.921f, 12.077f, 18.86f, 12.04f)
                    curveTo(17.11f, 11.27f, 13.49f, 17.47f, 12.72f, 19.04f)
                    curveTo(12.2f, 20.18f, 10.86f, 22f, 13f, 23f)
                    curveTo(15.14f, 24f, 17.83f, 24f, 20.32f, 23.84f)
                    curveTo(21.75f, 23.74f, 23.21f, 23.53f, 23.84f, 22.24f)
                    curveTo(24.011f, 21.883f, 24.067f, 21.481f, 24f, 21.09f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(18.66f, 16.53f)
                    curveTo(18.569f, 15.989f, 18.442f, 15.454f, 18.28f, 14.93f)
                    curveTo(18.2f, 14.8f, 17.87f, 14.69f, 17.73f, 15.12f)
                    curveTo(17.68f, 15.26f, 17.57f, 15.9f, 17.54f, 16.04f)
                    curveTo(17.51f, 16.279f, 17.51f, 16.521f, 17.54f, 16.76f)
                    curveTo(17.61f, 17.354f, 17.737f, 17.94f, 17.92f, 18.51f)
                    curveTo(18.57f, 18.84f, 18.75f, 17.26f, 18.66f, 16.53f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(10.08f, 18.16f)
                    curveTo(3.22f, 18f, 3.18f, 18f, 2.44f, 17.88f)
                    curveTo(2.016f, 17.835f, 1.616f, 17.664f, 1.29f, 17.39f)
                    curveTo(1.58f, 16.956f, 1.819f, 16.49f, 2f, 16f)
                    curveTo(4.36f, 9.77f, 3.76f, 6.36f, 4f, 3.14f)
                    curveTo(3.93f, 3.46f, 4f, 1f, 4f, 1f)
                    curveTo(3.988f, 0.958f, 3.967f, 0.918f, 3.939f, 0.885f)
                    curveTo(3.91f, 0.851f, 3.875f, 0.825f, 3.834f, 0.807f)
                    curveTo(3.794f, 0.789f, 3.751f, 0.779f, 3.707f, 0.78f)
                    curveTo(3.663f, 0.781f, 3.62f, 0.791f, 3.58f, 0.81f)
                    curveTo(3.27f, 0.94f, 3.36f, 1.42f, 3.35f, 1.57f)
                    curveTo(3f, 8.63f, 2.69f, 10f, 1.7f, 13.43f)
                    curveTo(1.45f, 14.35f, 1.139f, 15.252f, 0.77f, 16.13f)
                    curveTo(0.45f, 16.74f, 0.05f, 17.22f, 0.01f, 17.43f)
                    curveTo(-0.008f, 17.594f, 0.026f, 17.759f, 0.107f, 17.902f)
                    curveTo(0.188f, 18.045f, 0.311f, 18.16f, 0.46f, 18.23f)
                    curveTo(1.028f, 18.531f, 1.638f, 18.743f, 2.27f, 18.86f)
                    curveTo(3.1f, 18.99f, 3.27f, 18.95f, 10.11f, 18.86f)
                    curveTo(10.203f, 18.856f, 10.29f, 18.815f, 10.353f, 18.747f)
                    curveTo(10.416f, 18.678f, 10.449f, 18.588f, 10.445f, 18.495f)
                    curveTo(10.441f, 18.402f, 10.4f, 18.315f, 10.332f, 18.252f)
                    curveTo(10.263f, 18.189f, 10.173f, 18.156f, 10.08f, 18.16f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(5.36f, 1f)
                    lineTo(12.52f, 1.21f)
                    curveTo(12.832f, 1.693f, 12.998f, 2.255f, 13f, 2.83f)
                    curveTo(12.81f, 4.12f, 11.19f, 5.97f, 13.23f, 6.14f)
                    curveTo(14.175f, 6.181f, 15.119f, 6.024f, 16f, 5.68f)
                    lineTo(17.18f, 5.15f)
                    curveTo(17.23f, 5.137f, 17.276f, 5.113f, 17.315f, 5.079f)
                    curveTo(17.353f, 5.045f, 17.383f, 5.003f, 17.403f, 4.955f)
                    curveTo(17.422f, 4.908f, 17.43f, 4.856f, 17.426f, 4.805f)
                    curveTo(17.423f, 4.754f, 17.406f, 4.704f, 17.38f, 4.66f)
                    curveTo(17.15f, 4.28f, 16.3f, 4.66f, 15.69f, 4.91f)
                    curveTo(14.941f, 5.068f, 14.174f, 5.125f, 13.41f, 5.08f)
                    curveTo(13.8f, 3.73f, 14.24f, 2.7f, 13.34f, 1.22f)
                    horizontalLineTo(13.92f)
                    curveTo(14.83f, 1.31f, 17.92f, 4.14f, 17.92f, 4.7f)
                    curveTo(17.92f, 6.14f, 17.63f, 8.47f, 17.49f, 9.86f)
                    curveTo(17.499f, 9.915f, 17.523f, 9.967f, 17.56f, 10.008f)
                    curveTo(17.597f, 10.05f, 17.646f, 10.08f, 17.7f, 10.095f)
                    curveTo(17.754f, 10.11f, 17.811f, 10.109f, 17.864f, 10.092f)
                    curveTo(17.917f, 10.075f, 17.964f, 10.043f, 18f, 10f)
                    curveTo(18.18f, 8.81f, 18.65f, 6.4f, 18.81f, 4.82f)
                    curveTo(18.97f, 3.24f, 15.3f, 0.27f, 14f, 0.15f)
                    curveTo(12.92f, 0.08f, 13.6f, 0.15f, 5.37f, 0.3f)
                    curveTo(5.277f, 0.299f, 5.188f, 0.334f, 5.121f, 0.399f)
                    curveTo(5.054f, 0.464f, 5.016f, 0.552f, 5.015f, 0.645f)
                    curveTo(5.014f, 0.738f, 5.049f, 0.827f, 5.114f, 0.894f)
                    curveTo(5.179f, 0.961f, 5.267f, 0.999f, 5.36f, 1f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(8.5f, 8.48f)
                    curveTo(8.5f, 8.17f, 8.3f, 8.1f, 8.01f, 8.14f)
                    curveTo(7.768f, 8.232f, 7.539f, 8.356f, 7.33f, 8.51f)
                    curveTo(6.525f, 8.886f, 5.835f, 9.469f, 5.33f, 10.2f)
                    curveTo(5.01f, 10.82f, 5.73f, 11.2f, 5.88f, 11.37f)
                    curveTo(6.471f, 11.883f, 7.092f, 12.361f, 7.74f, 12.8f)
                    curveTo(7.806f, 12.847f, 7.887f, 12.869f, 7.967f, 12.862f)
                    curveTo(8.048f, 12.855f, 8.124f, 12.819f, 8.18f, 12.761f)
                    curveTo(8.237f, 12.703f, 8.271f, 12.627f, 8.277f, 12.546f)
                    curveTo(8.282f, 12.465f, 8.258f, 12.385f, 8.21f, 12.32f)
                    curveTo(7.9f, 11.96f, 7.48f, 11.57f, 7.11f, 11.16f)
                    curveTo(6.74f, 10.75f, 6.89f, 10.8f, 6.48f, 10.52f)
                    curveTo(7.65f, 9f, 8.55f, 9f, 8.5f, 8.48f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(13.64f, 9.17f)
                    curveTo(12.937f, 8.698f, 12.144f, 8.378f, 11.31f, 8.23f)
                    curveTo(11.258f, 8.258f, 11.214f, 8.3f, 11.184f, 8.351f)
                    curveTo(11.153f, 8.402f, 11.137f, 8.461f, 11.137f, 8.52f)
                    curveTo(11.137f, 8.579f, 11.153f, 8.638f, 11.184f, 8.689f)
                    curveTo(11.214f, 8.74f, 11.258f, 8.782f, 11.31f, 8.81f)
                    curveTo(11.838f, 9.085f, 12.343f, 9.403f, 12.82f, 9.76f)
                    curveTo(12.62f, 10.07f, 11.91f, 10.92f, 11.54f, 11.35f)
                    curveTo(11.17f, 11.78f, 10.54f, 12.01f, 10.86f, 12.35f)
                    curveTo(11.18f, 12.69f, 11.72f, 12.27f, 12.14f, 11.92f)
                    curveTo(13.14f, 11.15f, 14.69f, 10f, 13.64f, 9.17f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(19f, 1f)
                    curveTo(19.939f, 1.853f, 20.601f, 2.967f, 20.9f, 4.2f)
                    curveTo(20.9f, 5.67f, 19.99f, 9.63f, 19.9f, 11.06f)
                    curveTo(19.889f, 11.14f, 19.911f, 11.22f, 19.96f, 11.284f)
                    curveTo(20.008f, 11.348f, 20.08f, 11.389f, 20.16f, 11.4f)
                    curveTo(20.24f, 11.411f, 20.32f, 11.389f, 20.384f, 11.34f)
                    curveTo(20.448f, 11.292f, 20.489f, 11.22f, 20.5f, 11.14f)
                    curveTo(20.68f, 9.92f, 21.77f, 5.88f, 21.93f, 4.26f)
                    curveTo(21.889f, 3.459f, 21.659f, 2.68f, 21.259f, 1.984f)
                    curveTo(20.86f, 1.289f, 20.301f, 0.698f, 19.63f, 0.26f)
                    curveTo(18.6f, -0.26f, 18.47f, 0.72f, 19f, 1f)
                    close()
                }
            }
        }.build()

        return _ErrorIllustration!!
    }

@Suppress("ObjectPropertyName")
private var _ErrorIllustration: ImageVector? = null
