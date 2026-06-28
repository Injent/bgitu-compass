package ru.bgitu.app.core.designsystem.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColorScheme(
    val brand: Color,
    val bachelor: Color,
    val master: Color,
    val spe: Color,
    val designIcon: Color,
    val notificationIcon: Color,
    val syncIcon: Color,
    val widgetIcon: Color,
    val cat: Color,

    val foreground1: Color,
    val foreground2: Color,
    val foreground3: Color,
    val foreground4: Color,

    val onBrand: Color,
    val background1: Color,
    val background2: Color,
    val background3: Color,
    val background4: Color,
    val background1Container: Color,

    val stroke1: Color,
    val stroke2: Color,

    val destructive: Color,
    val dark: Boolean,

    val rippleBackground: Color
)

val DarkColorScheme = AppColorScheme(
    brand = Color(0xFF5696fd),
    bachelor = Color(0xFF1696e6),
    master = Color(0xFFea9513),
    spe = Color(0xFF3fbf3d),
    designIcon = Color(0xFFffb683),
    notificationIcon = Color(0xFFffaee4),
    syncIcon = Color(0xFFa1c9ff),
    cat = Color(0xFFd1d1d1),
    widgetIcon = Color(0xFFbeefbb),

    foreground1 = Color(0xFFd1d1d1),
    foreground2 = Color(0xFFababab),
    foreground3 = Color(0xFF636267),
    foreground4 = Color(0xff89888c),
    onBrand = Color.White,
    background1 = Color(0xFF25262b),
    background2 = Color(0xFF17181c),
    background3 = Color(0xFF1e1f24),
    background4 = Color(0xFF1e1f24),
    background1Container = Color(0xFF3A3C41),
    stroke1 = Color(0xFF414046),
    stroke2 = Color(0xFF36373c),
    destructive = Color(0xFFFF453A),
    rippleBackground = Color.White,
    dark = true
)


val LightColorScheme = AppColorScheme(
    brand = Color(0xFF007afe),
    bachelor = Color(0xFF1696e6),
    master = Color(0xFFea9513),
    spe = Color(0xFF3fbf3d),
    designIcon = Color(0xFFFCAE79),
    notificationIcon = Color(0xFFFC6BC1),
    syncIcon = Color(0xFF60A2FC),
    cat = Color(0xFF3B3B3B),
    widgetIcon = Color(0xFFbeefbb),

    foreground1 = Color(0xFF0c0d0f),
    foreground2 = Color(0xFF78797b),
    foreground3 = Color(0xFF969698),
    foreground4 = Color(0xff89888c),
    onBrand = Color.White,
    background1 = Color(0xFFffffff),
    background2 = Color(0xFFedeef2),
    background3 = Color(0xFFFFFFFF),
    background4 = Color(0xFFE7E7E7),
    background1Container = Color(0xFFf3edf7),
    stroke1 = Color(0xFF808281),
    stroke2 = Color(0xFFDCDCDC),
    destructive = Color(0xFFea3f48),
    rippleBackground = Color.Black,
    dark = false
)

internal val LocalAppColorScheme = compositionLocalOf<AppColorScheme> {
    error("AppColorScheme not provided")
}