package ru.bgitu.app.sync

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

class WorkManagerSyncManager(
    context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun requestSync(groupId: Int? = null) {
        workManager.enqueueUniqueWork(
            SyncWorker.NAME,
            ExistingWorkPolicy.KEEP,
            SyncWorker.start(groupId = groupId)
        )
    }
}