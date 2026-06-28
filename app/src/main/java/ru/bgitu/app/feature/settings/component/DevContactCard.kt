package ru.bgitu.app.feature.settings.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.component.DynamicAsyncImage
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.util.openTab
import ru.bgitu.app.feature.settings.model.DevContact

@Composable
fun DevContactCard(
    contact: DevContact,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ListItem(
        colors = ListItemDefaults.colors(
            headlineColor = AppTheme.colorScheme.foreground1,
            supportingColor = AppTheme.colorScheme.foreground2,
            containerColor = AppTheme.colorScheme.background1
        ),
        headlineContent = {
            Text(text = stringResource(contact.nameResId))
        },
        supportingContent = {
            Text(text = stringResource(contact.roleResId))
        },
        leadingContent = {
            DynamicAsyncImage(
                imageUrl = contact.avatarUrl,
                contentDescription = null,
                placeholder = {},
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
            )
        },
        trailingContent = {
            Row {
                contact.socials.forEach { social ->
                    IconButton(
                        onClick = { context.openTab(social.url) }
                    ) {
                        Icon(
                            imageVector = social.icon,
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        modifier = modifier
            .clip(shape)
    )
}