package ru.bgitu.app.core.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.icon.Check
import ru.bgitu.app.core.designsystem.icon.CrossInSwitch
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppTheme.colorScheme.onBrand,
            checkedTrackColor = AppTheme.colorScheme.brand,
            checkedIconColor = AppTheme.colorScheme.brand,

            uncheckedThumbColor = AppTheme.colorScheme.stroke1,
            uncheckedTrackColor = AppTheme.colorScheme.background2,
            uncheckedIconColor = if (AppTheme.colorScheme.dark) AppTheme.colorScheme.foreground2 else AppTheme.colorScheme.background2,
            uncheckedBorderColor = AppTheme.colorScheme.stroke1
        ),
        thumbContent = {
            val icon = if (checked) {
                Icons.Check
            } else {
                Icons.CrossInSwitch
            }
            val size by animateDpAsState(
                if (checked) 18.dp else 16.dp
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(size)
            )
        },
        modifier = modifier
    )
}