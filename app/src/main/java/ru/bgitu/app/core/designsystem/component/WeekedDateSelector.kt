package ru.bgitu.app.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DayOfWeekNames
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.designsystem.theme.CompassTheme
import ru.bgitu.app.core.util.getAbbreviatedByLocale
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.observeTime
import ru.bgitu.app.core.utilui.taperedBorder
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeekedDateSelector(
    dates: List<LocalDate>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    showGlance: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()
    val today by observeTime { 1.minutes }

    ElasticRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
    ) {
        for (date in dates) {
            val isSelected = date == selectedDate
            val scaleX = remember { Animatable(1f) }
            val cornerRounding by animateDpAsState(
                if (isSelected) 14.dp else 16.dp,
                animationSpec = spring(Spring.DampingRatioMediumBouncy)
            )

            DateItem(
                date = date,
                selected = isSelected,
                isToday = date == today.date,
                onClick = {
                    onDateSelected(date)
                    coroutineScope.launch(NonCancellable) {
                        scaleX.animateTo(
                            1.2f,
                            animationSpec = spring(stiffness = Spring.StiffnessHigh)
                        )
                        scaleX.animateTo(
                            1f,
                            animationSpec = spring(
                                dampingRatio = 0.4f,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(cornerRounding),
                showGlance = showGlance,
                modifier = Modifier
                    .elasticWeight { 1f * scaleX.value }
            )
        }
    }
}

@Composable
private fun DateItem(
    date: LocalDate,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showGlance: Boolean = false,
    isToday: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    val locale = LocalResources.current.configuration.locales[0]
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.brand
        } else {
            AppTheme.colorScheme.background1
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.onBrand
        } else {
            AppTheme.colorScheme.foreground1
        },
        label = "contentColor"
    )
    val glanceColorAlpha by animateFloatAsState(
        if (showGlance) 0.25f else 0f
    )

    Surface(
        modifier = modifier
            .defaultMinSize(minWidth = 56.dp)
            .taperedBorder(
                side = TaperedBorderSide.Bottom,
                strokeWidth = 1.dp,
                color = AppTheme.colorScheme.foreground1.copy(glanceColorAlpha),
                shape = shape
            )
            .drawWithContent {
                drawContent()
                if (isToday) {
                    drawLine(
                        color = contentColor,
                        start = Offset(
                            x = size.center.x - 6.dp.toPx(),
                            y = size.height - 8.dp.toPx()
                        ),
                        end = Offset(
                            x = size.center.x + 6.dp.toPx(),
                            y = size.height - 8.dp.toPx()
                        ),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            },
        shape = shape,
        color = containerColor,
        onClick = onClick,
        contentColor = contentColor,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = date.day.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                color = contentColor
            )
            Text(
                text = remember(locale, date) {
                    LocalDate.Format {
                        dayOfWeek(DayOfWeekNames.getAbbreviatedByLocale(locale))
                    }.format(date)
                },
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(widthDp = 450)
@Composable
private fun WeekedDateSelectorPreview() {
    val dates = remember {
        List(6) { LocalDate(2026, 1, 12 + it) }
    }
    var selected by remember { mutableStateOf(dates[0]) }

    CompassTheme {
        Column(
            modifier = Modifier
                .background(AppTheme.colorScheme.background2)
                .padding(16.dp)
        ) {
            WeekedDateSelector(
                dates = dates,
                selectedDate = selected,
                onDateSelected = { selected = it }
            )
        }
    }
}