package com.github.zharovvv.open.source.weather.app.domain

import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WeekWeatherModel
import io.reactivex.Observable

interface IWeekWeatherRepository {

    fun observableData(): Observable<DataState<WeekWeatherModel>>

    fun requestData(locationModel: LocationModel, withLoadingStatus: Boolean)

    fun requestDataSync(locationModel: LocationModel): DataState<WeekWeatherModel>
}