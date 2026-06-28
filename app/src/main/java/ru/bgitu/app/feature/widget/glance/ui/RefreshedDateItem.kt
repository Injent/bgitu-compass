package ru.bgitu.app.feature.widget.glance.ui

import android.R.attr.onClick
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import ru.bgitu.app.R
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Instant

@Composable
fun RefreshedDateItem(
    lastRefreshed: Instant,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current
    val timeZone = remember { TimeZone.currentSystemDefault() }

    Row(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val formatedDate = remember(lastRefreshed) {
            FullDateFormat.format(lastRefreshed.toLocalDateTime(timeZone).toJavaLocalDateTime())
        }
        Text(
            text = context.getString(R.string.feature_widget_refreshed, formatedDate),
            style = LayoutTextStyles.supportingText(textAlign = TextAlign.Center)
        )
    }
}

private val FullDateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)