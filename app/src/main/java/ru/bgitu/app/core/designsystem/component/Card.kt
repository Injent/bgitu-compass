package ru.bgitu.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.designsystem.theme.CompassTheme

object AppCardDefaults {
    val SingleCard = RoundedCornerShape(24.dp)
    val TopCard = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 4.dp,
        bottomEnd = 4.dp
    )
    val MiddleCard = RoundedCornerShape(4.dp)
    val BottomCard = RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 4.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )

    fun getVerticalListShape(
        index: Int,
        size: Int,
        inverted: Boolean = false
    ) = when {
        size == 1 -> SingleCard
        index == 0 -> if (inverted) BottomCard else TopCard
        index == size - 1 -> if (inverted) TopCard else BottomCard
        else -> MiddleCard
    }
}

@Composable
fun AppCard(
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    color: Color = AppTheme.colorScheme.background1,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = CardDefaults.cardColors(
        containerColor = color,
        disabledContainerColor = color,
        contentColor = AppTheme.colorScheme.foreground1
    )

    if (onClick != null) {
        Card(
            modifier = modifier,
            shape = shape,
            onClick = onClick,
            colors = colors,
            enabled = enabled,
            content = content,
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors,
            content = content
        )
    }
}

@Preview()
@Composable
private fun AppCardPreview() {
    CompassTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .background(AppTheme.colorScheme.background2)
                .padding(16.dp)
        ) {
            AppCard(
                shape = AppCardDefaults.getVerticalListShape(index = 0, size = 1)
            ) {
                ListItem(
                    colors = ListItemDefaults.colors(contentColor = Color.Transparent),
                    headlineContent = {
                        Text(text = "Single card")
                    }
                )
            }

            GroupStyledText(text = "Group")

            val size = 5
            repeat(5) { index ->
                AppCard(
                    shape = AppCardDefaults.getVerticalListShape(index = index, size = size)
                ) {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        headlineContent = {
                            Text(text = "Title")
                        }
                    )
                }
            }
        }
    }
}