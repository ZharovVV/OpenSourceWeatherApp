package com.github.zharovvv.open.source.weather.app.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel

fun updateWidget(
    context: Context,
    modelDataState: DataState<WeatherTodayModel>,
    appWidgetManager: AppWidgetManager,
) {
    val widgetRemoteViews = RemoteViews(context.packageName, R.layout.weather_widget)
    when (modelDataState) {
        is DataState.Success -> {
            val weatherModel = modelDataState.data
            widgetRemoteViews.setImageViewResource(
                R.id.weather_icon_widget_image_view,
                weatherModel.iconId
            )
            widgetRemoteViews.setTextViewText(
                R.id.weather_temperature_widget_text_view,
                weatherModel.temperature
            )
            widgetRemoteViews.setTextViewText(
                R.id.forecast_date_widget_text_view, weatherModel.shortDateString
            )
        }
        else -> {
            val errorText = modelDataState.message
            errorText?.let {
                widgetRemoteViews.setTextViewText(R.id.weather_temperature_widget_text_view, it)
            }
        }
    }
    val widgetIds = appWidgetManager.getAppWidgetIds(
        ComponentName(
            context,
            WeatherAppWidgetProvider::class.java
        )
    )
    appWidgetManager.updateAppWidget(widgetIds, widgetRemoteViews)
}