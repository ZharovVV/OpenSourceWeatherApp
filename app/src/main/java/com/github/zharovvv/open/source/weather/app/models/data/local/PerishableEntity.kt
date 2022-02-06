package com.github.zharovvv.open.source.weather.app.models.data.local

/**
 * Интерфейс для Entity, имеющей "срок годности".
 */
interface PerishableEntity : Entity {
    val isFresh: Boolean
}