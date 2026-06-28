package ru.bgitu.app.core.utilui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues =
    PaddingValues(
        top = this.calculateTopPadding() + other.calculateTopPadding(),
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
        start = this.calculateStartPadding(LayoutDirection.Ltr) + other.calculateStartPadding(LayoutDirection.Ltr),
        end = this.calculateEndPadding(LayoutDirection.Ltr) + other.calculateEndPadding(LayoutDirection.Ltr)
    )

operator fun PaddingValues.minus(other: PaddingValues): PaddingValues =
    PaddingValues(
        top = (this.calculateTopPadding() - other.calculateTopPadding()).coerceAtLeast(0.dp),
        bottom = (this.calculateBottomPadding() - other.calculateBottomPadding()).coerceAtLeast(0.dp),
        start = (this.calculateStartPadding(LayoutDirection.Ltr) - other.calculateStartPadding(LayoutDirection.Ltr)).coerceAtLeast(0.dp),
        end = (this.calculateEndPadding(LayoutDirection.Ltr) - other.calculateEndPadding(LayoutDirection.Ltr)).coerceAtLeast(0.dp)
    )

val LocalExternalPadding = compositionLocalOf { PaddingValues() }