package ru.bgitu.app.feature.schedule.component

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.WavyProgressIndicatorDefaults.trackColor
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.common.io.Files.append
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.TimeOrder
import ru.bgitu.app.core.designsystem.theme.AppTheme
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TimeProgressBar(
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    currentTime: LocalDateTime,
    modifier: Modifier = Modifier,
    popupBackgroundColor: Color = AppTheme.colorScheme.background1Container,
    popupTextColor: Color = AppTheme.colorScheme.foreground1
) {
    var remainingTimeEnabled by rememberSaveable { mutableStateOf(true) }
    val timeZone = remember { TimeZone.currentSystemDefault() }

    val totalDuration = remember(startTime, endTime, timeZone) {
        endTime.toInstant(timeZone) - startTime.toInstant(timeZone)
    }
    val elapsedDuration = remember(startTime, currentTime, timeZone, remainingTimeEnabled) {
        currentTime.toInstant(timeZone) - startTime.toInstant(timeZone)
    }
    val remainingDuration = remember(endTime, currentTime, timeZone) {
        endTime.toInstant(timeZone) - currentTime.toInstant(timeZone)
    }

    val progress by remember(totalDuration, elapsedDuration) {
        derivedStateOf {
            if (totalDuration.inWholeMilliseconds > 0) {
                (elapsedDuration.inWholeMilliseconds.toDouble() / totalDuration.inWholeMilliseconds.toDouble())
                    .toFloat()
                    .coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }

    val displayDuration = if (remainingTimeEnabled) remainingDuration else elapsedDuration
    val locale = remember { Locale.getDefault() }
    val measureFormat = remember(locale) { MeasureFormat.getInstance(locale, MeasureFormat.FormatWidth.NARROW) }
    val popupText = remember(displayDuration, measureFormat) {
        val displayDuration = if (displayDuration.isNegative()) Duration.ZERO else displayDuration
        val measures = displayDuration.toComponents { hours, minutes, seconds, _ ->
            buildList {
                if (hours > 0) add(Measure(hours, MeasureUnit.HOUR))
                if (minutes > 0) add(Measure(minutes, MeasureUnit.MINUTE))
                if (seconds > 0) add(Measure(seconds, MeasureUnit.SECOND))
            }
        }
        measureFormat.formatMeasures(*measures.toTypedArray())
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier.fillMaxWidth(),
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp)
        ) {
            val totalWidth = constraints.maxWidth.toFloat()

            LinearWavyProgressIndicator(
                progress = { progress },
                color = AppTheme.colorScheme.brand,
                trackColor = AppTheme.colorScheme.background2,
                waveSpeed = 15.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val textWidth = placeable.width
                        val textHeight = placeable.height
                        val anchorX = totalWidth * progress
                        val idealX = anchorX - (textWidth / 2)
                        val maxAllowedX = totalWidth - textWidth
                        val finalX = idealX.coerceIn(0f, maxAllowedX)

                        layout(textWidth, textHeight) {
                            placeable.placeRelative(
                                x = finalX.roundToInt(),
                                y = -textHeight
                            )
                        }
                    }
            ) {
                Text(
                    text = popupText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = popupTextColor,
                    modifier = Modifier
                        .background(popupBackgroundColor, CircleShape)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        val tint by animateColorAsState(
            if (remainingTimeEnabled) AppTheme.colorScheme.foreground1 else AppTheme.colorScheme.foreground3
        )
        TooltipBox(
            modifier = Modifier,
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                positioning = TooltipAnchorPosition.Above
            ),
            tooltip = {
                PlainTooltip(
                    containerColor = Color.Black.copy(.75f),
                    contentColor = Color.White
                ) {
                    Text(text = stringResource(R.string.feature_schedule_showRemainingTime))
                }
            },
            state = rememberTooltipState()
        ) {
            Icon(
                imageVector = Icons.TimeOrder,
                tint = tint,
                contentDescription = null,
                modifier = Modifier
                    .offset(y = 4.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable {
                        remainingTimeEnabled = !remainingTimeEnabled
                    }
                    .padding(2.dp)
            )
        }
    }
}