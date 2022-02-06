package com.github.zharovvv.open.source.weather.app.domain

import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WeatherTodayModel
import io.reactivex.Observable

interface IWeatherTodayRepository {

    fun observableData(): Observable<DataState<WeatherTodayModel>>

    fun requestData(locationModel: LocationModel, withLoadingStatus: Boolean)

    fun requestDataSync(locationModel: LocationModel): DataState<WeatherTodayModel>
}