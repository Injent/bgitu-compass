package ru.bgitu.app.feature.widget.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.bgitu.app.feature.widget.data.WidgetDataRepository

val widgetModule = module {
    singleOf(::WidgetDataRepository)
}