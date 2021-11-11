package com.github.zharovvv.open.source.weather.app.widget.work.manager

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.repository.LocationRepositoryProvider
import com.github.zharovvv.open.source.weather.app.repository.WeatherTodayRepositoryProvider
import com.github.zharovvv.open.source.weather.app.widget.updateWidget

class CurrentWeatherWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME =
            "com.github.zharovvv.open.source.weather.app.widget.work.manager.CurrentWeatherWorker"
        private const val LOG_TAG = "WorkManagerLifecycle"
    }

    init {
        Log.i(LOG_TAG, "CurrentWeatherWorker#<init>")
    }

    override fun doWork(): Result {
        Log.i(LOG_TAG, "CurrentWeatherWorker#doWork")
        val locationRepository = LocationRepositoryProvider.locationRepository
        val weatherRepository = WeatherTodayRepositoryProvider.weatherRepository
        val locationModel: LocationModel? = locationRepository.getLastKnownLocation()
        val result: DataState<WeatherTodayModel> =
            if (locationModel != null) {
                weatherRepository.requestDataSync(locationModel.latitude, locationModel.longitude)
            } else {
                DataState.Error("Для определения местоположения зайдите в приложение")
            }
        updateWidget(
            context = applicationContext,
            modelDataState = result,
            AppWidgetManager.getInstance(applicationContext)
        )
        return Result.success()
    }
}