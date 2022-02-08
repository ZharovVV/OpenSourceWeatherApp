package com.github.zharovvv.open.source.weather.app.di.presentation

import dagger.Module

@Module(
    includes = [
        ViewModelModule::class,
        WidgetModule::class
    ]
)
interface OpenSourceWeatherAppPresentationModule

