package ru.bgitu.app.feature.widget.glance.ui


import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ru.bgitu.app.R
import ru.bgitu.app.core.util.getAbbreviatedByLocale
import ru.bgitu.app.core.util.inWeekOfOtherDate
import ru.bgitu.app.core.util.now
import ru.bgitu.app.feature.widget.glance.GlanceShape
import ru.bgitu.app.feature.widget.glance.WidgetTheme.widgetPadding
import ru.bgitu.app.feature.widget.glance.backgroundCompat

@Composable
fun AppScaffold(
    groupName: String,
    selectedDate: LocalDate,
    onNextDateAction: () -> Unit,
    onPreviousDateAction: () -> Unit,
    onTodayDateAction: () -> Unit,
    modifier: GlanceModifier = GlanceModifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        titleBar = {
            TopWidgetBar(
                groupName = groupName,
                selectedDate = selectedDate,
                onNextDateAction = onNextDateAction,
                onPreviousDateAction = onPreviousDateAction,
                onTodayDateAction = onTodayDateAction
            )
        },
        modifier = modifier,
        content = content
    )
}

@Composable
private fun TopWidgetBar(
    groupName: String,
    selectedDate: LocalDate,
    onNextDateAction: () -> Unit,
    onPreviousDateAction: () -> Unit,
    onTodayDateAction: () -> Unit,
    modifier: GlanceModifier = GlanceModifier
) {
    val size = LocalSize.current
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0]

    Row(
        modifier = modifier.fillMaxWidth().padding(top = widgetPadding, bottom = 8.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
    ) {
        Spacer(GlanceModifier.width(widgetPadding))
        Row(
            modifier = GlanceModifier
                .defaultWeight()
                .clickable(block = onTodayDateAction, rippleOverride = GlanceShape.BTN_STEPPER_START_RIPPLE.resId)
        ) {
            val today = LocalDate.now()
            val yesterday = today.minus(1, DateTimeUnit.DAY)
            val tomorrow = today.plus(1, DateTimeUnit.DAY)
            val titleText = if (selectedDate in yesterday..tomorrow) {
                when (selectedDate) {
                    yesterday -> context.getString(R.string.feature_widget_yesterday)
                    today -> context.getString(R.string.feature_widget_today)
                    tomorrow -> context.getString(R.string.feature_widget_tomorrow)
                    else -> error("Unreachable statement")
                }
            } else {
                selectedDate.format(
                    LocalDate.Format {
                        dayOfWeek(DayOfWeekNames.getAbbreviatedByLocale(locale))

                        if (selectedDate.inWeekOfOtherDate(today).not()) {
                            chars(", ")
                            day()
                            char(' ')
                            monthName(MonthNames.getAbbreviatedByLocale(locale))
                        }
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier
                    .defaultWeight()
                    .height(40.dp)
            ) {
                if (size.width > 220.dp) {
                    Spacer(GlanceModifier.width(8.dp))
                }

                Text(
                    text = titleText,
                    style = LayoutTextStyles.titleBarText(),
                    maxLines = 1,
                )

                if (size.width > 220.dp) {
                    Text(
                        text = groupName,
                        style = LayoutTextStyles.titleBarText(textAlign = TextAlign.End),
                        maxLines = 1,
                        modifier = GlanceModifier
                            .defaultWeight()
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
        CustomButton(
            icon = ImageProvider(R.drawable.ic_arrow_left),
            onClick = onPreviousDateAction,
            shape = GlanceShape.BTN_STEPPER_START,
            rippleShape = GlanceShape.BTN_STEPPER_START_RIPPLE
        )
        Spacer(GlanceModifier.width(4.dp))
        CustomButton(
            icon = ImageProvider(R.drawable.ic_arrow_right),
            onClick = onNextDateAction,
            shape = GlanceShape.BTN_STEPPER_END,
            rippleShape = GlanceShape.BTN_STEPPER_END_RIPPLE
        )
        Spacer(GlanceModifier.width(widgetPadding))
    }
}

@Composable
private fun CustomButton(
    icon: ImageProvider,
    onClick: () -> Unit,
    shape: GlanceShape,
    rippleShape: GlanceShape,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .backgroundCompat(
                color = GlanceTheme.colors.secondaryContainer.getColor(context),
                shape = shape
            )
            .clickable(block = onClick, rippleOverride = rippleShape.resId)
    ) {
        Image(
            provider = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(GlanceTheme.colors.onSecondaryContainer)
        )
    }
}