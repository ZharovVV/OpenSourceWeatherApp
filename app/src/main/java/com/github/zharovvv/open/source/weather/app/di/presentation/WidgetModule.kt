package com.github.zharovvv.open.source.weather.app.di.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import com.github.zharovvv.open.source.weather.app.di.AppContext
import com.github.zharovvv.open.source.weather.app.di.ApplicationScope
import com.github.zharovvv.open.source.weather.app.presentation.widget.WeatherWidgetManager
import dagger.Module
import dagger.Provides

@Module
class WidgetModule {

    @Provides
    @ApplicationScope
    fun provideWeatherWidgetManager(@AppContext context: Context): WeatherWidgetManager =
        WeatherWidgetManager(
            applicationContext = context,
            appWidgetManager = AppWidgetManager.getInstance(context)
        )
}