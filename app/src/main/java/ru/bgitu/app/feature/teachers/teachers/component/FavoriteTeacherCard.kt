package ru.bgitu.app.feature.teachers.teachers.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.icon.Bookmark
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Teacher

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FavoriteTeacherCard(
    teacher: Teacher,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = AppTheme.colorScheme.background1
        ),
        shapes = ListItemDefaults.shapes(shape = AppCardDefaults.SingleCard),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    imageVector = Icons.Bookmark,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.foreground1
                )
            }
            BasicText(
                text = teacher.shortName,
                style = MaterialTheme.typography.titleMedium
                    .merge(color = AppTheme.colorScheme.foreground1),
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 13.sp, maxFontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            )
        }
    }
}