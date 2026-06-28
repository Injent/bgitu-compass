package ru.bgitu.app.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.GroupIllustration: ImageVector
    get() {
        if (_GroupIllustration != null) {
            return _GroupIllustration!!
        }
        _GroupIllustration = ImageVector.Builder(
            name = "GroupIllustration",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
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
                    moveTo(7.22f, 14.31f)
                    curveTo(7.489f, 14.345f, 7.761f, 14.345f, 8.03f, 14.31f)
                    curveTo(8.292f, 14.272f, 8.55f, 14.208f, 8.8f, 14.12f)
                    curveTo(9.36f, 13.907f, 9.896f, 13.635f, 10.4f, 13.31f)
                    curveTo(10.476f, 13.271f, 10.533f, 13.205f, 10.561f, 13.125f)
                    curveTo(10.589f, 13.045f, 10.585f, 12.957f, 10.55f, 12.88f)
                    curveTo(10.51f, 12.806f, 10.443f, 12.75f, 10.364f, 12.722f)
                    curveTo(10.284f, 12.695f, 10.198f, 12.697f, 10.12f, 12.73f)
                    curveTo(9.589f, 12.917f, 9.044f, 13.06f, 8.49f, 13.16f)
                    curveTo(8.16f, 13.228f, 7.826f, 13.278f, 7.49f, 13.31f)
                    horizontalLineTo(6.99f)
                    curveTo(7.574f, 12.545f, 7.924f, 11.629f, 8f, 10.67f)
                    curveTo(8.2f, 8.67f, 7.21f, 6.67f, 4.89f, 6.47f)
                    curveTo(4.144f, 6.466f, 3.408f, 6.638f, 2.743f, 6.974f)
                    curveTo(2.077f, 7.309f, 1.5f, 7.798f, 1.06f, 8.4f)
                    curveTo(0.568f, 8.977f, 0.275f, 9.696f, 0.223f, 10.452f)
                    curveTo(0.171f, 11.208f, 0.362f, 11.961f, 0.77f, 12.6f)
                    curveTo(1.028f, 12.985f, 1.37f, 13.306f, 1.77f, 13.54f)
                    lineTo(2.15f, 13.74f)
                    curveTo(1.383f, 14.524f, 0.802f, 15.471f, 0.45f, 16.51f)
                    curveTo(0.187f, 17.316f, 0.036f, 18.153f, 0f, 19f)
                    curveTo(-0.045f, 20.235f, 0.036f, 21.471f, 0.24f, 22.69f)
                    curveTo(0.242f, 22.735f, 0.254f, 22.779f, 0.274f, 22.82f)
                    curveTo(0.295f, 22.861f, 0.323f, 22.896f, 0.358f, 22.926f)
                    curveTo(0.392f, 22.955f, 0.433f, 22.976f, 0.476f, 22.989f)
                    curveTo(0.519f, 23.002f, 0.565f, 23.006f, 0.61f, 23f)
                    curveTo(0.691f, 22.99f, 0.766f, 22.949f, 0.818f, 22.886f)
                    curveTo(0.87f, 22.823f, 0.896f, 22.742f, 0.89f, 22.66f)
                    curveTo(0.828f, 21.466f, 0.865f, 20.268f, 1f, 19.08f)
                    curveTo(1.087f, 18.319f, 1.238f, 17.566f, 1.45f, 16.83f)
                    curveTo(1.73f, 15.803f, 2.209f, 14.842f, 2.86f, 14f)
                    curveTo(3.13f, 14.07f, 3.4f, 14.14f, 3.67f, 14.19f)
                    curveTo(3.743f, 14.205f, 3.819f, 14.19f, 3.881f, 14.148f)
                    curveTo(3.942f, 14.107f, 3.985f, 14.043f, 4f, 13.97f)
                    curveTo(4.015f, 13.897f, 4f, 13.821f, 3.958f, 13.759f)
                    curveTo(3.917f, 13.698f, 3.853f, 13.655f, 3.78f, 13.64f)
                    curveTo(1.78f, 13.18f, 1.42f, 12.25f, 1.41f, 12.22f)
                    curveTo(1.126f, 11.708f, 1.014f, 11.119f, 1.092f, 10.539f)
                    curveTo(1.169f, 9.959f, 1.431f, 9.419f, 1.84f, 9f)
                    curveTo(2.189f, 8.535f, 2.642f, 8.158f, 3.163f, 7.899f)
                    curveTo(3.684f, 7.641f, 4.258f, 7.507f, 4.84f, 7.51f)
                    curveTo(6.55f, 7.61f, 7.23f, 9.1f, 7.14f, 10.58f)
                    curveTo(7.15f, 11.354f, 6.903f, 12.11f, 6.438f, 12.728f)
                    curveTo(5.973f, 13.347f, 5.316f, 13.794f, 4.57f, 14f)
                    curveTo(4.488f, 14.015f, 4.414f, 14.06f, 4.364f, 14.127f)
                    curveTo(4.314f, 14.194f, 4.291f, 14.277f, 4.3f, 14.36f)
                    curveTo(4.315f, 14.442f, 4.36f, 14.514f, 4.428f, 14.563f)
                    curveTo(4.495f, 14.611f, 4.578f, 14.632f, 4.66f, 14.62f)
                    curveTo(5.201f, 14.542f, 5.714f, 14.329f, 6.15f, 14f)
                    curveTo(6.489f, 14.157f, 6.85f, 14.262f, 7.22f, 14.31f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(10.57f, 14.7f)
                    curveTo(10.162f, 15.086f, 9.709f, 15.422f, 9.22f, 15.7f)
                    curveTo(8.737f, 15.967f, 8.227f, 16.185f, 7.7f, 16.35f)
                    curveTo(7.294f, 16.45f, 6.894f, 16.57f, 6.5f, 16.71f)
                    curveTo(6.221f, 16.813f, 5.985f, 17.007f, 5.83f, 17.26f)
                    curveTo(5.605f, 17.811f, 5.509f, 18.406f, 5.55f, 19f)
                    curveTo(5.55f, 20.25f, 5.96f, 21.79f, 6.02f, 22.58f)
                    curveTo(6.021f, 22.622f, 6.031f, 22.663f, 6.048f, 22.7f)
                    curveTo(6.066f, 22.738f, 6.091f, 22.772f, 6.121f, 22.8f)
                    curveTo(6.152f, 22.828f, 6.188f, 22.85f, 6.228f, 22.863f)
                    curveTo(6.267f, 22.877f, 6.308f, 22.883f, 6.35f, 22.88f)
                    curveTo(6.391f, 22.879f, 6.431f, 22.869f, 6.468f, 22.853f)
                    curveTo(6.505f, 22.836f, 6.538f, 22.812f, 6.566f, 22.782f)
                    curveTo(6.594f, 22.753f, 6.616f, 22.718f, 6.63f, 22.68f)
                    curveTo(6.645f, 22.641f, 6.651f, 22.601f, 6.65f, 22.56f)
                    curveTo(6.65f, 21.88f, 6.54f, 20.63f, 6.54f, 19.48f)
                    curveTo(6.54f, 16.89f, 7.15f, 17.62f, 8.07f, 17.26f)
                    curveTo(8.62f, 17.001f, 9.145f, 16.693f, 9.64f, 16.34f)
                    curveTo(10.144f, 15.982f, 10.601f, 15.562f, 11f, 15.09f)
                    curveTo(11.028f, 15.065f, 11.05f, 15.034f, 11.066f, 14.999f)
                    curveTo(11.081f, 14.965f, 11.089f, 14.928f, 11.089f, 14.89f)
                    curveTo(11.089f, 14.852f, 11.081f, 14.815f, 11.066f, 14.781f)
                    curveTo(11.05f, 14.746f, 11.028f, 14.715f, 11f, 14.69f)
                    curveTo(10.973f, 14.659f, 10.939f, 14.635f, 10.902f, 14.618f)
                    curveTo(10.864f, 14.601f, 10.824f, 14.593f, 10.783f, 14.594f)
                    curveTo(10.742f, 14.595f, 10.701f, 14.605f, 10.665f, 14.623f)
                    curveTo(10.628f, 14.642f, 10.596f, 14.668f, 10.57f, 14.7f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(20.64f, 13.13f)
                    curveTo(20.165f, 13.18f, 19.685f, 13.18f, 19.21f, 13.13f)
                    curveTo(17.79f, 12.93f, 15.86f, 11.69f, 16.54f, 9.66f)
                    curveTo(16.769f, 9.067f, 17.136f, 8.538f, 17.611f, 8.115f)
                    curveTo(18.086f, 7.692f, 18.655f, 7.389f, 19.27f, 7.23f)
                    curveTo(19.92f, 7.033f, 20.616f, 7.054f, 21.253f, 7.288f)
                    curveTo(21.89f, 7.523f, 22.433f, 7.959f, 22.8f, 8.53f)
                    curveTo(22.982f, 8.822f, 23.104f, 9.148f, 23.16f, 9.488f)
                    curveTo(23.215f, 9.827f, 23.203f, 10.175f, 23.123f, 10.51f)
                    curveTo(23.043f, 10.845f, 22.897f, 11.16f, 22.694f, 11.439f)
                    curveTo(22.491f, 11.717f, 22.235f, 11.952f, 21.94f, 12.13f)
                    curveTo(21.872f, 12.178f, 21.825f, 12.252f, 21.81f, 12.334f)
                    curveTo(21.795f, 12.416f, 21.813f, 12.501f, 21.86f, 12.57f)
                    curveTo(21.882f, 12.606f, 21.912f, 12.637f, 21.946f, 12.661f)
                    curveTo(21.981f, 12.685f, 22.021f, 12.701f, 22.062f, 12.708f)
                    curveTo(22.104f, 12.716f, 22.146f, 12.715f, 22.187f, 12.705f)
                    curveTo(22.228f, 12.694f, 22.267f, 12.676f, 22.3f, 12.65f)
                    curveTo(22.684f, 12.435f, 23.021f, 12.146f, 23.292f, 11.799f)
                    curveTo(23.563f, 11.453f, 23.763f, 11.056f, 23.879f, 10.632f)
                    curveTo(23.995f, 10.207f, 24.026f, 9.764f, 23.969f, 9.328f)
                    curveTo(23.913f, 8.891f, 23.771f, 8.471f, 23.55f, 8.09f)
                    curveTo(23.104f, 7.318f, 22.418f, 6.714f, 21.597f, 6.369f)
                    curveTo(20.775f, 6.024f, 19.863f, 5.958f, 19f, 6.18f)
                    curveTo(18.217f, 6.385f, 17.496f, 6.779f, 16.901f, 7.329f)
                    curveTo(16.306f, 7.878f, 15.856f, 8.565f, 15.59f, 9.33f)
                    curveTo(15.423f, 9.841f, 15.393f, 10.388f, 15.504f, 10.914f)
                    curveTo(15.614f, 11.441f, 15.861f, 11.929f, 16.22f, 12.33f)
                    curveTo(16.055f, 12.272f, 15.894f, 12.202f, 15.74f, 12.12f)
                    curveTo(15.218f, 11.772f, 14.788f, 11.302f, 14.49f, 10.75f)
                    curveTo(14.245f, 10.309f, 13.935f, 9.908f, 13.57f, 9.56f)
                    curveTo(13.277f, 9.266f, 12.914f, 9.051f, 12.515f, 8.935f)
                    curveTo(12.116f, 8.82f, 11.695f, 8.808f, 11.29f, 8.9f)
                    curveTo(10.958f, 9.04f, 10.667f, 9.261f, 10.441f, 9.542f)
                    curveTo(10.216f, 9.823f, 10.064f, 10.156f, 10f, 10.51f)
                    curveTo(9.941f, 10.736f, 9.941f, 10.974f, 10f, 11.2f)
                    curveTo(10.106f, 11.476f, 10.226f, 11.747f, 10.36f, 12.01f)
                    curveTo(10.494f, 12.269f, 10.645f, 12.519f, 10.81f, 12.76f)
                    curveTo(11.221f, 13.391f, 11.704f, 13.971f, 12.25f, 14.49f)
                    curveTo(12.802f, 15.01f, 13.42f, 15.454f, 14.09f, 15.81f)
                    curveTo(14.626f, 16.035f, 15.173f, 16.232f, 15.73f, 16.4f)
                    curveTo(15.896f, 16.46f, 16.054f, 16.54f, 16.2f, 16.64f)
                    curveTo(16.303f, 17.009f, 16.37f, 17.388f, 16.4f, 17.77f)
                    curveTo(16.475f, 18.367f, 16.505f, 18.969f, 16.49f, 19.57f)
                    curveTo(16.468f, 20.49f, 16.394f, 21.408f, 16.27f, 22.32f)
                    curveTo(16.258f, 22.394f, 16.276f, 22.47f, 16.32f, 22.531f)
                    curveTo(16.364f, 22.592f, 16.431f, 22.633f, 16.505f, 22.645f)
                    curveTo(16.579f, 22.657f, 16.655f, 22.639f, 16.716f, 22.595f)
                    curveTo(16.777f, 22.551f, 16.818f, 22.484f, 16.83f, 22.41f)
                    curveTo(16.98f, 21.49f, 17.13f, 20.55f, 17.19f, 19.61f)
                    curveTo(17.235f, 18.974f, 17.235f, 18.336f, 17.19f, 17.7f)
                    curveTo(17.178f, 17.288f, 17.138f, 16.877f, 17.07f, 16.47f)
                    curveTo(17.028f, 16.293f, 16.938f, 16.13f, 16.81f, 16f)
                    curveTo(16.59f, 15.812f, 16.344f, 15.657f, 16.08f, 15.54f)
                    curveTo(15.58f, 15.31f, 14.99f, 15.16f, 14.56f, 14.92f)
                    curveTo(13.979f, 14.588f, 13.441f, 14.185f, 12.96f, 13.72f)
                    curveTo(12.479f, 13.251f, 12.047f, 12.735f, 11.67f, 12.18f)
                    curveTo(11.53f, 11.96f, 11.4f, 11.74f, 11.28f, 11.51f)
                    curveTo(10.86f, 10.75f, 10.96f, 10.79f, 11.02f, 10.58f)
                    curveTo(11.046f, 10.422f, 11.109f, 10.272f, 11.205f, 10.143f)
                    curveTo(11.3f, 10.014f, 11.426f, 9.91f, 11.57f, 9.84f)
                    curveTo(11.91f, 9.71f, 12.36f, 9.84f, 13f, 10.29f)
                    curveTo(13.64f, 10.74f, 14.1f, 12.11f, 15.35f, 12.77f)
                    curveTo(15.941f, 13.07f, 16.598f, 13.215f, 17.26f, 13.19f)
                    curveTo(17.357f, 13.253f, 17.457f, 13.309f, 17.56f, 13.36f)
                    curveTo(18.052f, 13.583f, 18.581f, 13.715f, 19.12f, 13.75f)
                    curveTo(19.638f, 13.776f, 20.157f, 13.753f, 20.67f, 13.68f)
                    curveTo(20.706f, 13.676f, 20.742f, 13.665f, 20.774f, 13.648f)
                    curveTo(20.806f, 13.63f, 20.834f, 13.606f, 20.857f, 13.577f)
                    curveTo(20.879f, 13.548f, 20.895f, 13.514f, 20.905f, 13.479f)
                    curveTo(20.914f, 13.443f, 20.916f, 13.406f, 20.91f, 13.37f)
                    curveTo(20.905f, 13.303f, 20.874f, 13.24f, 20.823f, 13.196f)
                    curveTo(20.773f, 13.151f, 20.707f, 13.127f, 20.64f, 13.13f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(21.54f, 12.72f)
                    curveTo(21.515f, 12.688f, 21.483f, 12.662f, 21.447f, 12.644f)
                    curveTo(21.41f, 12.625f, 21.37f, 12.615f, 21.33f, 12.614f)
                    curveTo(21.289f, 12.613f, 21.248f, 12.62f, 21.211f, 12.636f)
                    curveTo(21.174f, 12.652f, 21.14f, 12.676f, 21.113f, 12.706f)
                    curveTo(21.086f, 12.737f, 21.065f, 12.772f, 21.053f, 12.811f)
                    curveTo(21.041f, 12.85f, 21.038f, 12.891f, 21.043f, 12.932f)
                    curveTo(21.049f, 12.972f, 21.063f, 13.01f, 21.085f, 13.045f)
                    curveTo(21.107f, 13.079f, 21.136f, 13.108f, 21.17f, 13.13f)
                    curveTo(22.001f, 14.071f, 22.525f, 15.243f, 22.67f, 16.49f)
                    curveTo(22.8f, 17.237f, 22.881f, 17.992f, 22.91f, 18.75f)
                    curveTo(22.947f, 19.924f, 22.917f, 21.099f, 22.82f, 22.27f)
                    curveTo(22.814f, 22.311f, 22.817f, 22.352f, 22.828f, 22.392f)
                    curveTo(22.838f, 22.432f, 22.857f, 22.469f, 22.882f, 22.502f)
                    curveTo(22.907f, 22.534f, 22.938f, 22.561f, 22.974f, 22.582f)
                    curveTo(23.01f, 22.602f, 23.049f, 22.615f, 23.09f, 22.62f)
                    curveTo(23.172f, 22.632f, 23.255f, 22.611f, 23.323f, 22.563f)
                    curveTo(23.39f, 22.514f, 23.435f, 22.441f, 23.45f, 22.36f)
                    curveTo(23.707f, 21.172f, 23.864f, 19.964f, 23.92f, 18.75f)
                    curveTo(23.953f, 17.917f, 23.879f, 17.084f, 23.7f, 16.27f)
                    curveTo(23.402f, 14.877f, 22.64f, 13.625f, 21.54f, 12.72f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(7.14f, 4.65f)
                    curveTo(7.188f, 4.819f, 7.252f, 4.983f, 7.33f, 5.14f)
                    curveTo(7.4f, 5.295f, 7.48f, 5.445f, 7.57f, 5.59f)
                    curveTo(7.8f, 5.94f, 8.054f, 6.274f, 8.33f, 6.59f)
                    curveTo(8.368f, 6.653f, 8.428f, 6.699f, 8.498f, 6.72f)
                    curveTo(8.569f, 6.74f, 8.644f, 6.733f, 8.71f, 6.7f)
                    curveTo(8.775f, 6.664f, 8.823f, 6.604f, 8.844f, 6.533f)
                    curveTo(8.864f, 6.462f, 8.856f, 6.385f, 8.82f, 6.32f)
                    curveTo(8.69f, 5.93f, 8.62f, 5.55f, 8.49f, 5.17f)
                    curveTo(8.434f, 5.009f, 8.367f, 4.852f, 8.29f, 4.7f)
                    curveTo(8.22f, 4.548f, 8.14f, 4.401f, 8.05f, 4.26f)
                    curveTo(7.84f, 3.91f, 7.61f, 3.58f, 7.38f, 3.26f)
                    curveTo(7.343f, 3.185f, 7.278f, 3.128f, 7.199f, 3.101f)
                    curveTo(7.121f, 3.073f, 7.034f, 3.078f, 6.959f, 3.113f)
                    curveTo(6.883f, 3.148f, 6.824f, 3.211f, 6.795f, 3.289f)
                    curveTo(6.765f, 3.367f, 6.767f, 3.454f, 6.8f, 3.53f)
                    curveTo(6.92f, 3.89f, 7f, 4.27f, 7.14f, 4.65f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(11.67f, 4.89f)
                    curveTo(11.67f, 4.963f, 11.698f, 5.032f, 11.748f, 5.085f)
                    curveTo(11.799f, 5.137f, 11.868f, 5.167f, 11.94f, 5.17f)
                    curveTo(11.977f, 5.173f, 12.014f, 5.168f, 12.05f, 5.156f)
                    curveTo(12.085f, 5.143f, 12.117f, 5.124f, 12.144f, 5.098f)
                    curveTo(12.171f, 5.073f, 12.193f, 5.042f, 12.208f, 5.008f)
                    curveTo(12.222f, 4.974f, 12.23f, 4.937f, 12.23f, 4.9f)
                    curveTo(12.32f, 4.48f, 12.43f, 4.09f, 12.48f, 3.66f)
                    curveTo(12.49f, 3.484f, 12.49f, 3.307f, 12.48f, 3.13f)
                    curveTo(12.49f, 2.953f, 12.49f, 2.777f, 12.48f, 2.6f)
                    curveTo(12.48f, 2.19f, 12.36f, 1.79f, 12.3f, 1.37f)
                    curveTo(12.3f, 1.289f, 12.269f, 1.21f, 12.214f, 1.151f)
                    curveTo(12.158f, 1.091f, 12.081f, 1.055f, 12f, 1.05f)
                    curveTo(11.958f, 1.049f, 11.917f, 1.056f, 11.878f, 1.071f)
                    curveTo(11.84f, 1.086f, 11.804f, 1.108f, 11.774f, 1.137f)
                    curveTo(11.745f, 1.166f, 11.721f, 1.201f, 11.705f, 1.239f)
                    curveTo(11.688f, 1.277f, 11.68f, 1.318f, 11.68f, 1.36f)
                    curveTo(11.61f, 1.78f, 11.53f, 2.18f, 11.49f, 2.6f)
                    curveTo(11.48f, 2.777f, 11.48f, 2.954f, 11.49f, 3.13f)
                    curveTo(11.48f, 3.307f, 11.48f, 3.484f, 11.49f, 3.66f)
                    curveTo(11.51f, 4.08f, 11.61f, 4.47f, 11.67f, 4.89f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(15.31f, 6.27f)
                    curveTo(15.604f, 6.059f, 15.882f, 5.825f, 16.14f, 5.57f)
                    curveTo(16.241f, 5.459f, 16.331f, 5.338f, 16.41f, 5.21f)
                    curveTo(16.49f, 5.09f, 16.56f, 4.96f, 16.63f, 4.83f)
                    curveTo(16.78f, 4.51f, 16.9f, 4.2f, 17.04f, 3.89f)
                    curveTo(17.092f, 3.824f, 17.117f, 3.741f, 17.109f, 3.657f)
                    curveTo(17.102f, 3.574f, 17.063f, 3.496f, 17f, 3.44f)
                    curveTo(16.931f, 3.393f, 16.846f, 3.375f, 16.764f, 3.39f)
                    curveTo(16.681f, 3.405f, 16.608f, 3.452f, 16.56f, 3.52f)
                    curveTo(16.3f, 3.77f, 16.06f, 4f, 15.82f, 4.27f)
                    curveTo(15.73f, 4.38f, 15.64f, 4.49f, 15.56f, 4.61f)
                    curveTo(15.463f, 4.733f, 15.376f, 4.863f, 15.3f, 5f)
                    curveTo(15.15f, 5.31f, 15.05f, 5.62f, 14.89f, 5.92f)
                    curveTo(14.864f, 5.947f, 14.844f, 5.98f, 14.832f, 6.015f)
                    curveTo(14.819f, 6.05f, 14.813f, 6.087f, 14.815f, 6.125f)
                    curveTo(14.816f, 6.162f, 14.826f, 6.199f, 14.842f, 6.233f)
                    curveTo(14.858f, 6.267f, 14.881f, 6.297f, 14.909f, 6.322f)
                    curveTo(14.937f, 6.346f, 14.97f, 6.365f, 15.006f, 6.377f)
                    curveTo(15.041f, 6.389f, 15.079f, 6.394f, 15.116f, 6.391f)
                    curveTo(15.154f, 6.388f, 15.19f, 6.377f, 15.223f, 6.36f)
                    curveTo(15.257f, 6.343f, 15.286f, 6.319f, 15.31f, 6.29f)
                    verticalLineTo(6.27f)
                    close()
                }
            }
        }.build()

        return _GroupIllustration!!
    }

@Suppress("ObjectPropertyName")
private var _GroupIllustration: ImageVector? = null
