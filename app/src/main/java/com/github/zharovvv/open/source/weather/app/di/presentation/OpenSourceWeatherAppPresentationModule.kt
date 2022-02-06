package com.github.zharovvv.open.source.weather.app.di.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.*
import com.github.zharovvv.open.source.weather.app.domain.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.presentation.about.app.AboutAppViewModel
import com.github.zharovvv.open.source.weather.app.presentation.today.weather.HourlyWeatherViewModel
import com.github.zharovvv.open.source.weather.app.presentation.today.weather.WeatherTodayViewModel
import com.github.zharovvv.open.source.weather.app.presentation.week.weather.WeekWeatherViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
class OpenSourceWeatherAppPresentationModule {

    @Provides
    @[IntoMap ViewModelKey(AboutAppViewModel::class)]
    fun provideAboutAppViewModel(aboutAppRepository: IAboutAppRepository): ViewModel =
        AboutAppViewModel(aboutAppRepository)

    @Provides
    @[IntoMap ViewModelKey(LocationViewModel::class)]
    fun provideLocationViewModel(
        appContext: Context,
        locationRepository: ILocationRepository
    ): ViewModel = LocationViewModel(application = appContext as Application, locationRepository)

    @Provides
    @[IntoMap ViewModelKey(WeatherTodayViewModel::class)]
    fun provideWeatherTodayViewModel(
        locationRepository: ILocationRepository,
        weatherTodayRepository: IWeatherTodayRepository
    ): ViewModel = WeatherTodayViewModel(locationRepository, weatherTodayRepository)

    @Provides
    @[IntoMap ViewModelKey(HourlyWeatherViewModel::class)]
    fun provideHourlyWeatherViewModel(
        locationRepository: ILocationRepository,
        hourlyWeatherRepository: IHourlyWeatherRepository
    ): ViewModel = HourlyWeatherViewModel(locationRepository, hourlyWeatherRepository)

    @Provides
    @[IntoMap ViewModelKey(WeekWeatherViewModel::class)]
    fun provideWeekWeatherViewModel(
        locationRepository: ILocationRepository,
        weekWeatherRepository: IWeekWeatherRepository
    ): ViewModel = WeekWeatherViewModel(locationRepository, weekWeatherRepository)
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)