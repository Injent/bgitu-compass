package ru.bgitu.app.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import ru.bgitu.app.core.designsystem.icon.ArrowBack
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current

    IconButton(onClick = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
        onClick()
    }) {
        Icon(
            modifier = modifier,
            imageVector = Icons.ArrowBack,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground1
        )
    }
}