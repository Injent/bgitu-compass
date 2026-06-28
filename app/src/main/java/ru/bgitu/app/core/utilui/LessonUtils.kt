package ru.bgitu.app.core.utilui

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalResources
import ru.bgitu.app.R

fun getLessonLocation(res: Resources, building: String, classroom: String): String {
    return when {
        building.equals("ДОТ", ignoreCase = true) -> building
        classroom.isNotBlank() -> res.getString(R.string.core_utilUi_lessonLocationFull, classroom, building)
        else -> res.getString(R.string.core_utilUi_lessonLocationOnlyBuilding, building)
    }
}

@Composable
fun localizedLocation(building: String, classroom: String): String {
    val resources = LocalResources.current
    return getLessonLocation(
        res = resources,
        building = building,
        classroom = classroom
    )
}