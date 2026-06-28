package ru.bgitu.app.feature.widget.glance

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.background
import androidx.glance.color.ColorProviders
import androidx.glance.color.colorProviders
import androidx.glance.unit.ColorProvider
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.theme.AppColorScheme
import ru.bgitu.app.core.designsystem.theme.DarkColorScheme
import ru.bgitu.app.core.designsystem.theme.LightColorScheme
import ru.bgitu.app.core.model.WidgetPrefs
import ru.bgitu.app.core.model.WidgetTheme

@SuppressLint("RestrictedApi")
fun GlanceModifier.backgroundCompat(
    color: Color,
    shape: GlanceShape
): GlanceModifier {
    return background(
        imageProvider = ImageProvider(shape.resId),
        alpha = color.alpha,
        colorFilter = ColorFilter.tint(ColorProvider(color.copy(alpha = 1f)))
    )
}

enum class GlanceShape(
    @param:DrawableRes val resId: Int
) {
    ITEM_SINGLE(R.drawable.shape_item_single),
    ITEM_FIRST(R.drawable.shape_item_first),
    ITEM_MIDDLE(R.drawable.shape_item_middle),
    ITEM_LAST(R.drawable.shape_item_last),
    BTN_STEPPER_START(R.drawable.btn_stepper_start),
    BTN_STEPPER_END(R.drawable.btn_stepper_end),
    BTN_STEPPER_START_RIPPLE(R.drawable.btn_stepper_start_ripple),
    BTN_STEPPER_END_RIPPLE(R.drawable.btn_stepper_end_ripple);

    companion object {
        fun getListShape(index: Int, size: Int): GlanceShape {
            return when {
                size == 1 -> ITEM_SINGLE
                index == 0 -> ITEM_FIRST
                index == size - 1 -> ITEM_LAST
                else -> ITEM_MIDDLE
            }
        }
    }
}

object WidgetTheme {
    val widgetPadding = 12.dp
}

@Composable
fun CompassWidgetTheme(
    prefs: WidgetPrefs,
    content: @Composable () -> Unit
) {
    val defaultColors = GlanceTheme.colors
    val context = LocalContext.current
    val colors = when (prefs.uiTheme) {
        WidgetTheme.AUTO -> {
            val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isDarkMode = Configuration.UI_MODE_NIGHT_YES == currentNightMode
            appColorScheme(
                appColorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme,
                defaultColors = defaultColors,
                alpha = prefs.alpha
            )
        }
        WidgetTheme.DYNAMIC -> dynamicColorScheme(defaultColors = defaultColors, context = context, alpha = prefs.alpha)
        WidgetTheme.DARK -> appColorScheme(
            appColorScheme = DarkColorScheme,
            defaultColors = defaultColors,
            alpha = prefs.alpha
        )
        WidgetTheme.LIGHT -> appColorScheme(
            appColorScheme = LightColorScheme,
            defaultColors = defaultColors,
            alpha = prefs.alpha
        )
    }

    GlanceTheme(
        colors = colors,
        content = content
    )
}

@SuppressLint("RestrictedApi")
private fun ColorProvider.withAlpha(context: Context, alpha: Float): ColorProvider {
    return ColorProvider(getColor(context).copy(alpha = alpha))
}

private fun combineAlpha(alphaTop: Float, alphaBottom: Float): Float {
    val top = alphaTop.coerceIn(0f, 1f)
    val bottom = alphaBottom.coerceIn(0f, 1f)

    return top + bottom * (1f - top)
}

@SuppressLint("RestrictedApi")
private fun appColorScheme(
    appColorScheme: AppColorScheme,
    defaultColors: ColorProviders,
    alpha: Float
): ColorProviders {
    val overlayDiff = 1f - combineAlpha(alpha, alpha)
    val lessAlpha = (alpha - overlayDiff).coerceAtLeast(0f)

    return with(defaultColors) {
        colorProviders(
            primary = ColorProvider(appColorScheme.brand.copy(alpha)),
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = ColorProvider(appColorScheme.background1.copy(lessAlpha)),
            onSecondaryContainer = ColorProvider(appColorScheme.foreground1),
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            error = error,
            errorContainer = errorContainer,
            onError = onError,
            onErrorContainer = onErrorContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = ColorProvider(appColorScheme.foreground1),
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = ColorProvider(appColorScheme.foreground2),
            outline = outline,
            inverseOnSurface = inverseOnSurface,
            inverseSurface = inverseSurface,
            inversePrimary = inversePrimary,
            widgetBackground = ColorProvider(appColorScheme.background2.copy(alpha)),
        )
    }
}

private fun dynamicColorScheme(
    defaultColors: ColorProviders,
    context: Context,
    alpha: Float
): ColorProviders {
    val overlayDiff = 1f - combineAlpha(alpha, alpha)
    val lessAlpha = (alpha - overlayDiff).coerceAtLeast(0f)

    return with(defaultColors) {
        colorProviders(
            primary = primary.withAlpha(context, alpha),
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer.withAlpha(context, lessAlpha),
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            error = error,
            errorContainer = errorContainer,
            onError = onError,
            onErrorContainer = onErrorContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            inverseOnSurface = inverseOnSurface,
            inverseSurface = inverseSurface,
            inversePrimary = inversePrimary,
            widgetBackground = widgetBackground.withAlpha(context, alpha),
        )
    }
}