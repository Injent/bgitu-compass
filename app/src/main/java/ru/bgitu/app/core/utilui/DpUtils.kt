package ru.bgitu.app.core.utilui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val TextUnit.nonScaledSp: TextUnit
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp