package ru.bgitu.app.core.data.appupdater

class InstallException(
    val errorCode: Int,
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)