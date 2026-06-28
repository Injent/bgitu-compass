package ru.bgitu.app.feature.schedule.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.util.getAcademicWeek
import ru.bgitu.app.core.util.getFullByLocale
import ru.bgitu.app.core.util.isEvenAcademicWeek

@Composable
internal fun MonthWithWeekTitle(
    termStartDate: LocalDate,
    selectedDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val locale = LocalResources.current.configuration.locales[0]
    var previousDate by remember { mutableStateOf(selectedDate) }

    LaunchedEffect(selectedDate) {
        if (previousDate != selectedDate) {
            previousDate = selectedDate
        }
    }

    RollingContent(
        targetState = remember(selectedDate) {
            LocalDate.Format {
                monthName(MonthNames.getFullByLocale(locale))
            }.format(selectedDate)
        },
        rollingDirection = if (selectedDate > previousDate) {
            RollingDirection.UP
        } else {
            RollingDirection.DOWN
        },
        modifier = modifier
    ) { monthName ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = monthName,
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.colorScheme.foreground1,
                softWrap = false,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            RollingContent(
                targetState = remember(selectedDate, termStartDate) {
                    selectedDate.isEvenAcademicWeek(termStartDate)
                },
                rollingDirection = if (selectedDate.getAcademicWeek(termStartDate) > previousDate.getAcademicWeek(termStartDate)) {
                    RollingDirection.UP
                } else {
                    RollingDirection.DOWN
                },
            ) { isEvenWeek ->
                Text(
                    text = stringResource(
                        if (isEvenWeek) {
                            R.string.feature_schedule_evenWeek
                        } else {
                            R.string.feature_schedule_oddWeek
                        }
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.colorScheme.foreground2,
                    softWrap = false,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
            }
        }
    }
}

private enum class RollingDirection {
    UP,
    DOWN
}

@Composable
private fun <S> RollingContent(
    targetState: S,
    rollingDirection: RollingDirection,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        label = "RollingAnimation",
        transitionSpec = {
            val tweenSpec = tween<Float>(easing = FastOutSlowInEasing)
            slideInVertically {
                if (rollingDirection == RollingDirection.DOWN) it
                else -it
            } + fadeIn(tweenSpec) togetherWith slideOutVertically {
                if (rollingDirection == RollingDirection.DOWN) -it
                else it
            } + fadeOut(tweenSpec)
        },
        contentAlignment = Alignment.Center,
        modifier = modifier,
        content = content
    )
}