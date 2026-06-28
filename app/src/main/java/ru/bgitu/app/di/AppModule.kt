package ru.bgitu.app.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.app.CompassApp
import ru.bgitu.app.MainActivityViewModel
import ru.bgitu.app.core.data.di.dataModule
import ru.bgitu.app.core.datastore.di.dataStoreModule
import ru.bgitu.app.core.network.di.networkModule
import ru.bgitu.app.feature.groups.GroupsSearchViewModel
import ru.bgitu.app.feature.groups.GroupsViewModel
import ru.bgitu.app.feature.news.NewsViewModel
import ru.bgitu.app.feature.schedule.ScheduleViewModel
import ru.bgitu.app.feature.settings.SettingsViewModel
import ru.bgitu.app.feature.teachers.schedule.TeacherScheduleViewModel
import ru.bgitu.app.feature.teachers.search.TeacherSearchViewModel
import ru.bgitu.app.feature.teachers.teachers.TeacherModeScheduleViewModel
import ru.bgitu.app.feature.teachers.teachers.TeachersViewModel
import ru.bgitu.app.feature.update.UpdateViewModel
import ru.bgitu.app.feature.widget.di.widgetModule
import ru.bgitu.app.sync.WorkManagerSyncManager

val appModule = module {
    single { (androidApplication() as CompassApp).applicationScope }

    includes(
        dataStoreModule,
        networkModule,
        dataModule,
        widgetModule
    )
    singleOf(::WorkManagerSyncManager)

    viewModelOf(::MainActivityViewModel)
    viewModelOf(::ScheduleViewModel)
    viewModelOf(::GroupsViewModel)
    viewModelOf(::GroupsSearchViewModel)
    viewModelOf(::TeachersViewModel)
    viewModelOf(::TeacherSearchViewModel)
    viewModelOf(::TeacherScheduleViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::TeacherModeScheduleViewModel)
    viewModelOf(::UpdateViewModel)
    viewModelOf(::NewsViewModel)
}