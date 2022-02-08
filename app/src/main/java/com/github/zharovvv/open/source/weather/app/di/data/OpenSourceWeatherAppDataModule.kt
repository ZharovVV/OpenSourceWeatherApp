package com.github.zharovvv.open.source.weather.app.di.data

import android.content.Context
import androidx.room.Room
import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.data.local.AppDatabase
import com.github.zharovvv.open.source.weather.app.data.remote.WeatherApiMapper
import com.github.zharovvv.open.source.weather.app.di.AppContext
import com.github.zharovvv.open.source.weather.app.di.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class OpenSourceWeatherAppDataModule {

    @Provides
    @ApplicationScope
    fun provideAppDataBase(@AppContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "weather_app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideWeatherApiMapper(): WeatherApiMapper {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(WeatherApiMapper::class.java)
    }
}