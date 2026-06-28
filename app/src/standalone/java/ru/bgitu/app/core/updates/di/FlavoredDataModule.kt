package ru.bgitu.app.core.updates.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.app.core.data.appupdater.AppUpdateManager
import ru.bgitu.app.core.updates.StandaloneUpdateManager

val flavoredDataModule = module {
    singleOf(::StandaloneUpdateManager).bind<AppUpdateManager>()
}