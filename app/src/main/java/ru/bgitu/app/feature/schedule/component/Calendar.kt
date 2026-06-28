package ru.bgitu.app.feature.schedule.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.component.ButtonType
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.taperedBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDatePickerDialog(
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(34.dp),
        colors = DatePickerDefaults.colors(
            containerColor = AppTheme.colorScheme.background3
        ),
        confirmButton = {
            AppContentButton(
                onClick = { state.getSelectedDate()?.toKotlinLocalDate()?.let { onSelect(it) } },
                enabled = state.selectedDateMillis != null,
                modifier = Modifier
                    .padding(bottom = 10.dp, end = 14.dp, top = 14.dp)
            ) {
                Text(
                    text = stringResource(android.R.string.ok)
                )
            }
        },
        dismissButton = {
            AppContentButton(
                type = ButtonType.SECONDARY,
                onClick = onDismissRequest,
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = stringResource(android.R.string.cancel)
                )
            }
        },
        modifier = modifier
            .taperedBorder(
                side = TaperedBorderSide.Bottom,
                strokeWidth = 1.dp,
                color = AppTheme.colorScheme.foreground1.copy(.15f),
                extensionSize = 30.dp,
                shape = RoundedCornerShape(34.dp)
            )
    ) {
        DatePicker(
            state = state,
            title = null,
            headline = null,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = AppTheme.colorScheme.background1,

                weekdayContentColor = AppTheme.colorScheme.foreground1,

                dayContentColor = AppTheme.colorScheme.foreground1,
                selectedDayContainerColor = AppTheme.colorScheme.brand,
                selectedDayContentColor = AppTheme.colorScheme.onBrand,
                disabledDayContentColor = AppTheme.colorScheme.foreground3,
                todayDateBorderColor = AppTheme.colorScheme.brand,
                todayContentColor = AppTheme.colorScheme.brand,

                navigationContentColor = AppTheme.colorScheme.foreground1,

                yearContentColor = AppTheme.colorScheme.foreground1,
                currentYearContentColor = AppTheme.colorScheme.brand,
                selectedYearContentColor = AppTheme.colorScheme.onBrand,
                selectedYearContainerColor = AppTheme.colorScheme.brand,
                disabledYearContentColor = AppTheme.colorScheme.foreground3,
                disabledSelectedYearContainerColor = Color.Transparent,

                subheadContentColor = AppTheme.colorScheme.foreground1,
                titleContentColor = AppTheme.colorScheme.foreground1,
                headlineContentColor = AppTheme.colorScheme.foreground1,
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .taperedBorder(
                    side = TaperedBorderSide.Top,
                    strokeWidth = 1.dp,
                    color = AppTheme.colorScheme.foreground1.copy(.25f),
                    extensionSize = 150.dp,
                    shape = RoundedCornerShape(34.dp)
                )
                .taperedBorder(
                    side = TaperedBorderSide.Bottom,
                    strokeWidth = 1.dp,
                    color = AppTheme.colorScheme.foreground1.copy(.25f),
                    extensionSize = 90.dp,
                    shape = RoundedCornerShape(24.dp)
                )
        )
    }
}