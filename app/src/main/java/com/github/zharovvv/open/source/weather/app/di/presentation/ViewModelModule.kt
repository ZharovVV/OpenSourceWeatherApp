package com.github.zharovvv.open.source.weather.app.di.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.di.AppContext
import com.github.zharovvv.open.source.weather.app.domain.*
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WidgetWeatherInteractor
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.WorkManagerGateway
import com.github.zharovvv.open.source.weather.app.domain.location.LocationViewModel
import com.github.zharovvv.open.source.weather.app.presentation.about.app.AboutAppViewModel
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
        @AppContext
        appContext: Context,
        locationRepository: ILocationRepository,
    ): ViewModel = LocationViewModel(application = appContext as Application, locationRepository)

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
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)