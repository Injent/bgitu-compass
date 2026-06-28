package ru.bgitu.app.core.datastore.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.datastore.migration.MigrationV1
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.datastore.model.UserPrefsSerializer

val dataStoreModule = module {
    single {
        DataStoreFactory.create(
            serializer = UserPrefsSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler {
                UserPrefs()
            },
            migrations = listOf(
                MigrationV1(oldProtoFile = androidContext().dataStoreFile(OLD_DATASTORE_FILE))
            ),
            produceFile = { androidContext().dataStoreFile(DATASTORE_FILE) }
        )
    }

    singleOf(::SettingsRepository)
}

private const val DATASTORE_FILE = "settings.json"
private const val OLD_DATASTORE_FILE = "settings.pb"