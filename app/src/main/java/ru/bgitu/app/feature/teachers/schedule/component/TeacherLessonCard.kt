package ru.bgitu.app.feature.teachers.schedule.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.yearMonth
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCard
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.designsystem.icon.Flask
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.OpenBook
import ru.bgitu.app.core.designsystem.icon.Pc
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.TeacherLesson
import ru.bgitu.app.core.util.getAbbreviatedByLocale
import ru.bgitu.app.core.util.getFullByLocale
import ru.bgitu.app.core.utilui.localizedLocation

@Composable
fun TeacherLessonCard(
    lesson: TeacherLesson,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    AppCard(
        shape = shape,
        onClick = if (lesson.groupNames.size > 1) {
            {
                expanded = !expanded
            }
        } else null,
        modifier = modifier
    ) {
        ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                        trailingContentColor = AppTheme.colorScheme.foreground2,
                        contentColor = AppTheme.colorScheme.foreground1
                    ),
                    headlineContent = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = localizedLocation(
                                    building = lesson.building,
                                    classroom = lesson.classroom
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Icon(
                                imageVector = when {
                                    lesson.isLecture -> Icons.OpenBook
                                    lesson.building.equals("ДОТ", ignoreCase = true) -> Icons.Pc
                                    else -> Icons.Flask
                                },
                                contentDescription = null,
                                tint = AppTheme.colorScheme.foreground2,
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }

                    },
                    leadingContent = {
                        Text(
                            text = buildAnnotatedString {
                                append(lesson.startAt.toString())
                                appendLine()
                                append(lesson.endAt.toString())
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal,
                            color = AppTheme.colorScheme.foreground1
                        )
                    },
                    supportingContent = {
                        Column(
                            modifier = Modifier
                                .animateContentSize(
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                )
                        ) {
                            Text(
                                text = lesson.subjectName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Normal,
                                color = AppTheme.colorScheme.foreground2,
                                lineHeight = 20.sp
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                maxLines = if (expanded) Int.MAX_VALUE else 2,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            ) {
                                if (expanded || lesson.groupNames.size <= 1) {
                                    lesson.groupNames.forEach { groupName ->
                                        GroupName(name = groupName)
                                    }
                                } else {
                                    GroupName(name = lesson.groupNames.first())
                                    GroupName(
                                        name = "+${lesson.groupNames.size - 1}",
                                        modifier = Modifier
                                            .widthIn(min = 32.dp)
                                    )
                                }
                            }
                        }
                    },
        )
    }
}

@Composable
fun GroupName(
    name: String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.foreground1
) {
    Text(
        text = name,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        modifier = modifier
            .clip(CircleShape)
            .background(AppTheme.colorScheme.background1Container)
            .padding(
                horizontal = 6.dp,
                vertical = 4.dp
            )
    )
}

@Composable
fun TeacherLessonDate(
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locales[0]
    val shortWeekDayName = remember(date.dayOfWeek, locale) {
        LocalDate.Format {
            dayOfWeek(DayOfWeekNames.getAbbreviatedByLocale(locale))
        }.format(date)
    }

    Text(
        text = buildAnnotatedString {
            append(shortWeekDayName.uppercase())
            appendLine()
            withStyle(
                MaterialTheme.typography.titleLarge.merge(
                    color = AppTheme.colorScheme.foreground1,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ).toSpanStyle()
            ) {
                append(date.day.toString())
            }
        },
        maxLines = 2,
        color = AppTheme.colorScheme.foreground3,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}