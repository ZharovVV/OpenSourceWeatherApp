package com.github.zharovvv.open.source.weather.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import com.github.zharovvv.open.source.weather.app.repository.WidgetWeatherRepositoryProvider
import com.github.zharovvv.open.source.weather.app.widget.work.manager.cancelPeriodicWork
import com.github.zharovvv.open.source.weather.app.widget.work.manager.schedulePeriodicWork

class WeatherAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateWidgetImmediately(context!!)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        schedulePeriodicWork(context!!)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        cancelPeriodicWork(context!!)
    }

    private fun updateWidgetImmediately(context: Context) {
        val pendingResult = goAsync()
        Thread {
            val widgetWeatherRepository = WidgetWeatherRepositoryProvider.widgetWeatherRepository
            val data = widgetWeatherRepository.requestDataSync()
            updateWidget(
                context = context,
                widgetModelDataState = data,
                appWidgetManager = AppWidgetManager.getInstance(context)
            )
            pendingResult.finish()
        }.start()
    }
}