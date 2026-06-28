package ru.bgitu.app.core.model

import kotlin.time.Instant

sealed interface WidgetScheduleState {
    data class Success(
        val groupName: String,
        val lessons: List<Lesson>,
        val lastRefreshed: Instant
    ) : WidgetScheduleState
    data class GroupNotSelected(val noGroupsFound: Boolean) : WidgetScheduleState
    data object Error : WidgetScheduleState
    data object Loading : WidgetScheduleState
}