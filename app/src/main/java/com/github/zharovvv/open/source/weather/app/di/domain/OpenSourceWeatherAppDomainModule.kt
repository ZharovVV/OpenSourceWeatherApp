package com.github.zharovvv.open.source.weather.app.di.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.github.zharovvv.open.source.weather.app.data.local.AppDatabase
import com.github.zharovvv.open.source.weather.app.data.remote.WeatherApiMapper
import com.github.zharovvv.open.source.weather.app.data.repositories.*
import com.github.zharovvv.open.source.weather.app.di.AppContext
import com.github.zharovvv.open.source.weather.app.di.ApplicationScope
import com.github.zharovvv.open.source.weather.app.domain.*
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WidgetWeatherInteractor
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WorkManagerGateway
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
        weatherApiMapper: WeatherApiMapper,
    ): IHourlyWeatherRepository = HourlyWeatherRepository(
        hourlyWeatherDao = appDatabase.hourlyWeatherDao(),
        weatherApiMapper = weatherApiMapper,
        hourlyWeatherConverter = HourlyWeatherConverter()
    )

    @Provides
    @ApplicationScope
    fun provideLocationRepository(appDatabase: AppDatabase): ILocationRepository =
        LocationRepository(
            locationDao = appDatabase.locationDao()
        )

    @Provides
    @ApplicationScope
    fun provideWeatherTodayRepository(
        appDatabase: AppDatabase,
        weatherApiMapper: WeatherApiMapper,
    ): IWeatherTodayRepository = WeatherTodayRepository(
        weatherTodayDao = appDatabase.weatherTodayDao(),
        weatherApiMapper = weatherApiMapper,
        weatherTodayConverter = WeatherTodayConverter()
    )

    @Provides
    @ApplicationScope
    fun provideWeekWeatherRepository(
        appDatabase: AppDatabase,
        weatherApiMapper: WeatherApiMapper,
    ): IWeekWeatherRepository = WeekWeatherRepository(
        weekWeatherDao = appDatabase.weekWeatherDao(),
        weatherApiMapper = weatherApiMapper,
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