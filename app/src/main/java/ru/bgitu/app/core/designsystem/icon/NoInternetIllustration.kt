package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.NoInternetIllustration: ImageVector
    get() {
        if (_NoInternet != null) {
            return _NoInternet!!
        }
        _NoInternet = ImageVector.Builder(
            name = "NoInternet",
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
                    moveTo(22.76f, 14.91f)
                    curveTo(21.809f, 13.825f, 20.489f, 13.131f, 19.055f, 12.963f)
                    curveTo(17.622f, 12.795f, 16.177f, 13.164f, 15f, 14f)
                    curveTo(14.94f, 14.053f, 14.902f, 14.127f, 14.892f, 14.207f)
                    curveTo(14.882f, 14.286f, 14.902f, 14.366f, 14.948f, 14.433f)
                    curveTo(14.993f, 14.499f, 15.061f, 14.546f, 15.139f, 14.566f)
                    curveTo(15.216f, 14.586f, 15.299f, 14.576f, 15.37f, 14.54f)
                    curveTo(17.29f, 13.28f, 20.37f, 13.31f, 22.01f, 15.45f)
                    curveTo(22.459f, 16.077f, 22.747f, 16.806f, 22.848f, 17.57f)
                    curveTo(22.95f, 18.335f, 22.862f, 19.113f, 22.592f, 19.836f)
                    curveTo(22.323f, 20.559f, 21.88f, 21.205f, 21.302f, 21.716f)
                    curveTo(20.725f, 22.228f, 20.03f, 22.59f, 19.28f, 22.77f)
                    curveTo(18.642f, 22.924f, 17.977f, 22.935f, 17.334f, 22.802f)
                    curveTo(16.691f, 22.669f, 16.085f, 22.395f, 15.56f, 22f)
                    curveTo(15.96f, 21.42f, 17.28f, 20.21f, 18.41f, 18.71f)
                    curveTo(19.22f, 17.65f, 19.94f, 16.51f, 20.82f, 15.55f)
                    curveTo(20.88f, 15.489f, 20.913f, 15.407f, 20.912f, 15.321f)
                    curveTo(20.911f, 15.236f, 20.876f, 15.155f, 20.815f, 15.095f)
                    curveTo(20.754f, 15.035f, 20.672f, 15.002f, 20.587f, 15.003f)
                    curveTo(20.501f, 15.004f, 20.42f, 15.039f, 20.36f, 15.1f)
                    curveTo(18.292f, 17.023f, 16.49f, 19.212f, 15f, 21.61f)
                    curveTo(13.59f, 20.27f, 13.05f, 17.4f, 14.12f, 16.08f)
                    curveTo(14.169f, 16.023f, 14.193f, 15.949f, 14.188f, 15.874f)
                    curveTo(14.182f, 15.799f, 14.147f, 15.729f, 14.09f, 15.68f)
                    curveTo(14.033f, 15.631f, 13.959f, 15.606f, 13.884f, 15.612f)
                    curveTo(13.809f, 15.618f, 13.739f, 15.653f, 13.69f, 15.71f)
                    curveTo(12.61f, 16.93f, 12.78f, 19.18f, 13.34f, 20.6f)
                    curveTo(13.828f, 21.774f, 14.711f, 22.74f, 15.835f, 23.331f)
                    curveTo(16.96f, 23.923f, 18.257f, 24.103f, 19.5f, 23.84f)
                    curveTo(20.426f, 23.637f, 21.286f, 23.204f, 22f, 22.58f)
                    curveTo(22.714f, 21.956f, 23.259f, 21.162f, 23.584f, 20.271f)
                    curveTo(23.91f, 19.38f, 24.004f, 18.422f, 23.86f, 17.485f)
                    curveTo(23.716f, 16.548f, 23.338f, 15.662f, 22.76f, 14.91f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(11.45f, 20.83f)
                    curveTo(10.351f, 20.823f, 9.255f, 20.702f, 8.18f, 20.47f)
                    curveTo(7.673f, 20.385f, 7.181f, 20.226f, 6.72f, 20f)
                    lineTo(6.49f, 19.85f)
                    curveTo(-2.51f, 15.7f, 1.42f, 1.95f, 9.49f, 0.85f)
                    curveTo(10.304f, 0.608f, 11.16f, 0.547f, 12f, 0.67f)
                    curveTo(12.045f, 0.633f, 12.079f, 0.584f, 12.097f, 0.528f)
                    curveTo(12.114f, 0.472f, 12.115f, 0.412f, 12.099f, 0.356f)
                    curveTo(12.083f, 0.299f, 12.051f, 0.249f, 12.007f, 0.211f)
                    curveTo(11.963f, 0.172f, 11.908f, 0.148f, 11.85f, 0.14f)
                    curveTo(6.4f, -0.64f, 2.59f, 2.78f, 0.66f, 7.93f)
                    curveTo(-0.227f, 10.311f, -0.167f, 12.941f, 0.826f, 15.279f)
                    curveTo(1.819f, 17.617f, 3.671f, 19.486f, 6f, 20.5f)
                    curveTo(7.719f, 21.361f, 9.649f, 21.708f, 11.56f, 21.5f)
                    curveTo(11.604f, 21.493f, 11.646f, 21.477f, 11.684f, 21.454f)
                    curveTo(11.722f, 21.43f, 11.755f, 21.399f, 11.781f, 21.363f)
                    curveTo(11.807f, 21.327f, 11.825f, 21.286f, 11.835f, 21.242f)
                    curveTo(11.846f, 21.199f, 11.847f, 21.154f, 11.84f, 21.11f)
                    curveTo(11.833f, 21.066f, 11.817f, 21.024f, 11.793f, 20.986f)
                    curveTo(11.77f, 20.948f, 11.739f, 20.915f, 11.703f, 20.889f)
                    curveTo(11.667f, 20.863f, 11.626f, 20.845f, 11.582f, 20.834f)
                    curveTo(11.539f, 20.824f, 11.494f, 20.823f, 11.45f, 20.83f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(11.53f, 16.35f)
                    curveTo(10.058f, 16.394f, 8.586f, 16.276f, 7.14f, 16f)
                    curveTo(6.835f, 14.558f, 6.717f, 13.082f, 6.79f, 11.61f)
                    curveTo(10.964f, 11.959f, 15.163f, 11.875f, 19.32f, 11.36f)
                    curveTo(20.32f, 11.2f, 20.17f, 10.55f, 19.26f, 10.61f)
                    curveTo(18.12f, 10.71f, 16.65f, 10.75f, 15.02f, 10.75f)
                    curveTo(15.057f, 9.134f, 14.844f, 7.521f, 14.39f, 5.97f)
                    horizontalLineTo(17.87f)
                    curveTo(17.949f, 5.956f, 18.021f, 5.915f, 18.072f, 5.854f)
                    curveTo(18.124f, 5.793f, 18.152f, 5.715f, 18.152f, 5.635f)
                    curveTo(18.152f, 5.555f, 18.124f, 5.477f, 18.072f, 5.416f)
                    curveTo(18.021f, 5.355f, 17.949f, 5.314f, 17.87f, 5.3f)
                    curveTo(16.87f, 5.3f, 15.53f, 5.13f, 14.06f, 5f)
                    curveTo(13.81f, 4.3f, 13.54f, 3.6f, 13.27f, 2.9f)
                    curveTo(13.242f, 2.841f, 13.195f, 2.792f, 13.137f, 2.762f)
                    curveTo(13.078f, 2.731f, 13.012f, 2.721f, 12.947f, 2.733f)
                    curveTo(12.882f, 2.744f, 12.823f, 2.776f, 12.778f, 2.825f)
                    curveTo(12.734f, 2.873f, 12.706f, 2.935f, 12.7f, 3f)
                    curveTo(12.82f, 3.61f, 12.93f, 4.23f, 13.05f, 4.84f)
                    curveTo(11.25f, 4.7f, 9.6f, 4.61f, 7.89f, 4.64f)
                    curveTo(8.291f, 3.474f, 8.999f, 2.437f, 9.94f, 1.64f)
                    curveTo(9.94f, 1.32f, 9.72f, 1.2f, 8.43f, 2.12f)
                    curveTo(7.765f, 2.884f, 7.243f, 3.761f, 6.89f, 4.71f)
                    curveTo(6.1f, 4.71f, 5.36f, 4.81f, 4.68f, 4.9f)
                    curveTo(3.42f, 5.29f, 3.41f, 5.54f, 4.06f, 5.74f)
                    curveTo(4.886f, 5.68f, 5.714f, 5.68f, 6.54f, 5.74f)
                    curveTo(6.093f, 7.305f, 5.818f, 8.915f, 5.72f, 10.54f)
                    curveTo(4.53f, 10.54f, 3.45f, 10.45f, 2.53f, 10.38f)
                    curveTo(1.53f, 10.74f, 1.53f, 10.99f, 2.53f, 11.11f)
                    curveTo(3.53f, 11.23f, 4.53f, 11.39f, 5.69f, 11.51f)
                    curveTo(5.638f, 12.97f, 5.786f, 14.43f, 6.13f, 15.85f)
                    lineTo(3.37f, 15.4f)
                    curveTo(2.61f, 15.5f, 2.27f, 16.06f, 4.17f, 16.49f)
                    curveTo(4.52f, 16.59f, 4.85f, 16.69f, 5.17f, 16.77f)
                    curveTo(6.98f, 17.19f, 6.04f, 16.53f, 6.99f, 18.34f)
                    curveTo(7.167f, 18.686f, 7.368f, 19.02f, 7.59f, 19.34f)
                    curveTo(8.05f, 20.34f, 8.43f, 20.28f, 8.79f, 19.66f)
                    curveTo(8.183f, 18.915f, 7.718f, 18.064f, 7.42f, 17.15f)
                    curveTo(8.813f, 17.311f, 10.222f, 17.26f, 11.6f, 17f)
                    curveTo(11.652f, 17.009f, 11.705f, 17.006f, 11.755f, 16.991f)
                    curveTo(11.806f, 16.976f, 11.852f, 16.949f, 11.891f, 16.913f)
                    curveTo(11.929f, 16.877f, 11.958f, 16.833f, 11.977f, 16.783f)
                    curveTo(11.995f, 16.734f, 12.002f, 16.681f, 11.996f, 16.629f)
                    curveTo(11.99f, 16.576f, 11.973f, 16.526f, 11.944f, 16.482f)
                    curveTo(11.916f, 16.437f, 11.878f, 16.4f, 11.832f, 16.373f)
                    curveTo(11.787f, 16.346f, 11.736f, 16.33f, 11.684f, 16.326f)
                    curveTo(11.632f, 16.322f, 11.579f, 16.33f, 11.53f, 16.35f)
                    close()
                    moveTo(7.53f, 5.74f)
                    curveTo(8.85f, 5.74f, 12.53f, 5.9f, 13.22f, 5.91f)
                    curveTo(13.51f, 7.4f, 13.95f, 8.26f, 14.29f, 10.71f)
                    curveTo(12.64f, 10.71f, 7.56f, 10.55f, 6.81f, 10.53f)
                    curveTo(6.916f, 8.914f, 7.167f, 7.311f, 7.56f, 5.74f)
                    horizontalLineTo(7.53f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(18.13f, 2.81f)
                    curveTo(17.106f, 2.052f, 16.018f, 1.383f, 14.88f, 0.81f)
                    curveTo(14.802f, 0.781f, 14.716f, 0.781f, 14.638f, 0.811f)
                    curveTo(14.561f, 0.841f, 14.496f, 0.898f, 14.458f, 0.972f)
                    curveTo(14.42f, 1.046f, 14.41f, 1.132f, 14.431f, 1.212f)
                    curveTo(14.451f, 1.293f, 14.501f, 1.363f, 14.57f, 1.41f)
                    curveTo(14.97f, 1.63f, 15.71f, 2.14f, 16.37f, 2.62f)
                    curveTo(17.674f, 3.517f, 18.659f, 4.806f, 19.18f, 6.3f)
                    curveTo(20.001f, 7.954f, 20.399f, 9.785f, 20.34f, 11.63f)
                    curveTo(20.344f, 11.702f, 20.373f, 11.769f, 20.422f, 11.821f)
                    curveTo(20.472f, 11.873f, 20.538f, 11.906f, 20.609f, 11.913f)
                    curveTo(20.681f, 11.92f, 20.752f, 11.902f, 20.811f, 11.861f)
                    curveTo(20.87f, 11.82f, 20.912f, 11.759f, 20.93f, 11.69f)
                    curveTo(21.195f, 10.094f, 21.08f, 8.458f, 20.593f, 6.915f)
                    curveTo(20.107f, 5.372f, 19.263f, 3.965f, 18.13f, 2.81f)
                    close()
                }
            }
        }.build()

        return _NoInternet!!
    }

@Suppress("ObjectPropertyName")
private var _NoInternet: ImageVector? = null
