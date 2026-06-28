package ru.bgitu.app.feature.schedule.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.AppTab
import ru.bgitu.app.core.designsystem.icon.Edit
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.utilui.LocalFeatures

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun GroupTabs(
    selectedGroup: Group?,
    groups: List<Group>,
    onSelect: (Group) -> Unit,
    onClickSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(AppTheme.colorScheme.background3, AppCardDefaults.SingleCard)
            .border(width = 1.dp, color = AppTheme.colorScheme.stroke2, shape = AppCardDefaults.SingleCard)
            .padding(4.dp)
    ) {
        IconButton(
            onClick = onClickSettings,
            modifier = Modifier
                .padding(horizontal = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Edit,
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground2
            )
        }

        val selectedTabIndex = remember(selectedGroup, groups) {
            groups.indexOf(selectedGroup).coerceAtLeast(0)
        }

        SecondaryScrollableTabRow(
            containerColor = Color.Transparent,
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp,
            indicator = {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .tabIndicatorOffset(selectedTabIndex)
                        .zIndex(-1f)
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(32.dp)
                            .height(4.dp)
                            .background(
                                color = AppTheme.colorScheme.brand,
                                shape = CircleShape
                            )
                    )
                }
            },
            divider = {},
            tabs = {
                groups.forEach { group ->
                    AppTab(
                        shape = CircleShape,
                        selected = group == selectedGroup,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                            onSelect(group)
                        }
                    ) {
                        Text(
                            text = group.name,
                        )
                    }
                }
            }
        )
    }
}