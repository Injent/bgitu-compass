package ru.bgitu.app.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import ru.bgitu.app.core.data.liveupdate.LiveUpdateNotificationManager
import ru.bgitu.app.core.data.notifications.FirebaseNotificationsRepository
import ru.bgitu.app.core.data.remoteconfig.RemoteConfigRepository
import ru.bgitu.app.core.data.schedule.ScheduleRepository

class SyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val groupId = inputData.getInt(GROUP_ID, -1).takeIf { it != -1 }
        val scheduleRepository = get<ScheduleRepository>()
        val remoteConfigRepository = get<RemoteConfigRepository>()
        val notificationsRepository = get<FirebaseNotificationsRepository>()

        val success = awaitAll(
            async { remoteConfigRepository.fetch() },
            async { scheduleRepository.sync(groupId = groupId) },
            async { notificationsRepository.sync() },

        ).all { it }

        when {
            success -> Result.success()
            runAttemptCount < 2 -> Result.retry()
            else -> Result.failure()
        }
    }

    companion object {
        const val NAME = "SyncWork"
        const val GROUP_ID = "group_id"

        fun start(groupId: Int? = null) = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(workDataOf(GROUP_ID to groupId))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
    }
}