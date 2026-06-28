package ru.bgitu.app.core.common.exception

open class AppException(message: String?, cause: Throwable? = null) : Exception(message, cause) {
    init {
        if (cause != null) {
            stackTrace = cause.stackTrace
        }
    }
}