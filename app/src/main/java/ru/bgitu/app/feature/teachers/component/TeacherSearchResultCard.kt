package ru.bgitu.app.feature.teachers.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import ru.bgitu.app.core.designsystem.component.AppCard
import ru.bgitu.app.core.designsystem.icon.Close
import ru.bgitu.app.core.designsystem.icon.History
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.Search
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.SearchResultTeacher

@Composable
fun TeacherSearchResultCard(
    result: SearchResultTeacher,
    shape: RoundedCornerShape,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null
) {
    AppCard(
        shape = shape,
        onClick = onClick,
        modifier = modifier
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
                contentColor = AppTheme.colorScheme.foreground1,
                leadingContentColor = AppTheme.colorScheme.foreground2
            ),
            headlineContent = {
                Text(text = result.teacher.fullName, maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            leadingContent = {
                Icon(
                    imageVector = if (result.appearedBefore) Icons.History else Icons.Search,
                    contentDescription = null
                )
            },
            trailingContent = {
                if (onDelete != null) {
                    Icon(
                        imageVector = Icons.Close,
                        tint = AppTheme.colorScheme.foreground3,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(onClick = onDelete)
                            .clip(CircleShape)
                    )
                }
            }
        )
    }
}

