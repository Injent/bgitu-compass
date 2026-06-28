package ru.bgitu.app.core.designsystem.theme

import android.app.Activity
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ru.bgitu.app.core.utilui.Features
import ru.bgitu.app.core.utilui.LocalFeatures
import android.graphics.Color

object AppTheme {
    val colorScheme: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current
}

@Composable
fun CompassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    features: Features = Features(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val rippleConfig = RippleConfiguration(
        color = colorScheme.rippleBackground,
        rippleAlpha = RippleAlpha(
            pressedAlpha = 0.1f,
            draggedAlpha = 0.1f,
            focusedAlpha = 0.1f,
            hoveredAlpha = 0.1f
        )
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        @Suppress("DEPRECATION")
        SideEffect {
            val window = (view.context as Activity).window
            if (SDK_INT >= 29) {
                window.isStatusBarContrastEnforced = false
                window.isNavigationBarContrastEnforced = false
            }
            if (SDK_INT < 35) {
                window.statusBarColor = Color.TRANSPARENT
                window.navigationBarColor = Color.TRANSPARENT
            }
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !colorScheme.dark
            insetsController.isAppearanceLightNavigationBars = !colorScheme.dark
        }
    }

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalRippleConfiguration provides rippleConfig,
        LocalFeatures provides features
    ) {
        MaterialTheme(
            typography = Typography,
            colorScheme = MaterialTheme.colorScheme.copy(
                background = colorScheme.background2
            ),
            content = content
        )
    }
}