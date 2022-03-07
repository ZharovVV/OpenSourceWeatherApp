package com.github.zharovvv.open.source.weather.app.di.presentation

import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.*
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WidgetWeatherInteractor
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WorkManagerGateway
import com.github.zharovvv.open.source.weather.app.domain.location.ChooseCityInteractor
import com.github.zharovvv.open.source.weather.app.domain.location.ILocationRepository
import com.github.zharovvv.open.source.weather.app.domain.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.presentation.about.app.AboutAppViewModel
import com.github.zharovvv.open.source.weather.app.presentation.choose.city.ChooseCityViewModel
import com.github.zharovvv.open.source.weather.app.presentation.settings.SettingsViewModel
import com.github.zharovvv.open.source.weather.app.presentation.today.weather.WeatherTodayViewModel
import com.github.zharovvv.open.source.weather.app.presentation.week.weather.WeekWeatherViewModel
import com.github.zharovvv.open.source.weather.app.presentation.widget.WeatherWidgetManager
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
class ViewModelModule {

    @Provides
    @[IntoMap ViewModelKey(AboutAppViewModel::class)]
    fun provideAboutAppViewModel(aboutAppRepository: IAboutAppRepository): ViewModel =
        AboutAppViewModel(aboutAppRepository)

    @Provides
    @[IntoMap ViewModelKey(LocationViewModel::class)]
    fun provideLocationViewModel(
        locationRepository: ILocationRepository,
        preferencesRepository: IPreferencesRepository,
    ): ViewModel = LocationViewModel(locationRepository, preferencesRepository)

    @Provides
    @[IntoMap ViewModelKey(WeatherTodayViewModel::class)]
    fun provideWeatherTodayViewModel(
        locationRepository: ILocationRepository,
        weatherTodayRepository: IWeatherTodayRepository,
        hourlyWeatherRepository: IHourlyWeatherRepository,
        widgetWeatherInteractor: WidgetWeatherInteractor,
        workManagerGateway: WorkManagerGateway,
        weatherWidgetManager: WeatherWidgetManager,
    ): ViewModel = WeatherTodayViewModel(
        locationRepository,
        weatherTodayRepository,
        hourlyWeatherRepository,
        widgetWeatherInteractor,
        workManagerGateway,
        weatherWidgetManager
    )

    @Provides
    @[IntoMap ViewModelKey(WeekWeatherViewModel::class)]
    fun provideWeekWeatherViewModel(
        locationRepository: ILocationRepository,
        weekWeatherRepository: IWeekWeatherRepository,
    ): ViewModel = WeekWeatherViewModel(locationRepository, weekWeatherRepository)

    @Provides
    @[IntoMap ViewModelKey(SettingsViewModel::class)]
    fun provideSettingsViewModel(
        preferencesRepository: IPreferencesRepository,
        workManagerGateway: WorkManagerGateway,
    ): ViewModel = SettingsViewModel(preferencesRepository, workManagerGateway)

    @Provides
    @[IntoMap ViewModelKey(ChooseCityViewModel::class)]
    fun provideChooseCityViewModel(
        chooseCityInteractor: ChooseCityInteractor,
    ): ViewModel = ChooseCityViewModel(chooseCityInteractor)
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)