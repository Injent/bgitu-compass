package ru.bgitu.app.feature.groups.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCard
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.icon.ArrowDown
import ru.bgitu.app.core.designsystem.icon.ArrowUp
import ru.bgitu.app.core.designsystem.icon.Delete
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.taperedBorder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun GroupDropDownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    groupIndex: Int,
    groupsSize: Int,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    content: @Composable ExposedDropdownMenuBoxScope.() -> Unit,
) {
    val features = LocalFeatures.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        content()

        ExposedDropdownMenu(
            expanded = expanded,
            containerColor = Color.Transparent,
            shadowElevation = 8.dp,
            shape = AppCardDefaults.SingleCard,
            onDismissRequest = { onExpandedChange(false) },
        ) {
            val items = remember(groupIndex, groupsSize) {
                GroupDropDownMenuActions.entries.filter { action ->
                    when (groupIndex) {
                        0 if action == GroupDropDownMenuActions.MOVE_UP -> false
                        groupsSize - 1 if action == GroupDropDownMenuActions.MOVE_DOWN -> false
                        else -> true
                    }
                }
            }

            items.forEachIndexed { index, action ->
                if (index > 0) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(AppTheme.colorScheme.background2)
                    )
                }
                val cardShape = AppCardDefaults.getVerticalListShape(
                    index = index,
                    size = items.size
                )
                AppCard(
                    shape = cardShape,
                    color = Color.Transparent,
                    onClick = {
                        onExpandedChange(false)
                        when (action) {
                            GroupDropDownMenuActions.MOVE_UP -> onMoveUp()
                            GroupDropDownMenuActions.MOVE_DOWN -> onMoveDown()
                            GroupDropDownMenuActions.DELETE -> onDelete()
                        }
                    },
                    modifier = Modifier
                        .clip(cardShape)
                        .then(
                            if (features.blurEnabled) {
                                Modifier.hazeEffect(
                                    state = hazeState,
                                    style = HazeMaterials.thick(AppTheme.colorScheme.background1),
                                ) {
                                    blurRadius = 15.dp
                                    blurEnabled = true
                                }
                            } else Modifier.background(AppTheme.colorScheme.background1)
                        )
                        .then(
                            when (index) {
                                0 -> Modifier.taperedBorder(
                                    side = TaperedBorderSide.Top,
                                    strokeWidth = 1.dp,
                                    color = AppTheme.colorScheme.foreground1.copy(.5f),
                                    shape = cardShape
                                )
                                items.lastIndex -> Modifier.taperedBorder(
                                    side = TaperedBorderSide.Bottom,
                                    strokeWidth = 1.dp,
                                    color = AppTheme.colorScheme.foreground1.copy(.25f),
                                    shape = cardShape
                                )
                                else -> Modifier
                            }
                        )
                ) {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                            headlineColor = action.contentColor(),
                            leadingIconColor = action.contentColor()
                        ),
                        headlineContent = {
                            Text(
                                text = stringResource(action.label)
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}

private enum class GroupDropDownMenuActions(
    val icon: ImageVector,
    @param:StringRes val label: Int,
    val contentColor: @Composable () -> Color,
) {
    MOVE_UP(
        icon = Icons.ArrowUp,
        label = R.string.feature_groups_moveUp,
        contentColor = { AppTheme.colorScheme.foreground1 }
    ),
    MOVE_DOWN(
        icon = Icons.ArrowDown,
        label = R.string.feature_groups_moveDown,
        contentColor = { AppTheme.colorScheme.foreground1 }
    ),
    DELETE(
        icon = Icons.Delete,
        label = R.string.feature_groups_delete,
        contentColor = { AppTheme.colorScheme.destructive }
    )
}