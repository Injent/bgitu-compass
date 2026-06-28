package ru.bgitu.app.feature.settings.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.Telegram
import ru.bgitu.app.core.designsystem.icon.VK
import ru.bgitu.app.core.util.BGITU_COMPASS_URL

enum class DevContact(
    fullName: String,
    @param:StringRes val nameResId: Int,
    @param:StringRes val roleResId: Int,
    private val _socials: List<Social>
) {
    MOBILE_DEV(
        fullName = "eliseyVerevkin",
        nameResId = R.string.feature_settings_mobile_dev_name,
        roleResId = R.string.feature_settings_mobile_dev_role,
        _socials =  listOf(
            Social(
                icon = Icons.Telegram,
                url = "tg"
            ),
            Social(
                icon = Icons.VK,
                url = "vk"
            )
        )
    ),
    BACKEND_DEV(
        fullName ="kirillPudov",
        nameResId = R.string.feature_settings_backend_dev,
        roleResId = R.string.feature_settings_backend_dev_role,
        _socials = listOf(
            Social(
                icon = Icons.Telegram,
                url = "tg"
            ),
            Social(
                icon = Icons.VK,
                url = "vk"
            )
        )
    );

    private val baseUrl = "$BGITU_COMPASS_URL/contacts/$fullName/"

    val socials: List<Social>
        get() = _socials.map {
            it.copy(url = "$baseUrl${it.url}")
        }

    val avatarUrl: String
        get() = "${baseUrl}avatar.png"

    data class Social(
        val icon: ImageVector,
        val url: String
    )
}