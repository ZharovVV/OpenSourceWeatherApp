package com.github.zharovvv.open.source.weather.app.location

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.repository.LocationRepository
import com.github.zharovvv.open.source.weather.app.repository.LocationRepositoryProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class LocationService : Service() {

    private lateinit var locationManager: LocationManager
    private lateinit var geoCoder: Geocoder
    private var _locationListener: LocationListener? = null
    private val locationListener: LocationListener get() = _locationListener!!

    private val locationRepository: LocationRepository =
        LocationRepositoryProvider.locationRepository
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        Log.i("ServiceLifecycle", "$this#onCreate;")
        super.onCreate()
        locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        geoCoder = Geocoder(applicationContext)
        _locationListener = initLocationListener()
    }

    private fun initLocationListener(): LocationListener {
        return LocationListener { location: Location ->
            Log.d(
                "Location_debug", "locationListener#onLocationChanged\n" +
                        "latitude=${location.latitude};\n" +
                        "longitude=${location.longitude};\n" +
                        "speed=${location.speed};\n"
            )
            val latitude = location.latitude
            val longitude = location.longitude
            compositeDisposable += Single.fromCallable {
                geoCoder.getFromLocation(latitude, longitude, 1).first()
            }
                .map { address: Address ->
                    LocationModel(
                        latitude = latitude.toFloat(),
                        longitude = longitude.toFloat(),
                        cityName = address.locality,
                        countryName = address.countryName
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { locationModel: LocationModel ->
                        locationRepository.updateLocation(locationModel)
                    },
                    { throwable: Throwable ->
                        throwable.printStackTrace()
                    }
                )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("ServiceLifecycle", "$this#onStartCommand;")
        if (
            ContextCompat.checkSelfPermission(
                applicationContext,
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
                600_000L,
                2000f,
                locationListener
            )
        } else {
            Handler(Looper.getMainLooper()).postDelayed({ stopSelf() }, 1000L)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.i("ServiceLifecycle", "$this#onDestroy;")
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
        compositeDisposable.clear()
        _locationListener = null
    }
}