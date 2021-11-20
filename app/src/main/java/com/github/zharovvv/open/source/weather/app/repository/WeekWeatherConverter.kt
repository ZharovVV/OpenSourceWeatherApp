package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.database.entity.WeekWeatherEntity
import com.github.zharovvv.open.source.weather.app.database.entity.WeekWeatherItemPojoEntity
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.model.WeekWeatherItemModel
import com.github.zharovvv.open.source.weather.app.model.WeekWeatherModel
import com.github.zharovvv.open.source.weather.app.network.dto.WeekWeatherItem
import com.github.zharovvv.open.source.weather.app.network.dto.WeekWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.setZeroTime
import com.github.zharovvv.open.source.weather.app.util.toTitleCase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class WeekWeatherConverter :
    BaseConverter<WeekWeatherResponse, WeekWeatherEntity, WeekWeatherModel> {

    override fun convertToModel(entity: WeekWeatherEntity): WeekWeatherModel {
        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
        return WeekWeatherModel(
            items = entity.items.map { weekWeatherItemEntity ->
                val dateString = simpleDateFormat.format(weekWeatherItemEntity.forecastDate)
                    .toTitleCase()
                WeekWeatherItemModel(
                    iconId = HourlyWeatherConverter.ICONS_MAP_DARK[weekWeatherItemEntity.iconId]!!,
                    dayOfWeekOfForecast = dateString.substring(
                        startIndex = 0,
                        endIndex = dateString.indexOf(',') + 1
                    ),
                    forecastDate = dateString.substringAfter(','),
                    maxTemperature = weekWeatherItemEntity.maxTemperature.toString(),
                    minTemperature = "/${weekWeatherItemEntity.minTemperature}°"
                )
            }
        )
    }

    override fun convertToEntity(
        entityId: Int,
        locationModel: LocationModel,
        response: WeekWeatherResponse
    ): WeekWeatherEntity {
        return WeekWeatherEntity(
            id = entityId,
            latitude = locationModel.latitude,
            longitude = locationModel.longitude,
            updateTime = Date(),
            items = response.daily.map { weekWeatherItemRs: WeekWeatherItem ->
                val weather = weekWeatherItemRs.weather.first()
                WeekWeatherItemPojoEntity(
                    forecastDate = Date(weekWeatherItemRs.dt * 1000).setZeroTime(),
                    iconId = weather.icon,
                    maxTemperature = ceil(weekWeatherItemRs.temp.max).toInt(),
                    minTemperature = ceil(weekWeatherItemRs.temp.min).toInt()
                )
            }
        )
    }
}