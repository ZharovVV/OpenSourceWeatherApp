package com.github.zharovvv.open.source.weather.app.domain

import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.Flowable

interface ILocationRepository {

    fun updateLocation(locationModel: LocationModel)

    fun locationObservable(): Flowable<LocationModel>

    fun getLastKnownLocation(): LocationModel?
}