package ru.bgitu.app.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme

enum class ButtonType {
    PRIMARY,
    SECONDARY,
    DESTRUCTIVE
}

@Composable
fun AppContentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    contentPadding: PaddingValues = PaddingValues(vertical = 13.dp, horizontal = 20.dp),
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val containerColor = when (type) {
        ButtonType.PRIMARY -> AppTheme.colorScheme.brand
        else -> AppTheme.colorScheme.background4
    }
    val contentColor = when (type) {
        ButtonType.PRIMARY -> AppTheme.colorScheme.onBrand
        ButtonType.SECONDARY -> AppTheme.colorScheme.foreground1
        ButtonType.DESTRUCTIVE -> AppTheme.colorScheme.destructive
    }
    val alpha by animateFloatAsState(
        if (enabled) 1f else .75f
    )

    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = AppTheme.colorScheme.foreground2,
        ),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        enabled = enabled,
        content = {
            content()
        },
        modifier = modifier
            .alpha(alpha)
    )
}