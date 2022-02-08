package com.github.zharovvv.open.source.weather.app.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.WidgetWeatherModel
import com.github.zharovvv.open.source.weather.app.presentation.MainActivity

class WeatherWidgetManager(
    private val applicationContext: Context,
    private val appWidgetManager: AppWidgetManager
) {

    private val startActivityPendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun hasAnyWidget(): Boolean {
        val widgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                applicationContext,
                WeatherAppWidgetProvider::class.java
            )
        )
        return widgetIds != null && widgetIds.isNotEmpty()
    }

    fun updateWidget(widgetModelDataState: DataState<WidgetWeatherModel>) {
        val widgetRemoteViews = when (widgetModelDataState) {
            is DataState.Success -> {
                buildSuccessRemoteViews(
                    widgetModelDataStateSuccess = widgetModelDataState,
                )
            }
            is DataState.Error -> {
                buildErrorRemoteViews(
                    widgetModelDataStateError = widgetModelDataState,
                )
            }
            is DataState.Loading -> return
        }
        val widgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                applicationContext,
                WeatherAppWidgetProvider::class.java
            )
        )
        appWidgetManager.updateAppWidget(widgetIds, widgetRemoteViews)
    }

    private fun buildSuccessRemoteViews(
        widgetModelDataStateSuccess: DataState.Success<WidgetWeatherModel>,
    ): RemoteViews {
        val widgetWeatherModel = widgetModelDataStateSuccess.data
        val widgetRemoteViews = RemoteViews(applicationContext.packageName, R.layout.weather_widget)
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
        widgetModelDataStateError: DataState.Error<WidgetWeatherModel>,
    ): RemoteViews {
        val widgetRemoteViews =
            RemoteViews(applicationContext.packageName, R.layout.weather_error_widget)
        with(widgetRemoteViews) {
            setTextViewText(R.id.error_title_widget_text_view, widgetModelDataStateError.errorTitle)
            setOnClickPendingIntent(R.id.widget_error_layout, startActivityPendingIntent)
        }
        return widgetRemoteViews
    }
}