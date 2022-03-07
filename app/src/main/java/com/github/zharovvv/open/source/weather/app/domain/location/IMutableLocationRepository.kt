package com.github.zharovvv.open.source.weather.app.domain.location

import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel

interface IMutableLocationRepository : ILocationRepository {
    fun updateLocation(locationModel: LocationModel)
}