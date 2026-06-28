package ru.bgitu.app.feature.schedule.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCard
import ru.bgitu.app.core.designsystem.component.PerfectDashedDivider
import ru.bgitu.app.core.designsystem.icon.ArrowSmall
import ru.bgitu.app.core.designsystem.icon.Building
import ru.bgitu.app.core.designsystem.icon.Flask
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.OpenBook
import ru.bgitu.app.core.designsystem.icon.Pc
import ru.bgitu.app.core.designsystem.icon.Person
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.LessonItem
import ru.bgitu.app.core.util.now
import ru.bgitu.app.core.utilui.localizedLocation
import ru.bgitu.app.core.utilui.observeTime
import ru.bgitu.app.feature.teachers.schedule.component.GroupName
import kotlin.time.Duration.Companion.seconds

@Composable
fun LessonCard(
    lesson: LessonItem,
    date: LocalDate,
    shape: RoundedCornerShape,
    isOngoing: Boolean,
    onNavigateToTeacher: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isExpandable = remember {
        when (lesson) {
            is LessonItem.Student -> lesson.lesson.teacher.isNotEmpty() || isOngoing
            is LessonItem.Teacher -> lesson.lesson.groupNames.isNotEmpty() || isOngoing
        }
    }
    var expanded by rememberSaveable { mutableStateOf(lesson is LessonItem.Teacher) }

    AppCard(
        onClick = {
            if (!isExpandable) return@AppCard
            expanded = !expanded
        },
        shape = shape,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    append(lesson.subjectName)

                    if (lesson is LessonItem.Student) {
                        lesson.lesson.teacher.takeIf { it.isNotEmpty() }?.let { teacherName ->
                            appendLine()
                            withLink(
                                LinkAnnotation.Clickable(
                                    tag = "teacher",
                                    linkInteractionListener = { onNavigateToTeacher() },
                                    styles = TextLinkStyles(
                                        style = MaterialTheme.typography.bodyMedium.toSpanStyle().copy(
                                            color = AppTheme.colorScheme.foreground2,
                                            textDecoration = TextDecoration.None
                                        )
                                    )
                                )
                            ) {
                                appendInlineContent(PERSON_CARD_ID)
                                append(" ")
                                append(teacherName)
                            }
                        }
                    }

                    withStyle(
                        MaterialTheme.typography.bodyMedium.toSpanStyle()
                            .copy(color = AppTheme.colorScheme.foreground2)
                    ) {
                        appendLine()
                        if (lesson.building.equals("ДОТ", true)) {
                            appendInlineContent(PC_ID)
                        } else {
                            appendInlineContent(BUILDING_ID)
                        }
                        append(" ")
                        append(localizedLocation(
                            building = lesson.building,
                            classroom = lesson.classroom
                        ))
                    }
                },
                lineHeight = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                inlineContent = lessonInlineContent
            )
            Column(
                Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(
                            stringResource(
                                if (lesson.isLecture) {
                                    R.string.feature_schedule_lecture_abbreviation
                                } else {
                                    R.string.feature_schedule_practice_abbreviation
                                }
                            )
                        )
                        append(" ")

                        appendInlineContent(if (lesson.isLecture) OPEN_BOOK_ID else FLASK_ID)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.foreground2,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium,
                    inlineContent = lessonInlineContent,
                    modifier = Modifier
                        .background(AppTheme.colorScheme.background1Container, CircleShape)
                        .padding(
                            horizontal = 5.dp,
                            vertical = 3.dp
                        )
                )

                Spacer(Modifier.weight(1f))

                if (isExpandable) {
                    val rotation by animateFloatAsState(
                        if (expanded) -90f else 90f
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.ArrowSmall,
                            contentDescription = null,
                            tint = AppTheme.colorScheme.foreground2,
                            modifier = Modifier
                                .rotate(rotation)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ),
            exit = shrinkVertically(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
        ) {
            Column(
                Modifier
                    .padding(bottom = 12.dp)
            ) {
                PerfectDashedDivider(
                    dashColor = AppTheme.colorScheme.background2,
                    backgroundColor = AppTheme.colorScheme.background1
                )
                when (lesson) {
                    is LessonItem.Student -> {
                        lesson.lesson.teacherFullName?.let { teacherName ->
                            Spacer(Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .clickable { onNavigateToTeacher() }
                                    .padding(horizontal = 12.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = teacherName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AppTheme.colorScheme.foreground1,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.ArrowSmall,
                                    contentDescription = null,
                                    tint = AppTheme.colorScheme.foreground3,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    is LessonItem.Teacher -> {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            maxLines = if (expanded) Int.MAX_VALUE else 2,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .padding(horizontal = 12.dp)
                        ) {
                            lesson.lesson.groupNames.forEach { groupName ->
                                GroupName(name = groupName)
                            }
                        }
                    }
                }

                var showProgressBar by remember(date) {
                    val now = LocalDateTime.now()
                    val startTime = date.atTime(lesson.startAt)
                    val endTime = date.atTime(lesson.endAt)
                    mutableStateOf(now in startTime..endTime)
                }

                AnimatedVisibility(
                    visible = showProgressBar,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val now by observeTime { 1.seconds }
                    val startTime = date.atTime(lesson.startAt)
                    val endTime = date.atTime(lesson.endAt)

                    LaunchedEffect(now) {
                        if (now !in startTime..endTime) {
                            showProgressBar = false
                        }
                    }

                    TimeProgressBar(
                        startTime = startTime,
                        endTime = endTime,
                        currentTime = now,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

private const val BUILDING_ID = "building"
private const val PERSON_CARD_ID = "person_card"
private const val FLASK_ID = "flask"
private const val OPEN_BOOK_ID = "book"
private const val PC_ID = "devices"

private val lessonInlineContent = mapOf(
    BUILDING_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 1.em,
            height = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
        )
    ) {
        Icon(
            imageVector = Icons.Building,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    PERSON_CARD_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 1.em,
            height = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
        )
    ) {
        Icon(
            imageVector = Icons.Person,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    FLASK_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 11.sp,
            height = 11.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
        )
    ) {
        Icon(
            imageVector = Icons.Flask,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    OPEN_BOOK_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 12.sp,
            height = 12.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
        )
    ) {
        Icon(
            imageVector = Icons.OpenBook,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    },
    PC_ID to InlineTextContent(
        placeholder = Placeholder(
            width = 1.em,
            height = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
        )
    ) {
        Icon(
            imageVector = Icons.Pc,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground2,
            modifier = Modifier
        )
    }
)