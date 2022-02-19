package com.github.zharovvv.open.source.weather.app.di

import android.content.Context
import com.github.zharovvv.open.source.weather.app.di.data.OpenSourceWeatherAppDataModule
import com.github.zharovvv.open.source.weather.app.di.domain.OpenSourceWeatherAppDomainModule
import com.github.zharovvv.open.source.weather.app.di.presentation.OpenSourceWeatherAppPresentationModule
import com.github.zharovvv.open.source.weather.app.domain.IPreferencesRepository
import com.github.zharovvv.open.source.weather.app.domain.auto.update.widget.CurrentWeatherWorker
import com.github.zharovvv.open.source.weather.app.domain.location.LocationService
import com.github.zharovvv.open.source.weather.app.presentation.BaseFragment
import com.github.zharovvv.open.source.weather.app.presentation.settings.SettingsPreferenceFragment
import com.github.zharovvv.open.source.weather.app.presentation.widget.WeatherAppWidgetProvider
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Qualifier
import javax.inject.Scope

@Component(
    modules = [
        AppModule::class,
        OpenSourceWeatherAppDataModule::class,
        OpenSourceWeatherAppDomainModule::class,
        OpenSourceWeatherAppPresentationModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withAppContext(appContext: Context): Builder
        fun build(): ApplicationComponent
    }

    fun inject(baseFragment: BaseFragment)

    //TODO переделать (возможно стоит объеденить SettingsContainerFragment и SettingsPreferenceFragment)
    fun inject(settingsPreferenceFragment: SettingsPreferenceFragment)

    //TODO удалить этот чертов сервис
    fun inject(locationService: LocationService)

    fun inject(currentWeatherWorker: CurrentWeatherWorker)

    fun inject(weatherAppWidgetProvider: WeatherAppWidgetProvider)

    val preferencesRepository: IPreferencesRepository
}

@Module
interface AppModule {
    /**
     * Привязываем context в графе зависимостей к квалификатору [AppContext].
     */
    @Binds
    @AppContext
    fun bindAppContext(context: Context): Context
}

@Scope
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext