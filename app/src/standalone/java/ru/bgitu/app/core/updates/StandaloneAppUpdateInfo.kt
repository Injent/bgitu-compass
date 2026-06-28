package ru.bgitu.app.core.updates

import ru.bgitu.app.core.data.appupdater.AppUpdateInfo

internal data class StandaloneAppUpdateInfo(
    val versionCode: Int,
) : AppUpdateInfo {
    override val isStandalone: Boolean get() = true
}
