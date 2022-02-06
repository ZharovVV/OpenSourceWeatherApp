package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel

interface BaseConverter<Response, Entity, Model> {

    fun convertToModel(entity: Entity): Model

    fun convertToEntity(
        entityId: Int,
        locationModel: LocationModel,
        response: Response,
    ): Entity
}