package com.github.zharovvv.open.source.weather.app.repository

import android.location.Location
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.database.dao.LocationDao
import com.github.zharovvv.open.source.weather.app.database.entity.LocationEntity
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import io.reactivex.Flowable

class LocationRepository {

    private val locationDao: LocationDao = OpenSourceWeatherApp.appDatabase.locationDao()

    fun updateLocation(locationModel: LocationModel) {
        val lastKnownLocationEntity: LocationEntity? = locationDao.getLastKnownLocation()
        val newLocationEntity = LocationEntity(
            id = lastKnownLocationEntity?.id ?: 0,
            latitude = locationModel.latitude,
            longitude = locationModel.longitude,
            cityName = locationModel.cityName,
            countryName = locationModel.countryName
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
        newLocationEntity: LocationEntity
    ): Boolean {
        val oldLatitude = oldLocationEntity.latitude
        val oldLongitude = oldLocationEntity.longitude
        val newLatitude = newLocationEntity.latitude
        val newLongitude = newLocationEntity.longitude
        val floatArray = FloatArray(1)
        Location.distanceBetween(
            oldLatitude.toDouble(),
            oldLongitude.toDouble(),
            newLatitude.toDouble(),
            newLongitude.toDouble(),
            floatArray
        )
        val distance: Float = floatArray[0]
        return distance > 2000f
    }

    fun locationObservable(): Flowable<LocationModel> {
        return locationDao.getLocation()
            .map { locationEntity: LocationEntity ->
                LocationModel(
                    latitude = locationEntity.latitude,
                    longitude = locationEntity.longitude,
                    cityName = locationEntity.cityName,
                    countryName = locationEntity.countryName
                )
            }
            .doOnError { it.printStackTrace() }
    }
}