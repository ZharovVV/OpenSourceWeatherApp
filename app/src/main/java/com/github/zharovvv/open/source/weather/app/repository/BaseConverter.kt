package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.model.LocationModel

interface BaseConverter<Response, Entity, Model> {

    fun convertToModel(entity: Entity): Model

    fun convertToEntity(
        entityId: Int,
        locationModel: LocationModel,
        response: Response,
    ): Entity
}