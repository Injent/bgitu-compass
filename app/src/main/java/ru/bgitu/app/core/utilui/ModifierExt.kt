package ru.bgitu.app.core.utilui

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

fun Modifier.hazeSourceIfEnabled(state: HazeState, enabled: Boolean): Modifier = if (enabled) {
    this.hazeSource(state)
} else {
    this
}