package ru.bgitu.app.feature.widget.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import ru.bgitu.app.R
import ru.bgitu.app.core.model.Lesson
import ru.bgitu.app.core.utilui.getLessonLocation
import ru.bgitu.app.feature.widget.glance.GlanceShape
import ru.bgitu.app.feature.widget.glance.WidgetTheme.widgetPadding
import ru.bgitu.app.feature.widget.glance.backgroundCompat
import kotlin.time.Instant

@Composable
fun SuccessContent(
    lastRefreshed: Instant,
    lessons: List<Lesson>,
    onStartActivity: Action,
) {
    val context = LocalContext.current

    if (lessons.isNotEmpty()) {
        LazyColumnWithGap(
            items = lessons,
            itemContentProvider = { index, lesson ->
                LessonItem(
                    lesson = lesson,
                    shape = GlanceShape.getListShape(index = index, size = lessons.size),
                    onClick = onStartActivity,
                    modifier = GlanceModifier
                        .fillMaxWidth()
                )
            },
            footer = {
                RefreshedDateItem(lastRefreshed = lastRefreshed)
            },
            modifier = GlanceModifier
                .fillMaxSize()
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(onClick = onStartActivity)
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_cat_sleeping),
                contentDescription = null,
                colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
            )
            Spacer(GlanceModifier.height(12.dp))
            Text(
                text = context.getString(R.string.feature_schedule_weekend),
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }
    }
}

@Composable
private fun LessonItem(
    lesson: Lesson,
    shape: GlanceShape,
    onClick: Action,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current
    fun TextUnit.toDp(): Dp {
        return if (this.type == TextUnitType.Sp) {
            val fontScale = context.resources.configuration.fontScale
            (this.value * fontScale).dp
        } else {
            Dp.Unspecified
        }
    }

    Row(
        modifier = modifier
            .backgroundCompat(
                color = GlanceTheme.colors.secondaryContainer.getColor(context),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Column {
            Spacer(
                modifier = GlanceModifier
                    .height(
                        LayoutTextStyles.titleText().fontSize!!.toDp()
                                - LayoutTextStyles.supportingText().fontSize!!.toDp()
                    )
            )
            Text(
                text = lesson.startAt.toString(),
                style = LayoutTextStyles.supportingText(),
            )
            Text(
                text = lesson.endAt.toString(),
                style = LayoutTextStyles.supportingText(),
            )
        }
        Spacer(GlanceModifier.width(8.dp))
        Column(
            modifier = GlanceModifier
                .defaultWeight()
        ) {
            Text(
                text = lesson.subjectName,
                style = LayoutTextStyles.titleText()
            )
            Text(
                text = getLessonLocation(
                    res = context.resources,
                    building = lesson.building,
                    classroom = lesson.classroom
                ),
                style = LayoutTextStyles.supportingText()
            )
        }
        Spacer(GlanceModifier.width(4.dp))
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = GlanceModifier
                .padding(top = 5.dp)
        ) {
            Image(
                provider = ImageProvider(
                    if (lesson.isLecture) R.drawable.ic_book else R.drawable.ic_flask
                ),
                contentDescription = null,
                colorFilter = ColorFilter.tint(GlanceTheme.colors.onSecondaryContainer),
                modifier = GlanceModifier
                    .size(12.dp)
            )
        }

    }
}

@Composable
private fun <T> LazyColumnWithGap(
    items: List<T>,
    itemContentProvider: @Composable (index: Int, item: T) -> Unit,
    modifier: GlanceModifier = GlanceModifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalItemsSpacing: Dp = 2.dp,
    bottomPadding: Dp = widgetPadding,
    footer: @Composable () -> Unit
) {
    LazyColumn(modifier, horizontalAlignment) {
        itemsIndexed(items) { index, item ->
            Column(modifier = GlanceModifier.fillMaxWidth()) {
                itemContentProvider(index, item)
                if (index != items.lastIndex) {
                    Spacer(modifier = GlanceModifier.height(verticalItemsSpacing))
                }
            }
        }
        item {
            Spacer(GlanceModifier.height(bottomPadding))
        }
        item {
            footer()
        }
    }
}
