package ru.bgitu.app.core.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.app.core.data.TeachersRepository
import ru.bgitu.app.core.data.liveupdate.LiveUpdateNotificationManager
import ru.bgitu.app.core.data.notifications.FirebaseNotificationsRepository
import ru.bgitu.app.core.data.remoteconfig.RemoteConfigRepository
import ru.bgitu.app.core.data.schedule.GroupsRepository
import ru.bgitu.app.core.data.schedule.ScheduleRepository
import ru.bgitu.app.core.data.util.AcademicWeekProvider
import ru.bgitu.app.core.data.util.AndroidTimeMonitor
import ru.bgitu.app.core.data.util.NetworkMonitor
import ru.bgitu.app.core.data.util.TimeMonitor
import ru.bgitu.app.core.updates.di.flavoredDataModule

val dataModule = module {
    singleOf(::NetworkMonitor)
    singleOf(::RemoteConfigRepository)
    singleOf(::ScheduleRepository)
    singleOf(::GroupsRepository)
    singleOf(::TeachersRepository)
    singleOf(::FirebaseNotificationsRepository)
    singleOf(::LiveUpdateNotificationManager)
    singleOf(::AcademicWeekProvider)
    singleOf(::AndroidTimeMonitor).bind<TimeMonitor>()

    includes(flavoredDataModule)
}