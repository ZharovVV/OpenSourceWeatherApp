package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.data.local.LocationDao
import com.github.zharovvv.open.source.weather.app.models.data.local.LocationEntity
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.util.distanceBetween
import io.reactivex.Flowable

class LocationLocalRepository(
    private val locationDao: LocationDao,
) {

    fun updateLocation(locationModel: LocationModel) {
        val lastKnownLocationEntity: LocationEntity? = locationDao.getLastKnownLocation()
        val newLocationEntity = LocationEntity(
            id = lastKnownLocationEntity?.id ?: 0,
            latitude = locationModel.latitude,
            longitude = locationModel.longitude,
            cityName = locationModel.cityName,
            countryName = locationModel.countryName,
            isRealLocation = locationModel.isRealLocation
        )
        if (lastKnownLocationEntity != null) {
            if (shouldUpdateLocationEntity(
                    oldLocationEntity = lastKnownLocationEntity,
                    newLocationEntity = newLocationEntity
                )
            ) {
                locationDao.updateLocation(newLocationEntity)
            }
        } else {
            locationDao.insertLocation(newLocationEntity)
        }
    }

    private fun shouldUpdateLocationEntity(
        oldLocationEntity: LocationEntity,
        newLocationEntity: LocationEntity,
    ): Boolean {
        val oldLatitude = oldLocationEntity.latitude
        val oldLongitude = oldLocationEntity.longitude
        val newLatitude = newLocationEntity.latitude
        val newLongitude = newLocationEntity.longitude
        val distance: Float = distanceBetween(oldLatitude, oldLongitude, newLatitude, newLongitude)
        return distance > 2000f
    }

    fun locationObservable(): Flowable<LocationModel> {
        return locationDao.getLocation()
            .map { locationEntity: LocationEntity ->
                LocationModel(
                    latitude = locationEntity.latitude,
                    longitude = locationEntity.longitude,
                    cityName = locationEntity.cityName,
                    countryName = locationEntity.countryName,
                    isRealLocation = locationEntity.isRealLocation
                )
            }
            .doOnError { it.printStackTrace() }
    }

    fun getLastKnownLocation(): LocationModel? {
        return locationDao.getLastKnownLocation()?.let { locationEntity: LocationEntity ->
            LocationModel(
                latitude = locationEntity.latitude,
                longitude = locationEntity.longitude,
                cityName = locationEntity.cityName,
                countryName = locationEntity.countryName,
                isRealLocation = locationEntity.isRealLocation
            )
        }
    }
}