package com.github.zharovvv.open.source.weather.app.di.domain

import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.LocationManager
import androidx.work.WorkManager
import com.github.zharovvv.open.source.weather.app.data.local.AppDatabase
import com.github.zharovvv.open.source.weather.app.data.remote.WeatherApiGateway
import com.github.zharovvv.open.source.weather.app.data.repositories.*
import com.github.zharovvv.open.source.weather.app.di.AppContext
import com.github.zharovvv.open.source.weather.app.di.ApplicationScope
import com.github.zharovvv.open.source.weather.app.domain.*
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WidgetWeatherInteractor
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WorkManagerGateway
import com.github.zharovvv.open.source.weather.app.domain.location.*
import com.github.zharovvv.open.source.weather.app.resource.ResourceProvider
import dagger.Module
import dagger.Provides

@Module
class OpenSourceWeatherAppDomainModule {

    @Provides
    @ApplicationScope
    fun provideAboutAppRepository(@AppContext context: Context): IAboutAppRepository =
        AboutAppRepository(appContext = context)

    @Provides
    @ApplicationScope
    fun provideHourlyWeatherRepository(
        appDatabase: AppDatabase,
        weatherApiGateway: WeatherApiGateway,
    ): IHourlyWeatherRepository = HourlyWeatherRepository(
        hourlyWeatherDao = appDatabase.hourlyWeatherDao(),
        weatherApiGateway = weatherApiGateway,
        hourlyWeatherConverter = HourlyWeatherConverter()
    )

    @Provides
    @ApplicationScope
    fun provideLocationRemoteRepository(@AppContext context: Context): ILocationRemoteRepository =
        LocationRemoteRepository(
            locationManager = context.getSystemService(Service.LOCATION_SERVICE) as LocationManager,
            geocoder = Geocoder(context),
            applicationContext = context
        )

    @Provides
    @ApplicationScope
    fun provideMutableLocationRepository(
        appDatabase: AppDatabase,
        locationRemoteRepository: ILocationRemoteRepository,
        preferencesRepository: IPreferencesRepository,
    ): IMutableLocationRepository = LocationRepository(
        locationLocalRepository = LocationLocalRepository(
            locationDao = appDatabase.locationDao()
        ),
        locationRemoteRepository = locationRemoteRepository,
        preferencesRepository = preferencesRepository
    )

    @Provides
    @ApplicationScope
    fun bindLocationRepository(
        mutableLocationRepository: IMutableLocationRepository,
    ): ILocationRepository = mutableLocationRepository

    @Provides
    @ApplicationScope
    fun provideWeatherTodayRepository(
        appDatabase: AppDatabase,
        weatherApiGateway: WeatherApiGateway,
    ): IWeatherTodayRepository = WeatherTodayRepository(
        weatherTodayDao = appDatabase.weatherTodayDao(),
        weatherApiGateway = weatherApiGateway,
        weatherTodayConverter = WeatherTodayConverter()
    )

    @Provides
    @ApplicationScope
    fun provideWeekWeatherRepository(
        appDatabase: AppDatabase,
        weatherApiGateway: WeatherApiGateway,
    ): IWeekWeatherRepository = WeekWeatherRepository(
        weekWeatherDao = appDatabase.weekWeatherDao(),
        weatherApiGateway = weatherApiGateway,
        weekWeatherConverter = WeekWeatherConverter()
    )

    @Provides
    @ApplicationScope
    fun provideWidgetWeatherInteractor(
        locationRepository: ILocationRepository,
        weatherTodayRepository: IWeatherTodayRepository,
    ) = WidgetWeatherInteractor(locationRepository, weatherTodayRepository)

    @Provides
    @ApplicationScope
    fun provideChooseCityInteractor(
        mutableLocationRepository: IMutableLocationRepository,
        locationRemoteRepository: ILocationRemoteRepository,
        preferencesRepository: IPreferencesRepository,
        @AppContext applicationContext: Context,
    ) = ChooseCityInteractor(
        mutableLocationRepository,
        locationRemoteRepository,
        preferencesRepository,
        applicationContext
    )

    @Provides
    @ApplicationScope
    fun provideSelectLocationModeInteractor(
        preferencesRepository: IPreferencesRepository,
    ) = SelectLocationModeInteractor(preferencesRepository)

    @Provides
    @ApplicationScope
    fun provideWorkManagerGateway(
        @AppContext context: Context,
        preferencesRepository: IPreferencesRepository,
    ) = WorkManagerGateway(
        workManager = WorkManager.getInstance(context),
        preferencesRepository = preferencesRepository
    )

    @Provides
    @ApplicationScope
    fun providePreferencesRepository(
        sharedPreferences: SharedPreferences,
        resourceProvider: ResourceProvider,
    ): IPreferencesRepository = PreferencesRepository(sharedPreferences, resourceProvider)
}