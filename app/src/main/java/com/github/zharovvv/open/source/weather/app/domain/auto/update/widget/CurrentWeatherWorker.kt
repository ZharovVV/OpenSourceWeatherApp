package com.github.zharovvv.open.source.weather.app.domain.auto.update.widget

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.zharovvv.open.source.weather.app.appComponent
import com.github.zharovvv.open.source.weather.app.logger.Logger
import com.github.zharovvv.open.source.weather.app.presentation.widget.WeatherWidgetManager
import javax.inject.Inject

class CurrentWeatherWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME =
            "com.github.zharovvv.open.source.weather.app.widget.work.manager.CurrentWeatherWorker"  //TODO Оставляем старое название (на всякий случай, не известно как себя поведет WorkManager)
        private const val LOG_TAG = "WorkManagerLifecycle"
    }

    init {
        Logger.i(LOG_TAG, "CurrentWeatherWorker#<init>")
    }

    @Inject
    internal lateinit var weatherWidgetManager: WeatherWidgetManager

    @Inject
    internal lateinit var widgetWeatherInteractor: WidgetWeatherInteractor

    override fun doWork(): Result {
        applicationContext.appComponent.inject(this)
        Logger.i(LOG_TAG, "CurrentWeatherWorker#doWork")
        weatherWidgetManager.updateWidget(
            widgetModelDataState = widgetWeatherInteractor.requestDataSync()
        )
        return Result.success()
    }
}