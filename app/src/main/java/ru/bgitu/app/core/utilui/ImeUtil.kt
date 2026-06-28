package ru.bgitu.app.core.utilui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClearFocusOnImeCloseEffect() {
    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(isImeVisible) {
        if (isImeVisible) return@LaunchedEffect
        focusManager.clearFocus()
    }
}