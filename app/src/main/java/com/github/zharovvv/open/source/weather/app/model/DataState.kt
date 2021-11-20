package com.github.zharovvv.open.source.weather.app.model

import androidx.annotation.DrawableRes
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.R

sealed class DataState<T>(
    open val data: T? = null,
    val message: String? = null
) {

    class Success<T>(override val data: T) : DataState<T>(data)

    class Loading<T>(data: T? = null) : DataState<T>(data)

    class Error<T>(
        @DrawableRes
        val errorIconId: Int,
        val errorTitle: String,
        val errorDescription: String,
        val isBlocking: Boolean = false,
        message: String,
        data: T? = null
    ) : DataState<T>(data, message) {

        companion object {
            fun <T> buildNetworkError(errorMessage: String): Error<T> {
                val appContext = OpenSourceWeatherApp.appContext
                return Error(
                    errorIconId = R.drawable.ic_baseline_wifi_off_24,
                    errorTitle = appContext.getString(R.string.error_title),
                    errorDescription = appContext.getString(R.string.network_error_description),
                    isBlocking = false,
                    message = errorMessage
                )
            }

            fun <T> buildLocationPermissionError(errorMessage: String): Error<T> {
                val appContext = OpenSourceWeatherApp.appContext
                return Error(
                    errorIconId = R.drawable.ic_baseline_location_off_24,
                    errorTitle = appContext.getString(R.string.error_title),
                    errorDescription = appContext.getString(R.string.location_permission_error_description),
                    isBlocking = true,
                    message = errorMessage
                )
            }

            fun <T> buildUnexpectedError(errorMessage: String): Error<T> {
                val appContext = OpenSourceWeatherApp.appContext
                return Error(
                    errorIconId = R.drawable.ic_baseline_error_outline_24,
                    errorTitle = appContext.getString(R.string.error_title),
                    errorDescription = appContext.getString(R.string.unexpected_error_description),
                    isBlocking = false,
                    message = errorMessage
                )
            }

            fun <T> buildWidgetError(errorTitle: String): Error<T> {
                return Error(
                    errorIconId = 0,
                    errorTitle = errorTitle,
                    errorDescription = "",
                    isBlocking = false,
                    message = errorTitle
                )
            }
        }
    }
}
