package ru.bgitu.app.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import kotlinx.datetime.LocalDate
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.icon.CalendarIllustration
import ru.bgitu.app.core.designsystem.icon.Case
import ru.bgitu.app.core.designsystem.icon.Group
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.News
import ru.bgitu.app.core.designsystem.icon.Settings
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.util.now
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.nonScaledSp
import ru.bgitu.app.feature.news.NewsNavKey
import ru.bgitu.app.feature.schedule.ScheduleKey
import ru.bgitu.app.feature.settings.SettingsKey
import ru.bgitu.app.feature.teachers.TeachersKey

data class NavBarItem(
    val key: NavKey,
    @param:StringRes val label: Int,
    val provideIcon: @Composable () -> Unit
)

private val SCHEDULE_ITEM by lazy {
    NavBarItem(
        key = ScheduleKey,
        label = R.string.app_label_schedule,
        provideIcon = {
            Box(Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.CalendarIllustration,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
                val monthNumber = LocalDate.now().day.toString()
                Text(
                    text = monthNumber,
                    style = TextStyle(
                        fontSize = 12.sp.nonScaledSp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        color = AppTheme.colorScheme.background1
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 3.dp),
                )
            }
        }
    )
}

private val STUDENTS_ITEM by lazy {
    NavBarItem(
        key = ScheduleKey,
        label = R.string.app_label_students,
        provideIcon = {
            Icon(
                imageVector = Icons.Group,
                contentDescription = null
            )
        }
    )
}

private val TEACHERS_SCHEDULE_ITEM by lazy {
    NavBarItem(
        key = TeachersKey,
        label = R.string.app_label_schedule,
        provideIcon = {
            Box(Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.CalendarIllustration,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
                val monthNumber = LocalDate.now().day.toString()
                Text(
                    text = monthNumber,
                    style = TextStyle(
                        fontSize = 12.sp.nonScaledSp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        color = AppTheme.colorScheme.background1
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 3.dp),
                )
            }
        }
    )
}

private val TEACHERS_ITEM by lazy {
    NavBarItem(
        key = TeachersKey,
        label = R.string.app_label_teachers,
        provideIcon = {
            Icon(
                imageVector = Icons.Case,
                contentDescription = null
            )
        }
    )
}

private val SETTINGS_ITEM by lazy {
    NavBarItem(
        key = SettingsKey,
        label = R.string.app_label_settings,
        provideIcon = {
            Icon(
                imageVector = Icons.Settings,
                contentDescription = null
            )
        }
    )
}

private val NEWS_ITEM by lazy {
    NavBarItem(
        key = NewsNavKey,
        label = R.string.app_label_news,
        provideIcon = {
            val features = LocalFeatures.current

            Box {
                Icon(
                    imageVector = Icons.News,
                    contentDescription = null
                )
                if (features.hasUnreadNotifications) {
                    Spacer(
                        Modifier
                            .background(AppTheme.colorScheme.destructive, CircleShape)
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
    )
}

val TOP_LEVEL_ROUTES by lazy {
    listOf(
        SCHEDULE_ITEM,
        TEACHERS_ITEM,
        SETTINGS_ITEM,
    )
}

val TEACHERS_TOP_LEVEL_ROUTES by lazy {
    listOf(
        TEACHERS_SCHEDULE_ITEM,
        STUDENTS_ITEM,
        SETTINGS_ITEM
    )
}