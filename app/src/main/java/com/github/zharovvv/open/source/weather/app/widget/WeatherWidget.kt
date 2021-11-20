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
    val startActivityPendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )
    val widgetRemoteViews = when (widgetModelDataState) {
        is DataState.Success -> {
            buildSuccessRemoteViews(
                context = context,
                widgetModelDataStateSuccess = widgetModelDataState,
                startActivityPendingIntent = startActivityPendingIntent
            )
        }
        is DataState.Error -> {
            buildErrorRemoteViews(
                context = context,
                widgetModelDataStateError = widgetModelDataState,
                startActivityPendingIntent = startActivityPendingIntent
            )
        }
        is DataState.Loading -> return
    }
    val widgetIds = appWidgetManager.getAppWidgetIds(
        ComponentName(
            context,
            WeatherAppWidgetProvider::class.java
        )
    )
    appWidgetManager.updateAppWidget(widgetIds, widgetRemoteViews)
}

private fun buildSuccessRemoteViews(
    context: Context,
    widgetModelDataStateSuccess: DataState.Success<WidgetWeatherModel>,
    startActivityPendingIntent: PendingIntent
): RemoteViews {
    val widgetWeatherModel = widgetModelDataStateSuccess.data
    val widgetRemoteViews = RemoteViews(context.packageName, R.layout.weather_widget)
    with(widgetRemoteViews) {
        setViewVisibility(R.id.weather_icon_widget_image_view, View.VISIBLE)
        setViewVisibility(R.id.location_icon_widget_image_view, View.VISIBLE)
        setImageViewResource(
            R.id.weather_icon_widget_image_view,
            widgetWeatherModel.weatherIconId
        )
        setTextViewText(
            R.id.weather_temperature_widget_text_view,
            widgetWeatherModel.temperature
        )
        setTextViewText(
            R.id.location_description_widget_text_view,
            widgetWeatherModel.locationDescription
        )
        setTextViewText(
            R.id.forecast_date_widget_text_view, widgetWeatherModel.forecastDateString
        )
        setOnClickPendingIntent(R.id.widget_layout, startActivityPendingIntent)
    }
    return widgetRemoteViews
}

private fun buildErrorRemoteViews(
    context: Context,
    widgetModelDataStateError: DataState.Error<WidgetWeatherModel>,
    startActivityPendingIntent: PendingIntent
): RemoteViews {
    val widgetRemoteViews = RemoteViews(context.packageName, R.layout.weather_error_widget)
    with(widgetRemoteViews) {
        setTextViewText(R.id.error_title_widget_text_view, widgetModelDataStateError.errorTitle)
        setOnClickPendingIntent(R.id.widget_error_layout, startActivityPendingIntent)
    }
    return widgetRemoteViews
}

fun AppWidgetManager.hasAnyWidget(context: Context): Boolean {
    val widgetIds = getAppWidgetIds(
        ComponentName(
            context,
            WeatherAppWidgetProvider::class.java
        )
    )
    return widgetIds != null && widgetIds.isNotEmpty()
}