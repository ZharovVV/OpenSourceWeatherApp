package com.github.zharovvv.open.source.weather.app.logger

interface LoggerDelegate {
    fun i(tag: String, message: String)

    fun d(tag: String, message: String)

    fun w(tag: String, message: String)

    fun e(tag: String, message: String, throwable: Throwable)
}