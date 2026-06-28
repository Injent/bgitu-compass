package ru.bgitu.app.feature.teachers.schedule.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.yearMonth
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.util.getFullByLocale
import ru.bgitu.app.feature.teachers.schedule.model.LessonsGroupedByDay

@Composable
fun TeacherLessonDayGroup(
    dayGroup: LessonsGroupedByDay,
    dateWidth: Dp,
    showMonth: Boolean,
    index: Int,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    var dateHeightPx by remember { mutableFloatStateOf(0f) }
    var monthHeaderHeightPx by remember { mutableFloatStateOf(0f) }

    Column(modifier = modifier) {
        if (showMonth) {
            val firstLesson = dayGroup.lessons.firstOrNull()
            if (firstLesson?.lessonDate != null) {
                Row(
                    modifier = Modifier.onGloballyPositioned { monthHeaderHeightPx = it.size.height.toFloat() }
                ) {
                    Spacer(Modifier.width(dateWidth))
                    val locale = LocalConfiguration.current.locales[0]

                    GroupStyledText(
                        text = remember(locale, firstLesson.lessonDate.yearMonth) {
                            YearMonth.Format {
                                monthName(MonthNames.getFullByLocale(locale))
                                char(' ')
                                year()
                            }.format(firstLesson.lessonDate.yearMonth).uppercase()
                        }
                    )
                }
            }
        }

        Row {
            Box(
                modifier = Modifier
                    .width(dateWidth)
                    .onGloballyPositioned { dateHeightPx = it.size.height.toFloat() }
                    .graphicsLayer {
                        val currentMonthHeight = if (showMonth) monthHeaderHeightPx else 0f
                        val itemInfo = listState.layoutInfo.visibleItemsInfo.find { it.index == index }

                        if (itemInfo != null) {
                            val offsetRelativeToScreen = itemInfo.offset + currentMonthHeight
                            if (offsetRelativeToScreen < 0) {
                                val maxTranslation = (itemInfo.size - currentMonthHeight - dateHeightPx).coerceAtLeast(0f)
                                translationY = (-offsetRelativeToScreen).coerceAtMost(maxTranslation)
                            } else {
                                translationY = 0f
                            }
                        } else {
                            translationY = 0f
                        }
                    }
            ) {
                val firstLesson = dayGroup.lessons.firstOrNull()
                if (firstLesson?.lessonDate != null) {
                    TeacherLessonDate(date = firstLesson.lessonDate)
                }
            }

            Column {
                dayGroup.lessons.forEachIndexed { lessonIndex, lesson ->
                    if (lessonIndex > 0) {
                        Spacer(Modifier.height(2.dp))
                    }

                    TeacherLessonCard(
                        lesson = lesson,
                        shape = AppCardDefaults.getVerticalListShape(
                            index = lessonIndex,
                            size = dayGroup.lessons.size
                        )
                    )
                }
            }
        }
    }
}
