package com.github.zharovvv.open.source.weather.app.logger

object Logger {

    private var _logDelegate: LoggerDelegate? = null

    fun setDelegate(loggerDelegate: LoggerDelegate) {
        _logDelegate = loggerDelegate
    }

    fun i(tag: String, message: String) {
        _logDelegate?.i(tag, message)
    }

    fun d(tag: String, message: String) {
        _logDelegate?.d(tag, message)
    }

    fun w(tag: String, message: String) {
        _logDelegate?.w(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable) {
        _logDelegate?.e(tag, message, throwable)
    }
}