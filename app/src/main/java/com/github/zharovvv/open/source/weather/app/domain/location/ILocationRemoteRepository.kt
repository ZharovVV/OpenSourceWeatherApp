package com.github.zharovvv.open.source.weather.app.domain.location

import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.Observable
import io.reactivex.Single

interface ILocationRemoteRepository {

    fun requestRealLocation(): Observable<LocationModel>

    fun findLocationByName(locationName: String, maxResults: Int = 5): Single<List<LocationModel>>
}