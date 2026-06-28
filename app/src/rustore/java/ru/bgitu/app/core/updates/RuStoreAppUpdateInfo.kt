package ru.bgitu.app.core.updates

import ru.bgitu.app.core.data.appupdater.AppUpdateInfo

@JvmInline
value class RuStoreAppUpdateInfo(
    val info: ru.rustore.sdk.appupdate.model.AppUpdateInfo,
) : AppUpdateInfo {
    override val isStandalone: Boolean get() = false
}
