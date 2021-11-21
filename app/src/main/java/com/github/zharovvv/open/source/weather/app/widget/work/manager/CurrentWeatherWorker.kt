package com.github.zharovvv.open.source.weather.app.widget.work.manager

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.WidgetWeatherModel
import com.github.zharovvv.open.source.weather.app.repository.WidgetWeatherRepositoryProvider
import com.github.zharovvv.open.source.weather.app.widget.updateWidget

class CurrentWeatherWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val AUTO_UPDATE_WEATHER_UNIQUE_WORK_NAME =
            "com.github.zharovvv.open.source.weather.app.widget.work.manager.CurrentWeatherWorker"
        private const val LOG_TAG = "WorkManagerLifecycle"
    }

    override fun doWork(): Result {
        val widgetWeatherRepository = WidgetWeatherRepositoryProvider.widgetWeatherRepository
        val widgetModelDataState: DataState<WidgetWeatherModel> =
            widgetWeatherRepository.requestDataSync()
        updateWidget(
            context = applicationContext,
            widgetModelDataState = widgetModelDataState,
            appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        )
        return Result.success()
    }
}