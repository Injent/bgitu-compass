package ru.bgitu.app.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext

class OneTimeEvent<T> {

    private val value = MutableStateFlow<T?>(null)

    suspend fun consume(onEvent: suspend (event: T) -> Unit) {
        value
            .filterNotNull()
            .collect {
                onEvent(it)
                value.value = null
            }
    }

    fun send(event: T) {
        value.value = event
    }
}

fun OneTimeEvent<Unit>.trigger() {
    send(Unit)
}

@Composable
fun <T> CollectEvent(
    emitter: OneTimeEvent<T>,
    onEvent: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(emitter, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                emitter.consume(onEvent)
            }
        }
    }
}