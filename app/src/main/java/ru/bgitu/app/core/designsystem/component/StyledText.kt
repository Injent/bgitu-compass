package ru.bgitu.app.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun GroupStyledText(
    text: String,
    modifier: Modifier = Modifier,
    topPadding: Dp = 24.dp,
    bottomPadding: Dp = 10.dp,
    startPadding: Dp = 8.dp,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = AppTheme.colorScheme.foreground2,
        modifier = modifier
            .padding(
                start = startPadding,
                end = 20.dp,
                top = topPadding,
                bottom = bottomPadding
            )
    )
}