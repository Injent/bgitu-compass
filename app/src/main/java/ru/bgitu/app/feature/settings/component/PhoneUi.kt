package ru.bgitu.app.feature.settings.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.designsystem.theme.AppColorScheme
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.designsystem.theme.DarkColorScheme
import ru.bgitu.app.core.designsystem.theme.LightColorScheme
import ru.bgitu.app.core.model.UiTheme

@Composable
fun PhoneUiSection(
    uiTheme: UiTheme,
    onChangeUiTheme: (UiTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        GroupStyledText(
            text = stringResource(R.string.feature_settings_uiTheme),
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            UiTheme.entries.forEach { theme ->
                val borderColor by animateColorAsState(
                    if (uiTheme == theme) AppTheme.colorScheme.brand else AppTheme.colorScheme.stroke1
                )
                val icon = rememberPhoneUiIcon(theme)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(
                            enabled = uiTheme != theme,
                            indication = null,
                            interactionSource = null,
                            onClick = { onChangeUiTheme(theme) }
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.5.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(5.dp)
                    ) {
                        Image(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                        )
                    }
                    Text(
                        text = stringResource(
                            when (theme) {
                                UiTheme.AUTO -> R.string.feature_settings_uiTheme_auto
                                UiTheme.DARK -> R.string.feature_settings_uiTheme_dark
                                UiTheme.LIGHT -> R.string.feature_settings_uiTheme_light
                            }
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = AppTheme.colorScheme.foreground1,
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberPhoneUiIcon(
    uiTheme: UiTheme
): ImageVector {
    fun AppColorScheme.toPhoneUiTheme() = PhoneUiTheme(
        skeleton = background4,
        background1 = background1,
        background2 = background2,
        brand = brand
    )

    return remember(uiTheme) {
        val lightTheme = when (uiTheme) {
            UiTheme.DARK -> DarkColorScheme
            else -> LightColorScheme
        }.toPhoneUiTheme()
        val darkTheme = when (uiTheme) {
            UiTheme.LIGHT -> LightColorScheme
            else -> DarkColorScheme
        }.toPhoneUiTheme()
        createThemedImageVector(
            lightTheme = lightTheme,
            darkTheme = darkTheme
        )
    }
}

private fun createThemedImageVector(
    lightTheme: PhoneUiTheme,
    darkTheme: PhoneUiTheme
): ImageVector {
    return ImageVector.Builder(
        name = "PhoneUi",
        defaultWidth = 70.dp,
        defaultHeight = 140.dp,
        viewportWidth = 60f,
        viewportHeight = 120f
    ).apply {
        path(
            fill = SolidColor(lightTheme.background2),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(30f)
            verticalLineToRelative(120f)
            horizontalLineToRelative(-30f)
            close()
        }
        path(
            fill = SolidColor(darkTheme.background2),
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(30f, 0f)
            horizontalLineToRelative(30f)
            verticalLineToRelative(120f)
            horizontalLineToRelative(-30f)
            close()
        }
        path(fill = SolidColor(lightTheme.background1)) {
            moveTo(30f, 60f)
            lineTo(30f, 120f)
            lineTo(6f, 120f)
            arcTo(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 114f)
            lineTo(0f, 66f)
            arcTo(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 60f)
            close()
        }
        path(fill = SolidColor(darkTheme.background1)) {
            moveTo(30f, 60f)
            lineTo(54f, 60f)
            arcTo(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 60f, 66f)
            lineTo(60f, 114f)
            arcTo(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 54f, 120f)
            lineTo(30f, 120f)
            close()
        }
        path(fill = SolidColor(lightTheme.skeleton)) {
            moveTo(30f, 65f)
            lineTo(30f, 85f)
            lineTo(8f, 85f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 81f)
            lineTo(4f, 69f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 65f)
            close()
        }
        path(fill = SolidColor(darkTheme.skeleton)) {
            moveTo(30f, 65f)
            lineTo(52f, 65f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 56f, 69f)
            lineTo(56f, 81f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 52f, 85f)
            lineTo(30f, 85f)
            close()
        }
        path(fill = SolidColor(lightTheme.skeleton)) {
            moveTo(30f, 33f)
            lineTo(30f, 49f)
            lineTo(8f, 49f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 45f)
            lineTo(4f, 37f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 33f)
            close()
        }
        path(fill = SolidColor(darkTheme.skeleton)) {
            moveTo(30f, 33f)
            lineTo(52f, 33f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 56f, 37f)
            lineTo(56f, 45f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 52f, 49f)
            lineTo(30f, 49f)
            close()
        }
        path(fill = SolidColor(lightTheme.skeleton)) {
            moveTo(30f, 8f)
            lineTo(30f, 28f)
            lineTo(8f, 28f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 24f)
            lineTo(4f, 12f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 8f)
            close()
        }
        path(fill = SolidColor(darkTheme.skeleton)) {
            moveTo(30f, 8f)
            lineTo(52f, 8f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 56f, 12f)
            lineTo(56f, 24f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 52f, 28f)
            lineTo(30f, 28f)
            close()
        }
        path(fill = SolidColor(lightTheme.skeleton)) {
            moveTo(30f, 88f)
            lineTo(30f, 100f)
            lineTo(8f, 100f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 96f)
            lineTo(4f, 92f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 88f)
            close()
        }
        path(fill = SolidColor(darkTheme.skeleton)) {
            moveTo(30f, 88f)
            lineTo(52f, 88f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 56f, 92f)
            lineTo(56f, 96f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 52f, 100f)
            lineTo(30f, 100f)
            close()
        }
        path(fill = SolidColor(lightTheme.brand)) {
            moveTo(30f, 103f)
            lineTo(30f, 115f)
            lineTo(8f, 115f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 111f)
            lineTo(4f, 107f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 103f)
            close()
        }
        path(fill = SolidColor(darkTheme.brand)) {
            moveTo(30f, 103f)
            lineTo(52f, 103f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 56f, 107f)
            lineTo(56f, 111f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 52f, 115f)
            lineTo(30f, 115f)
            close()
        }
    }.build()
}

private class PhoneUiTheme(
    val skeleton: Color,
    val background1: Color,
    val background2: Color,
    val brand: Color,
)