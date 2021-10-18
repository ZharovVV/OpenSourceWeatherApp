package com.github.zharovvv.open.source.weather.app.repository

interface BaseConverter<Response, Entity, Model> {

    fun convertToModel(entity: Entity): Model

    fun convertToEntity(
        entityId: Int,
        latitude: Float,
        longitude: Float,
        response: Response,
    ): Entity
}