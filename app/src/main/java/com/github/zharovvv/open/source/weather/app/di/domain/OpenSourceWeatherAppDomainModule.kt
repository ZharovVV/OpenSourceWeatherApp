package com.github.zharovvv.open.source.weather.app.di.domain

import com.github.zharovvv.open.source.weather.app.data.local.AppDatabase
import com.github.zharovvv.open.source.weather.app.data.remote.WeatherApiMapper
import com.github.zharovvv.open.source.weather.app.data.repositories.*
import com.github.zharovvv.open.source.weather.app.di.ApplicationScope
import com.github.zharovvv.open.source.weather.app.domain.*
import dagger.Module
import dagger.Provides

@Module
class OpenSourceWeatherAppDomainModule {

    @Provides
    @ApplicationScope
    fun provideAboutAppRepository(): IAboutAppRepository = AboutAppRepository()

    @Provides
    @ApplicationScope
    fun provideHourlyWeatherRepository(
        appDatabase: AppDatabase,
        weatherApiMapper: WeatherApiMapper
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
        weatherApiMapper: WeatherApiMapper
    ): IWeatherTodayRepository = WeatherTodayRepository(
        weatherTodayDao = appDatabase.weatherTodayDao(),
        weatherApiMapper = weatherApiMapper,
        weatherTodayConverter = WeatherTodayConverter()
    )

    @Provides
    @ApplicationScope
    fun provideWeekWeatherRepository(
        appDatabase: AppDatabase,
        weatherApiMapper: WeatherApiMapper
    ): IWeekWeatherRepository = WeekWeatherRepository(
        weekWeatherDao = appDatabase.weekWeatherDao(),
        weatherApiMapper = weatherApiMapper,
        weekWeatherConverter = WeekWeatherConverter()
    )
}