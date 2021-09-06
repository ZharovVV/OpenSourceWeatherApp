package com.github.zharovvv.open.source.weather.app.location

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class LocationLiveData(context: Context) : LiveData<LocationModel>() {

    private val contextWeakReference: WeakReference<Context> = WeakReference(context)
    private val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    private val geoCoder = Geocoder(context)
    private val locationListener = LocationListener { location: Location ->
        Log.d("Location_debug", "locationListener#onLocationChanged")
        val latitude = location.latitude
        val longitude = location.longitude
        compositeDisposable += Single.fromCallable {
            geoCoder.getFromLocation(latitude, longitude, 1).first()
        }
            .map { address: Address ->
                LocationModel(
                    latitude = address.latitude.toFloat(),
                    longitude = address.longitude.toFloat(),
                    cityName = address.locality,
                    countryName = address.countryName
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { locationModel: LocationModel ->
                    value = locationModel
                },
                { throwable: Throwable ->
                    Log.d("Location_debug", throwable.message.toString())
                }
            )
    }
    private val compositeDisposable = CompositeDisposable()

    fun requestLocation() {
        val context = contextWeakReference.get()
        if (context != null
            && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?.let { location: Location ->
                    locationListener.onLocationChanged(location)
                }
            Log.d("Location_debug", "locationManager#requestLocationUpdates")
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                3600_000L,
                2000f,
                locationListener
            )
        }
    }

    fun clear() {
        locationManager.removeUpdates(locationListener)
        compositeDisposable.clear()
    }
}