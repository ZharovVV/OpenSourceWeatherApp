package com.github.zharovvv.open.source.weather.app.presentation.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.github.zharovvv.open.source.weather.app.appComponent
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WidgetWeatherInteractor
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WorkManagerGateway
import javax.inject.Inject

class WeatherAppWidgetProvider : AppWidgetProvider() {

    @Inject
    internal lateinit var widgetWeatherInteractor: WidgetWeatherInteractor

    @Inject
    internal lateinit var workManagerGateway: WorkManagerGateway

    @Inject
    internal lateinit var weatherWidgetManager: WeatherWidgetManager

    override fun onReceive(context: Context?, intent: Intent?) {
        requireNotNull(context).appComponent.inject(this)
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateWidgetImmediately()
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        workManagerGateway.schedulePeriodicWeatherWidgetUpdate()
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        workManagerGateway.cancelPeriodicWeatherWidgetUpdate()
    }

    private fun updateWidgetImmediately() {
        val pendingResult = goAsync()
        Thread {
            val data = widgetWeatherInteractor.requestDataSync()
            weatherWidgetManager.updateWidget(widgetModelDataState = data)
            pendingResult.finish()
        }.start()
    }
}