package ru.bgitu.app.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.taperedBorder
import kotlin.math.roundToInt

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun AppNavigationBar(
    selectedTabIndex: Int,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit
) {
    val features = LocalFeatures.current

    PrimaryTabRow(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        selectedTabIndex = selectedTabIndex,
        indicator = {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .tabIndicatorOffset(selectedTabIndex)
                    .zIndex(-1f)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                        .width(16.dp)
                        .background(AppTheme.colorScheme.brand, CircleShape)
                )
            }
        },
        divider = {},
        tabs = tabs,
        modifier = modifier
            .clip(CircleShape)
            .then(
                if (features.blurEnabled) {
                    Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(AppTheme.colorScheme.background1)
                    ) {
                        blurRadius = 15.dp
                        blurEnabled = true
                    }
                } else Modifier.background(AppTheme.colorScheme.background1)
            )
            .taperedBorder(
                side = TaperedBorderSide.Top,
                strokeWidth = 1.dp,
                color = AppTheme.colorScheme.foreground1.copy(.25f),
                shape = CircleShape,
                extensionSize = 48.dp
            )
            .taperedBorder(
                side = TaperedBorderSide.Bottom,
                strokeWidth = 1.dp,
                color = AppTheme.colorScheme.foreground1.copy(.15f),
                shape = CircleShape,
                extensionSize = 48.dp
            )
            .padding(4.dp)
    )
}

@Composable
fun AppNavItem(
    icon: @Composable () -> Unit,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    val offsetY = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val iconColor = if (AppTheme.colorScheme.dark) AppTheme.colorScheme.onBrand else AppTheme.colorScheme.brand
    val contentColor by animateColorAsState(
        if (selected) iconColor else AppTheme.colorScheme.foreground2.copy(.75f)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .height(54.dp)
            .clickable(enabled = !selected, indication = null, interactionSource = null) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
                scope.launch(NonCancellable) {
                    scale.animateTo(
                        targetValue = 1.1f,
                        animationSpec = tween(durationMillis = 50)
                    )
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
                scope.launch(NonCancellable) {
                    offsetY.animateTo(
                        targetValue = -10f,
                        animationSpec = tween(
                            durationMillis = 150,
                            easing = EaseOutBack
                        )
                    )
                    offsetY.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 150,
                            easing = EaseOutBack
                        )
                    )
                }
            }
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .scale(scale.value)
                    .offset {
                        IntOffset(x = 0, y = offsetY.value.roundToInt())
                    },
                content = { icon() }
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}