package com.github.zharovvv.open.source.weather.app.domain.auto.update.widget

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.zharovvv.open.source.weather.app.presentation.settings.PreferencesKeyProvider
import java.util.concurrent.TimeUnit

class WorkManagerGateway(
    private val applicationContext: Context,
    private val workManager: WorkManager
) {

    fun schedulePeriodicWeatherWidgetUpdate() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val repeatIntervalInHours =
            sharedPreferences.getString(PreferencesKeyProvider.preferenceAutoUpdateKey, "1")
        val uniqueWorkName = CurrentWeatherWorker.AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME
        val existingWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE
        val periodicWorkRequest = PeriodicWorkRequestBuilder<CurrentWeatherWorker>(
            repeatInterval = repeatIntervalInHours!!.toLong() * 60L,
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