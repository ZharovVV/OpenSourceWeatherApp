package com.github.zharovvv.open.source.weather.app.widget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.WidgetWeatherModel
import com.github.zharovvv.open.source.weather.app.ui.MainActivity

fun updateWidget(
    context: Context,
    widgetModelDataState: DataState<WidgetWeatherModel>,
    appWidgetManager: AppWidgetManager,
) {
    val widgetRemoteViews = RemoteViews(context.packageName, R.layout.weather_widget)
    when (widgetModelDataState) {
        is DataState.Success -> {
            val widgetWeatherModel = widgetModelDataState.data
            widgetRemoteViews.setViewVisibility(R.id.weather_icon_widget_image_view, View.VISIBLE)
            widgetRemoteViews.setViewVisibility(R.id.location_icon_widget_image_view, View.VISIBLE)
            widgetRemoteViews.setImageViewResource(
                R.id.weather_icon_widget_image_view,
                widgetWeatherModel.weatherIconId
            )
            widgetRemoteViews.setTextViewText(
                R.id.weather_temperature_widget_text_view,
                widgetWeatherModel.temperature
            )
            widgetRemoteViews.setTextViewText(
                R.id.location_description_widget_text_view,
                widgetWeatherModel.locationDescription
            )
            widgetRemoteViews.setTextViewText(
                R.id.forecast_date_widget_text_view, widgetWeatherModel.forecastDateString
            )
        }
        is DataState.Error -> {
            widgetRemoteViews.setTextViewText(
                R.id.weather_temperature_widget_text_view,//TODO Отрисовка ошибок в виджете
                widgetModelDataState.errorTitle
            )
        }
        is DataState.Loading -> return
    }
    val startActivityPendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )
    widgetRemoteViews.setOnClickPendingIntent(R.id.widget_layout, startActivityPendingIntent)
    val widgetIds = appWidgetManager.getAppWidgetIds(
        ComponentName(
            context,
            WeatherAppWidgetProvider::class.java
        )
    )
    appWidgetManager.updateAppWidget(widgetIds, widgetRemoteViews)
}