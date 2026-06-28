package ru.bgitu.app.feature.teachers.schedule.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.YearMonth

data class LessonsGroupedByMonth(
    val yearMonth: YearMonth,
    val days: ImmutableList<LessonsGroupedByDay>
)