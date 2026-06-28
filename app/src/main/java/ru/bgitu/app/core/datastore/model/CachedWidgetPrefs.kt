package ru.bgitu.app.core.datastore.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.model.WidgetTheme
import ru.bgitu.app.core.util.now

@Serializable
data class CachedWidgetPrefs(
    val alpha: Float = 1f,
    val selectedDate: LocalDate = LocalDate.now(),
    val uiTheme: WidgetTheme = WidgetTheme.AUTO,
    private val groupId: Int?,
    private val groupName: String?
) {
    val group: Group?
        get() = runCatching {
            Group(
                id = groupId!!,
                name = groupName!!
            )
        }.getOrNull()
}