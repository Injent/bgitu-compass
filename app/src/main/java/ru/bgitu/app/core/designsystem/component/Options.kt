package ru.bgitu.app.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.horizontalFadingEdges

@Composable
fun SwitchableOption(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    description: String? = null,
    supportingContent: (@Composable () -> Unit)? = null
) {
    val alpha by animateFloatAsState(
        if (enabled) 1f else 0.5f
    )

    AppCard(
        shape = shape,
        onClick = { onCheckedChange(!checked) },
        enabled = enabled,
        modifier = modifier
            .alpha(alpha)
    ) {
        ListItem(
            colors = ListItemDefaults.appColors(transparentBackground = true),
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colorScheme.foreground1
                )
            },
            supportingContent = description?.let {
                {
                    Column {
                        Text(text = it)
                        supportingContent?.invoke()
                    }
                }
            },
            leadingContent = {
                icon?.let {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(32.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                        )
                    }
                }
            },
            trailingContent = {
                AppSwitch(
                    checked = checked,
                    onCheckedChange = {
                        if (enabled) onCheckedChange(it)
                    },
                )
            }
        )
    }
}

@Composable
fun SingleChoiceOption(
    title: String,
    icon: ImageVector,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    CustomOption(
        title = title,
        icon = icon,
        shape = shape,
        enabled = enabled,
        modifier = modifier
    ) {
        val scrollState = rememberScrollState()

        CompositionLocalProvider(
            LocalContentColor provides AppTheme.colorScheme.foreground2,
            LocalTextStyle provides MaterialTheme.typography.bodyLarge
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalFadingEdges(
                        scrollState = scrollState,
                        color = AppTheme.colorScheme.background1
                    )
                    .horizontalScroll(scrollState),
                content = content
            )
        }
    }
}

@Composable
fun SliderOption(
    title: String,
    icon: ImageVector,
    value: Float,
    onValueChange: (Float) -> Unit,
    shape: RoundedCornerShape,
    steps: Int = 0,
    modifier: Modifier = Modifier
) {
    CustomOption(
        title = title,
        icon = icon,
        shape = shape,
        modifier = modifier
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            steps = steps,
            colors = SliderDefaults.colors(
                activeTickColor = AppTheme.colorScheme.onBrand,
                activeTrackColor = AppTheme.colorScheme.brand,
                thumbColor = AppTheme.colorScheme.brand,
                inactiveTickColor = AppTheme.colorScheme.foreground2,
                inactiveTrackColor = AppTheme.colorScheme.stroke1
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun CustomOption(
    title: String,
    icon: ImageVector,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val alpha by animateFloatAsState(
        if (enabled) 1f else 0.5f
    )

    ListItem(
        colors = ListItemDefaults.appColors(),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.colorScheme.foreground1,
            )
        },
        supportingContent = content,
        leadingContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(32.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier
            .clip(shape)
            .alpha(alpha)
    )
}

@Composable
private fun ListItemDefaults.appColors(transparentBackground: Boolean = false): ListItemColors {
    return colors(
        containerColor = if (transparentBackground) Color.Transparent else AppTheme.colorScheme.background1,
        disabledContainerColor = if (transparentBackground) Color.Transparent else AppTheme.colorScheme.background1.copy(alpha = 0.5f),
        supportingContentColor = AppTheme.colorScheme.foreground2,
        leadingContentColor = AppTheme.colorScheme.foreground2
    )
}