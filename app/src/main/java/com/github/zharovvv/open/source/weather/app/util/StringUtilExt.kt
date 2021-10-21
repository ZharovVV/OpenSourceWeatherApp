package com.github.zharovvv.open.source.weather.app.util

import org.apache.commons.text.WordUtils

/**
 * This Is Title Case
 */
fun String.toTitleCase(): String {
    return WordUtils.capitalizeFully(this)
}