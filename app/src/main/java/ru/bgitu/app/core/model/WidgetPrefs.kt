package ru.bgitu.app.core.model

import kotlinx.datetime.LocalDate
import ru.bgitu.app.core.util.now

data class WidgetPrefs(
    val group: Group? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val alpha: Float = 1f,
    val uiTheme: WidgetTheme = WidgetTheme.AUTO,
)
