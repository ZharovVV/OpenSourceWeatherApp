package com.github.zharovvv.open.source.weather.app.util

fun <K, V> Array<K>.associateWithAnother(valueArray: Array<V>): Map<K, V> {
    if (this.size != valueArray.size) throw IllegalArgumentException("The sizes of the arrays must match!")
    val keyArray = this
    val map = mutableMapOf<K, V>()
    keyArray.forEachIndexed { index: Int, key: K ->
        map += key to valueArray[index]
    }
    return map
}