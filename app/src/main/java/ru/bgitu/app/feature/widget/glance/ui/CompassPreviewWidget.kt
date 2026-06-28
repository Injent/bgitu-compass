package ru.bgitu.app.feature.widget.glance.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.glance.GlanceId
import androidx.glance.LocalContext
import androidx.glance.LocalState
import androidx.glance.action.action
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.bgitu.app.R
import ru.bgitu.app.core.model.Lesson
import ru.bgitu.app.core.model.WidgetPrefs
import ru.bgitu.app.core.util.now
import ru.bgitu.app.feature.widget.glance.CompassWidgetTheme
import kotlin.time.Clock

class CompassPreviewWidget : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            val prefs = LocalState.current as? WidgetPrefs ?: WidgetPrefs()

            CompassWidgetTheme(prefs) {
                Content(prefs = prefs)
            }
        }
    }

    @Composable
    private fun Content(prefs: WidgetPrefs) {
        val context = LocalContext.current

        AppScaffold(
            selectedDate = LocalDate.now(),
            onNextDateAction = {},
            onPreviousDateAction = {},
            onTodayDateAction = {},
            groupName = prefs.group?.name ?: ""
        ) {
            val lessons = remember { getDemoLessons(context) }

            SuccessContent(
                lessons = lessons,
                onStartActivity = action {},
                lastRefreshed = remember { Clock.System.now() }
            )
        }
    }
}

private fun getDemoLessons(context: Context): List<Lesson> = buildList {
    val subjects = context.resources.getStringArray(R.array.feature_widget_demo_subjects)
    val buildings = context.resources.getStringArray(R.array.feature_widget_demo_buildings)
    val classrooms = context.resources.getStringArray(R.array.feature_widget_demo_classrooms)
    repeat(3) { index ->
        Lesson(
            subjectName = subjects[index],
            building = buildings[index],
            classroom = classrooms[index],
            startAt = when (index) {
                0 -> LocalTime(8, 20, 0)
                1 -> LocalTime(10, 5, 0)
                else -> LocalTime(12, 20, 0)
            },
            endAt = when (index) {
                0 -> LocalTime(9, 55, 0)
                1 -> LocalTime(11, 40, 0)
                else -> LocalTime(13, 55, 0)
            },
            teacher = "",
            isLecture = index != 1,
            teacherFullName = null
        ).let(::add)
    }
}
