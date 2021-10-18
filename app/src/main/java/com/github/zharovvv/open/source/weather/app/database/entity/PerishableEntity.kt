package com.github.zharovvv.open.source.weather.app.database.entity

/**
 * Интерфейс для Entity, имеющей "срок годности".
 */
interface PerishableEntity : Entity {
    val isFresh: Boolean
}