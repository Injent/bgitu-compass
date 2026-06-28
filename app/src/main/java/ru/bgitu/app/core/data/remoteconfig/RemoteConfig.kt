package ru.bgitu.app.core.data.remoteconfig

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class RemoteConfig(
    @SerialName("lastResetTimestamp")
    val lastResetTimestamp: Instant? = null,
    @SerialName("versionCode")
    val versionCode: Long? = null,
    @SerialName("downloadUrl")
    val apkDownloadUrl: String? = null,
    @SerialName("lastFetchAt")
    val lastFetchAt: Instant? = null,
    @SerialName("maxLinkSupport")
    val maxLinkSupport: String? = null,
    @SerialName("vkLinkSupport")
    val vkLinkSupport: String? = null,
    @SerialName("telegramLinkSupport")
    val telegramLinkSupport: String? = null,
    @SerialName("termStartDate")
    val termStartDate: LocalDate? = LocalDate(2026, 1, 26)
)