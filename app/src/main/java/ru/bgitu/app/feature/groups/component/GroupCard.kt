package ru.bgitu.app.feature.groups.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.component.AppCard
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.designsystem.theme.CompassTheme
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.taperedBorder

@Composable
fun GroupCard(
    group: Group,
    onClick: (Group) -> Unit,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val color by animateColorAsState(
        if (selected) {
            AppTheme.colorScheme.rippleBackground.copy(.1f)
                .compositeOver(AppTheme.colorScheme.background1)
        } else {
            AppTheme.colorScheme.background1
        }
    )

    AppCard(
        shape = shape,
        onClick = {
            onClick(group)
        },
        color = color,
        modifier = modifier
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
                contentColor = AppTheme.colorScheme.foreground1,
            ),
            headlineContent = {
                Text(text = group.name)
            },
            leadingContent = {
                val leadingContentColor = when (group.degree) {
                    Group.Degree.BACHELOR -> AppTheme.colorScheme.bachelor
                    Group.Degree.MASTER -> AppTheme.colorScheme.master
                    Group.Degree.SPE -> AppTheme.colorScheme.spe
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .background(leadingContentColor, CircleShape)
                        .taperedBorder(
                            side = TaperedBorderSide.Top,
                            color = Color.White.copy(.35f),
                            shape = CircleShape,
                            strokeWidth = 1.2.dp
                        )
                        .taperedBorder(
                            side = TaperedBorderSide.Start,
                            color = Color.White.copy(.25f),
                            shape = CircleShape,
                            strokeWidth = 1.2.dp
                        )
                ) {
                    Text(
                        text = when (group.degree) {
                            Group.Degree.BACHELOR -> "БАК"
                            Group.Degree.MASTER -> "МАГ"
                            Group.Degree.SPE -> "CПО"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            },
            trailingContent = trailingIcon
        )
    }
}

@Preview
@Composable
private fun GroupCardPreview() {
    CompassTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .background(AppTheme.colorScheme.background2)
                .padding(16.dp)
        ) {
            val groups = listOf(
                Group(id = 1, name = "ЭКОНм-101"),
                Group(id = 2, name = "СТР-102"),
                Group(id = 3, name = "ИСТ(СПО)-103"),
            )
            groups.forEachIndexed { index, group ->
                GroupCard(
                    group = group,
                    onClick = {},
                    shape = AppCardDefaults.getVerticalListShape(
                        index = index,
                        size = groups.size
                    )
                )
            }
        }
    }
}