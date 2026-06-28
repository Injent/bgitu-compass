package ru.bgitu.app.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun InfoCard(
    icon: @Composable () -> Unit,
    label: String,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit,
) {
    ListItemDefaults.ContentPadding
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(shape)
            .background(AppTheme.colorScheme.background1)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .height(48.dp),
    ) {
        Spacer(Modifier.width(16.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colorScheme.foreground2
            ) {
                icon()
            }
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.foreground1
        )
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.width(16.dp))
        CompositionLocalProvider(
            LocalContentColor provides AppTheme.colorScheme.foreground2,
            content = trailingContent
        )
    }
}