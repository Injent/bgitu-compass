package ru.bgitu.app.feature.widget.glance

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.GlanceId
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.action
import androidx.glance.appwidget.AppWidgetId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.app.MainActivity
import ru.bgitu.app.R
import ru.bgitu.app.core.model.WidgetScheduleState
import ru.bgitu.app.core.util.now
import ru.bgitu.app.feature.widget.WidgetSettingsActivity
import ru.bgitu.app.feature.widget.data.WidgetDataRepository
import ru.bgitu.app.feature.widget.glance.ui.AppScaffold
import ru.bgitu.app.feature.widget.glance.ui.FailedToLoadContent
import ru.bgitu.app.feature.widget.glance.ui.LoadingContent
import ru.bgitu.app.feature.widget.glance.ui.SuccessContent

class CompassWidget : GlanceAppWidget(errorUiLayout = R.layout.widget_error_layout), KoinComponent {

    private val repo by inject<WidgetDataRepository>()

    override val sizeMode: SizeMode = SizeMode.Exact

    @OptIn(FlowPreview::class)
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        @SuppressLint("RestrictedApi")
        val appWidgetId = if (id is AppWidgetId) { id.appWidgetId } else 0
        val initialPrefs = repo.getPrefs(appWidgetId = appWidgetId)

        provideContent {
            val prefs by repo.getPrefsFlow(appWidgetId).collectAsState(initialPrefs)

            CompassWidgetTheme(prefs = prefs) {
                val coroutineScope = rememberCoroutineScope()
                val selectedDate by repo.getSelectedDateForWidgetFlow(appWidgetId = appWidgetId)
                    .collectAsState(initial = LocalDate.now())
                val widgetScheduleState by repo
                    .getScheduleForWidgetFlow(appWidgetId = appWidgetId)
                    .collectAsState(initial = WidgetScheduleState.Loading)

                val openAppAction = remember(prefs.group, selectedDate) {
                    context.openScheduleScreenAction(
                        date = selectedDate,
                        groupId = prefs.group?.id
                    )
                }
                val downloadScheduleAction = remember(prefs.group) {
                    context.openScheduleScreenAction(groupId = prefs.group?.id)
                }

                Content(
                    selectedDate = selectedDate,
                    widgetScheduleState = widgetScheduleState,
                    onNextDateAction = {
                        coroutineScope.launch {
                            repo.selectDateForWidget(
                                appWidgetId = appWidgetId,
                                date = selectedDate.plus(1, DateTimeUnit.DAY)
                            )
                        }
                    },
                    onPreviousDateAction = {
                        coroutineScope.launch {
                            repo.selectDateForWidget(
                                appWidgetId = appWidgetId,
                                date = selectedDate.minus(1, DateTimeUnit.DAY)
                            )
                        }
                    },
                    onTodayDateAction = {
                        coroutineScope.launch {
                            repo.selectDateForWidget(
                                appWidgetId = appWidgetId,
                                date = LocalDate.now()
                            )
                        }
                    },
                    openAppAction = openAppAction,
                    openWidgetSettingsAction = context.openWidgetSettings(appWidgetId),
                    downloadScheduleAction = downloadScheduleAction,
                )
            }
        }
    }

    @Composable
    private fun Content(
        selectedDate: LocalDate,
        widgetScheduleState: WidgetScheduleState,
        onNextDateAction: () -> Unit,
        onPreviousDateAction: () -> Unit,
        onTodayDateAction: () -> Unit,
        openAppAction: Action,
        openWidgetSettingsAction: Action,
        downloadScheduleAction: Action,
    ) {
        val context = LocalContext.current

        AppScaffold(
            selectedDate = selectedDate,
            onNextDateAction = onNextDateAction,
            onTodayDateAction = onTodayDateAction,
            onPreviousDateAction = onPreviousDateAction,
            groupName = (widgetScheduleState as? WidgetScheduleState.Success)?.groupName ?: ""
        ) {
            when (widgetScheduleState) {
                is WidgetScheduleState.GroupNotSelected -> FailedToLoadContent(
                    action = if (widgetScheduleState.noGroupsFound) openAppAction else openWidgetSettingsAction,
                    actionText = context.getString(R.string.feature_widget_select),
                    title = context.getString(R.string.feature_widget_groupNotSelected)
                )
                is WidgetScheduleState.Error -> FailedToLoadContent(
                    action = downloadScheduleAction,
                    actionText = context.getString(R.string.feature_widget_download),
                    title = context.getString(R.string.feature_widget_scheduleIsNotDownloaded)
                )
                is WidgetScheduleState.Loading -> LoadingContent()
                is WidgetScheduleState.Success -> {
                    SuccessContent(
                        lessons = widgetScheduleState.lessons,
                        onStartActivity = openAppAction,
                        lastRefreshed = widgetScheduleState.lastRefreshed
                    )
                }
            }
        }
    }
}

fun Context.openScheduleScreenAction(date: LocalDate? = null, groupId: Int? = null): Action {
    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        data = Uri.Builder()
            .scheme("app")
            .authority("schedule")
            .apply {
                groupId?.let { appendQueryParameter("groupId", it.toString()) }
                date?.let { appendQueryParameter("date", it.toString()) }
            }
            .build()
    }
    return actionStartActivity(intent)
}

private fun Context.openWidgetSettings(appWidgetId: Int): Action {
    val intent = Intent(this, WidgetSettingsActivity::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    return actionStartActivity(intent)
}