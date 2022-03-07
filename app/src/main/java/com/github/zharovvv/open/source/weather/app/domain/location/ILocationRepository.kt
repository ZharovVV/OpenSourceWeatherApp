package com.github.zharovvv.open.source.weather.app.domain.location

import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.Flowable

interface ILocationRepository {

    fun locationObservable(): Flowable<LocationModel>

    fun getLastKnownLocation(): LocationModel?
}