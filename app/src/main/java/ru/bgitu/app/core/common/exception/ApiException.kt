package ru.bgitu.app.core.common.exception

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class ApiException(
    cause: Throwable?,
    val code: Int,
    val errorBody: String?,
    message: String,
    val format: Json
) : AppException(message, cause) {
    inline fun <reified T> bodyOrNull(): T? = decodeBody(format.serializersModule.serializer<T>())

    fun <T> decodeBody(serializer: KSerializer<T>): T? {
        val body = errorBody ?: return null
        return try {
            format.decodeFromString(serializer, body)
        } catch (_: Exception) {
            null
        }
    }
}