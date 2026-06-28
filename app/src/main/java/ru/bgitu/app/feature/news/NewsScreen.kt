package ru.bgitu.app.feature.news

import android.os.Parcelable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.icon.CatReadNews
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.util.dateFormat
import ru.bgitu.app.core.util.getAbbreviatedByLocale
import ru.bgitu.app.core.util.inWeekOfOtherDate
import ru.bgitu.app.core.util.now
import ru.bgitu.app.core.util.openTab
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.component.LoadingIndicatorBox
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.navigation.Navigator

@Parcelize
@Serializable
data object NewsNavKey : NavKey, Parcelable

fun EntryProviderScope<NavKey>.newsScreen(navigator: Navigator) {
    entry<NewsNavKey> {
        NewsScreen()
    }
}

@Composable
private fun NewsScreen(
    viewModel: NewsViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notificationManager = remember { NotificationManagerCompat.from(context) }

    NewsScreenContent(
        modifier = modifier,
        toggleUnreadNews = viewModel::toggleUnreadNotifications,
        onMarkAsRead = { notificationId ->
            notificationManager.cancel(notificationId)
            viewModel.markAsRead(notificationId)
        },
        onMarkAllAsRead = {
            notificationManager.cancelAll()
            viewModel.markAllAsRead()
        },
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NewsScreenContent(
    uiState: NewsUiState,
    toggleUnreadNews: () -> Unit,
    onMarkAsRead: (Int) -> Unit,
    onMarkAllAsRead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val today = remember { LocalDate.now() }
    val timeZone = remember { TimeZone.currentSystemDefault() }
    val locale = LocalConfiguration.current.locales[0]

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            @Composable
            fun Chip(
                text: String,
                selected: Boolean
            ) {
                FilterChip(
                    selected = selected,
                    onClick = toggleUnreadNews,
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = AppTheme.colorScheme.background1,
                        selectedContainerColor = AppTheme.colorScheme.brand,
                        labelColor = AppTheme.colorScheme.brand,
                        selectedLabelColor = AppTheme.colorScheme.onBrand
                    ),
                    border = null,
                    label = { Text(text = text) },
                    modifier = Modifier.height(44.dp)
                )
            }

            if (uiState is NewsUiState.Success) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .statusBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp)
                ) {
                    Spacer(Modifier.width(16.dp))
                    Chip(text = stringResource(R.string.feature_notifications_all), selected = !uiState.showOnlyUnreadNews)
                    Chip(text = stringResource(R.string.feature_notifications_unread), selected = uiState.showOnlyUnreadNews)
                    Spacer(Modifier.width(16.dp))
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = (uiState as? NewsUiState.Success)?.hasUnreadNews == true,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AppContentButton(
                    onClick = onMarkAllAsRead,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalExternalPadding.current)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = stringResource(R.string.feature_notifications_markAsRead))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier
    ) { innerPadding ->
        when (uiState) {
            is NewsUiState.Loading -> {
                LoadingIndicatorBox(Modifier.fillMaxSize())
            }
            is NewsUiState.Success -> {
                LazyColumn(
                    contentPadding = innerPadding + PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(
                        items = uiState.news
                    ) { index, notification ->
                        BadgedBox(
                            badge = {
                                if (!notification.read) {
                                    Spacer(
                                        Modifier.size(12.dp).background(
                                            AppTheme.colorScheme.destructive,
                                            CircleShape
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.animateItem()
                        ) {
                            ListItem(
                                colors = ListItemDefaults.colors(
                                    containerColor = AppTheme.colorScheme.background1
                                ),
                                shapes = ListItemDefaults.shapes(
                                    shape = AppCardDefaults.getVerticalListShape(
                                        index = index,
                                        size = uiState.news.size
                                    )
                                ),
                                onClick = { onMarkAsRead(notification.id) },
                                overlineContent = {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = notification.source.uppercase(),
                                            color = AppTheme.colorScheme.foreground3,
                                            style = MaterialTheme.typography.labelLarge
                                        )

                                        val time = remember(notification.postedAt) {
                                            val notificationDateTime =
                                                notification.postedAt.toLocalDateTime(timeZone)
                                            val date = notificationDateTime.date

                                            val yesterday = today.minus(1, DateTimeUnit.DAY)
                                            val tomorrow = today.plus(1, DateTimeUnit.DAY)

                                            if (date in yesterday..tomorrow) {
                                                LocalDateTime.Format {
                                                    when (date) {
                                                        yesterday -> resources.getString(R.string.feature_widget_yesterday)
                                                        today -> resources.getString(R.string.feature_widget_today)
                                                        tomorrow -> resources.getString(R.string.feature_widget_tomorrow)
                                                        else -> error("Unreachable statement")
                                                    }.let { chars(it) }
                                                    char(' ')
                                                    hour()
                                                    char(':')
                                                    minute()
                                                }
                                            } else {
                                                if (date.inWeekOfOtherDate(today)) {
                                                    dateFormat(locale)
                                                } else {
                                                    LocalDateTime.Format {
                                                        dayOfWeek(
                                                            DayOfWeekNames.getAbbreviatedByLocale(
                                                                locale
                                                            )
                                                        )
                                                        char(' ')
                                                        hour()
                                                        char(':')
                                                        minute()
                                                    }
                                                }
                                            }.format(notificationDateTime)
                                        }
                                        Text(
                                            text = time,
                                            color = AppTheme.colorScheme.foreground3,
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                },
                                supportingContent = {
                                    Column {
                                        Text(
                                            text = notification.body,
                                            color = AppTheme.colorScheme.foreground2
                                        )
                                        notification.url?.let { url ->
                                            TextButton(
                                                onClick = { context.openTab(url) },
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = AppTheme.colorScheme.brand
                                                ),
                                                modifier = Modifier
                                                    .offset(
                                                        x = -ButtonDefaults.TextButtonContentPadding.calculateLeftPadding(
                                                            LayoutDirection.Ltr
                                                        )
                                                    )
                                            ) {
                                                Text(text = stringResource(R.string.feature_notifications_moreDetails))
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = notification.title,
                                    color = AppTheme.colorScheme.foreground1
                                )
                            }
                        }
                    }
                }
            }
            is NewsUiState.Empty -> {
                EmptyStateContent()
            }
        }
    }
}

@Composable
private fun EmptyStateContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.CatReadNews,
            contentDescription = null,
            tint = AppTheme.colorScheme.cat,
            modifier = Modifier.size(192.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.feature_news_noNews),
            color = AppTheme.colorScheme.foreground1,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}