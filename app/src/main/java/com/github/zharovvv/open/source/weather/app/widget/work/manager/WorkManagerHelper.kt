package com.github.zharovvv.open.source.weather.app.widget.work.manager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun schedulePeriodicWork(context: Context) {
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

fun cancelPeriodicWork(context: Context) {
    val workManager = WorkManager.getInstance(context)
    workManager.cancelUniqueWork(CurrentWeatherWorker.AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME)
}