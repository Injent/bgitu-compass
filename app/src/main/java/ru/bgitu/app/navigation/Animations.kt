package ru.bgitu.app.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun slideAndScaleOut(
    targetOffsetX: (fullWidth: Int) -> Int = { -it / 2 },
) = slideOutHorizontally(targetOffsetX = targetOffsetX) +
        scaleOut(targetScale = 0.9f) +
        fadeOut(targetAlpha = 0.5f)

fun slideAndScaleIn(
    initialOffsetX: (fullWidth: Int) -> Int = { -it / 2 },
) = slideInHorizontally(
    initialOffsetX = initialOffsetX,
    animationSpec = spring(Spring.DampingRatioLowBouncy)
) +
        scaleIn(initialScale = 0.9f) +
        fadeIn(initialAlpha = 0.5f)