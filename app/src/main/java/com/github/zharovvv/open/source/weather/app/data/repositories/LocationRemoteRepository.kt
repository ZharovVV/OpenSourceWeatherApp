package com.github.zharovvv.open.source.weather.app.data.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.core.content.ContextCompat
import com.github.zharovvv.open.source.weather.app.domain.location.ILocationRemoteRepository
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

class LocationRemoteRepository(
    private val locationManager: LocationManager,
    private val geocoder: Geocoder,
    private val applicationContext: Context,
) : ILocationRemoteRepository {

    override fun requestRealLocation(): Observable<LocationModel> {
        return createObservable()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .map { location ->
                val latitude = location.latitude
                val longitude = location.longitude
                geocoder.getFromLocation(latitude, longitude, 1)
                    .map { address: Address ->
                        LocationModel(
                            latitude = latitude.toFloat(),
                            longitude = longitude.toFloat(),
                            cityName = address.locality,
                            countryName = address.countryName,
                            isRealLocation = true
                        )
                    }.first()
            }
    }

    override fun findLocationByName(
        locationName: String,
        maxResults: Int,
    ): Single<List<LocationModel>> {
        return Single.fromCallable {
            //TODO использовать вместо гео-кодера другой инструмент
            //Place Autocomplete??
            geocoder.getFromLocationName(locationName, maxResults)
                .map { address ->
                    LocationModel(
                        latitude = address.latitude.toFloat(),
                        longitude = address.longitude.toFloat(),
                        cityName = address.locality,
                        countryName = address.countryName,
                        isRealLocation = false
                    )
                }
        }.subscribeOn(Schedulers.io())
    }

    private fun checkLocationPermission() {
        if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            throw IllegalStateException("Has no ACCESS_COARSE_LOCATION permission!")
        }
    }

    private fun createObservable(): Observable<Location> {
        checkLocationPermission()
        return Observable.create { emitter ->
            val locationListener = LocationListener { location: Location ->
                if (!emitter.isDisposed) {
                    emitter.onNext(location)
                }
            }
            emitter.setDisposable(Disposables.fromAction {
                locationManager.removeUpdates(locationListener)
            })
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let {
                locationListener.onLocationChanged(it)
            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                600_000L,
                2000f,
                locationListener
            )
        }
    }
}