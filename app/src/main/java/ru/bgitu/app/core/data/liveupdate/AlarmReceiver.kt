package ru.bgitu.app.core.data.liveupdate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.app.core.datastore.SettingsRepository

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            val settingsRepository = get<SettingsRepository>()
            val prefs = settingsRepository.data.first()
            if (!prefs.liveUpdateNotification) {
                pendingResult.finish()
                return@launch
            }

            if (intent.action == ACTION_DISABLE_FOR_TODAY) {
                LiveUpdateNotificationManager(context).disable(forever = false)
                pendingResult.finish()
                return@launch
            }

            prefs.liveUpdateGroupId?.let { groupId ->
                LiveUpdateNotificationManager(context).updateAndScheduleNext(groupId)
            }
            pendingResult.finish()
        }
    }
}
