package com.github.zharovvv.open.source.weather.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.zharovvv.open.source.weather.app.repository.WidgetWeatherRepositoryProvider
import com.github.zharovvv.open.source.weather.app.widget.work.manager.CurrentWeatherWorker
import java.util.concurrent.TimeUnit

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

    private fun schedulePeriodicWork(context: Context) {
        val uniqueWorkName = CurrentWeatherWorker.AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME
        val existingWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE
        val periodicWorkRequest = PeriodicWorkRequestBuilder<CurrentWeatherWorker>(
            repeatInterval = 60L,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = 5L,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        ).build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            existingWorkPolicy,
            periodicWorkRequest
        )
    }

    private fun cancelPeriodicWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(CurrentWeatherWorker.AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME)
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