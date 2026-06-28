package ru.bgitu.app.feature.widget.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.app.core.util.now
import ru.bgitu.app.feature.widget.data.WidgetDataRepository

class CompassWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {

    override val glanceAppWidget: GlanceAppWidget = CompassWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        CoroutineScope(Dispatchers.IO).launch {
            val widgetDataRepository = get<WidgetDataRepository>()
            val glanceManager = GlanceAppWidgetManager(context)
            val compassWidget = CompassWidget()
            appWidgetIds.map { it to glanceManager.getGlanceIdBy(it) }.forEach { (appWidgetId, glanceId) ->
                widgetDataRepository.selectDateForWidget(appWidgetId, LocalDate.now())
                compassWidget.update(context, glanceId)
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        CoroutineScope(Dispatchers.IO).launch {
            val widgetDataRepository = get<WidgetDataRepository>()
            appWidgetIds.forEach { appWidgetId ->
                widgetDataRepository.deletePrefs(appWidgetId)
            }
        }
    }
}