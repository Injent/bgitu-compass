package ru.bgitu.app.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.EOFException
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.bgitu.app.BuildConfig
import ru.bgitu.app.core.common.exception.ApiException
import ru.bgitu.app.core.common.exception.NetworkException
import ru.bgitu.app.core.network.service.CompassApi
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.ClosedChannelException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.experimental.xor

val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
})

val sslContext = SSLContext.getInstance("SSL").apply {
    init(null, trustAllCerts, SecureRandom())
}

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            coerceInputValues = true
        }
    }

    single {
        val json = get<Json>()

        HttpClient(OkHttp) {
            expectSuccess = true
            followRedirects = true

            // TrustManager мешает делать http запросы если дата на устройстве не соответствует
            // серверной. Для дебага бывает полезно использовать разные даты
            if (BuildConfig.DEBUG) {
                engine {
                    config {
                        sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                        hostnameVerifier { _, _ -> true }
                    }
                }
            }

            install(ContentNegotiation) {
                json(get())
            }

            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                url(getBaseUrl())
            }

            install(HttpRequestRetry) {
                maxRetries = 2
                exponentialDelay()
                retryOnExceptionIf { _, e ->
                    e is NetworkException
                }
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, _ ->
                    val mappedException = when (cause) {
                        is HttpRequestTimeoutException,
                        is ConnectTimeoutException,
                        is SocketTimeoutException,
                        is UnknownHostException,
                        is UnresolvedAddressException,
                        is ConnectException,
                        is NoRouteToHostException,
                        is EOFException,
                        is ClosedChannelException -> NetworkException(cause)
                        is ClientRequestException,
                        is ServerResponseException -> ApiException(
                            cause = cause,
                            code = cause.response.status.value,
                            errorBody = cause.response.bodyAsText(),
                            message = cause.response.status.toString(),
                            format = json
                        )
                        else -> cause
                    }

                    throw mappedException
                }
            }
        }
    }

    singleOf(::CompassApi)
}

private const val BYTE_KEY: Byte = 69

// Усложнение для доступа к API для поверхностных программистов-злоумышленников
private fun getBaseUrl(): String {
    val encodedBytes = byteArrayOf(
        45, 49, 49, 53, 54, 127, 106, 106, 36, 53, 44, 104, 54, 54, 41, 107,
        39, 34, 44, 49, 48, 104, 38, 42, 40, 53, 36, 54, 54, 107, 55, 48, 106
    )
    return encodedBytes.map { byte ->
        byte xor BYTE_KEY
    }.toByteArray().decodeToString()
}