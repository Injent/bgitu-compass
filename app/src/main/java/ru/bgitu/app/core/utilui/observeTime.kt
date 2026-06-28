package ru.bgitu.app.core.utilui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import ru.bgitu.app.core.data.util.TimeMonitor
import kotlin.time.Clock
import kotlin.time.Duration

@Composable
fun observeTime(interval: (LocalDateTime) -> Duration): State<LocalDateTime> {
    val timeInterval by rememberUpdatedState(interval)
    val timeZone = remember { TimeZone.currentSystemDefault() }
    val timeMonitor = koinInject<TimeMonitor>()

    LifecycleResumeEffect(timeMonitor) {
        timeMonitor.refresh()
        onPauseOrDispose {}
    }

    val timeFlow = remember(timeZone, timeInterval, timeMonitor) {
        timeMonitor
            .monitor { now -> timeInterval(now.toLocalDateTime(timeZone)) }
            .mapLatest { now -> now.toLocalDateTime(timeZone) }
    }
    return timeFlow.collectAsStateWithLifecycle(Clock.System.now().toLocalDateTime(timeZone))
}