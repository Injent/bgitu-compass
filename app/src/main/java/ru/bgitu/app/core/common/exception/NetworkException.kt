package ru.bgitu.app.core.common.exception

class NetworkException(cause: Throwable? = null) : AppException(cause?.message, cause)