package ru.bgitu.app.feature.settings.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.component.AppCard
import ru.bgitu.app.core.designsystem.component.FramedIcon
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun SettingsTopLevelCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    icon: ImageVector,
    iconTint: Color,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    AppCard(
        shape = shape,
        onClick = onClick,
        modifier = modifier
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
                headlineColor = AppTheme.colorScheme.foreground1,
                supportingColor = AppTheme.colorScheme.foreground2
            ),
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            },
            supportingContent = {
                Text(
                    text = description,
                    maxLines = 2
                )
            },
            leadingContent = {
                FramedIcon(
                    containerColor = AppTheme.colorScheme.background2,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            }
        )
    }
}