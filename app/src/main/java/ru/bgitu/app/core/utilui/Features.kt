package ru.bgitu.app.core.utilui

import androidx.compose.runtime.compositionLocalOf

data class Features(
    val highQualityBlur: Boolean = true,
    val blurEnabled: Boolean = true,
    val userIsTeacher: Boolean = false,
    val hasUnreadNotifications: Boolean = false
)

val LocalFeatures = compositionLocalOf<Features> {
    error("Features not provided")
}