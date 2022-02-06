package com.github.zharovvv.open.source.weather.app.domain

import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.Observable

interface IHourlyWeatherRepository {

    fun observableData(): Observable<DataState<HourlyWeatherModel>>

    fun requestData(locationModel: LocationModel, withLoadingStatus: Boolean)

    fun requestDataSync(locationModel: LocationModel): DataState<HourlyWeatherModel>
}