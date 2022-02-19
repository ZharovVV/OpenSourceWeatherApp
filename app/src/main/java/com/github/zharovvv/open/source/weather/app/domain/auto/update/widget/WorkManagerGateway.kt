package com.github.zharovvv.open.source.weather.app.domain.auto.update.widget

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import java.util.concurrent.TimeUnit

class WorkManagerGateway(
    private val workManager: WorkManager,
    private val preferencesRepository: IPreferencesRepository,
) {

    fun schedulePeriodicWeatherWidgetUpdate() {
        val repeatIntervalInHours = preferencesRepository.requestAutoUpdatePreference().value
        val uniqueWorkName = CurrentWeatherWorker.AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME
        val existingWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE
        val periodicWorkRequest = PeriodicWorkRequestBuilder<CurrentWeatherWorker>(
            repeatInterval = repeatIntervalInHours.toLong() * 60L,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = 5L,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        ).build()
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            existingWorkPolicy,
            periodicWorkRequest
        )
    }

    fun cancelPeriodicWeatherWidgetUpdate() {
        workManager.cancelUniqueWork(CurrentWeatherWorker.AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME)
    }
}