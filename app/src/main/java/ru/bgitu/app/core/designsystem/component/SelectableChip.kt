package ru.bgitu.app.core.designsystem.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun AppSelectableChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerRounding by animateDpAsState(
        if (selected) 8.dp else 16.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )

    InputChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        label = {
            label()
        },
        colors = InputChipDefaults.inputChipColors(
            containerColor = AppTheme.colorScheme.background1Container,
            selectedContainerColor = AppTheme.colorScheme.brand,
            labelColor = AppTheme.colorScheme.foreground1,
            selectedLabelColor = AppTheme.colorScheme.onBrand,
        ),
        border = null,
        shape = RoundedCornerShape(cornerRounding)
    )
}