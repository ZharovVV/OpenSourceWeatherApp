package com.github.zharovvv.open.source.weather.app.model

sealed class DataState<T>(open val data: T? = null, val message: String? = null) {
    class Success<T>(override val data: T) : DataState<T>(data)
    class Loading<T>(data: T? = null) : DataState<T>(data)
    class Error<T>(message: String, data: T? = null) : DataState<T>(data, message)
}
