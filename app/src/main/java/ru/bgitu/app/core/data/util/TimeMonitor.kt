package ru.bgitu.app.core.data.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

interface TimeMonitor {

    fun monitor(interval: (Instant) -> Duration): Flow<Instant>

    fun refresh()
}

class AndroidTimeMonitor(
    private val context: Context,
) : TimeMonitor {

    private val timeProvider: Clock = Clock.System

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val resetSignal = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override fun monitor(interval: (Instant) -> Duration): Flow<Instant> {
        return flow {
            emitAll(produceTicks(interval))
        }
            .shareIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                replay = 1
            )
    }

    private fun produceTicks(intervalProvider: (Instant) -> Duration): Flow<Instant> = channelFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                resetSignal.tryEmit(Unit)
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        context.registerReceiver(receiver, filter)

        try {
            while (currentCoroutineContext().isActive) {
                val now = timeProvider.now()
                send(now)
                val nextDelay = intervalProvider(now)
                withTimeoutOrNull(nextDelay) { resetSignal.first() }
            }
        } finally {
            channel.close()
        }

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (_: IllegalArgumentException) {
            }
        }
    }.distinctUntilChanged()

    override fun refresh() {
        resetSignal.tryEmit(Unit)
    }
}