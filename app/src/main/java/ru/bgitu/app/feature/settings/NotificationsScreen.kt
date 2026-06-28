package ru.bgitu.app.feature.settings

import android.Manifest
import android.R.attr.timeZone
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.datetime.TimeZone
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppAlertDialog
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.component.AppSelectableChip
import ru.bgitu.app.core.designsystem.component.BackButton
import ru.bgitu.app.core.designsystem.component.ButtonType
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.designsystem.component.SingleChoiceOption
import ru.bgitu.app.core.designsystem.component.SwitchableOption
import ru.bgitu.app.core.designsystem.icon.Alert
import ru.bgitu.app.core.designsystem.icon.Group
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.LiveNotification
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.model.SubscribeTopic
import ru.bgitu.app.core.util.checkNotificationsEnabled
import ru.bgitu.app.core.util.findActivity
import ru.bgitu.app.core.util.openNotificationSettings
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.component.BlurredTopAppBar
import ru.bgitu.app.core.utilui.formatAt
import ru.bgitu.app.feature.settings.component.MainOptionCard

@Composable
fun NotificationsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onBack()
    }

    (uiState as? SettingsUiState.Success)?.let {
        NotificationsScreenContent(
            settings = it.settings,
            onBack = onBack,
            onChangeLiveUpdateNotification = viewModel::setLiveUpdateNotificationEnabled,
            onChangeLiveUpdateGroup = viewModel::setLiveUpdateGroup,
            onNotificationsEnabledChange = viewModel::setNotificationsEnabled,
            onSubscribeToTopic = viewModel::subscribeToTopic,
            onUnsubscribeToTopic = viewModel::unsubscribeFromTopic
        )
    }
}

@Composable
private fun NotificationsScreenContent(
    settings: UserEditableSettings,
    onBack: () -> Unit,
    onChangeLiveUpdateNotification: (Boolean) -> Unit,
    onChangeLiveUpdateGroup: (Group) -> Unit,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onSubscribeToTopic: (SubscribeTopic) -> Unit,
    onUnsubscribeToTopic: (SubscribeTopic) -> Unit,
) {
    val resources = LocalResources.current
    val timeZone = remember { TimeZone.currentSystemDefault() }
    val hazeState = rememberHazeState()
    val features = LocalFeatures.current

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            BlurredTopAppBar(
                title = { Text(text = stringResource(R.string.feature_settings_notifications)) },
                navigationIcon = { BackButton(onClick = onBack) },
                hazeState = hazeState
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .then(
                    if (features.blurEnabled) {
                        Modifier.hazeSource(hazeState)
                    } else Modifier
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(innerPadding.calculateTopPadding() + 16.dp))

            OptionalEnableNotificationsCard()

            GroupStyledText(text = stringResource(R.string.feature_settings_currentLessons))
            SwitchableOption(
                title = stringResource(R.string.feature_settings_liveUpdateNotification),
                description = stringResource(R.string.feature_settings_liveUpdateNotification_description),
                checked = settings.liveUpdateNotification,
                onCheckedChange = onChangeLiveUpdateNotification,
                shape = if (settings.liveUpdateNotification) AppCardDefaults.TopCard else AppCardDefaults.SingleCard,
                icon = Icons.LiveNotification,
                supportingContent = {
                    val formatedTime = remember(timeZone, settings.liveUpdateTriggerTime) {
                        settings.liveUpdateTriggerTime?.formatAt(
                            resources = resources,
                            timeZone = timeZone
                        ) ?: resources.getString(R.string.feature_settings_liveUpdateNotification_notScheduled)
                    }
                    AnimatedVisibility(
                        visible = settings.liveUpdateNotification
                    ) {
                        Text(
                            text = formatedTime,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.colorScheme.brand,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            )

            AnimatedVisibility(
                visible = settings.liveUpdateNotification,
                enter = fadeIn() + expandVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ),
                exit = fadeOut() + shrinkVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy))
            ) {
                SingleChoiceOption(
                    title = stringResource(R.string.feature_widget_group),
                    icon = Icons.Group,
                    shape = AppCardDefaults.BottomCard,
                    enabled = settings.liveUpdateNotification
                ) {
                    if (settings.userGroups.isEmpty()) {
                        Text(
                            text = stringResource(R.string.feature_settings_noGroupsAvailable),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    if (settings.userGroups.size == 1) {
                        LaunchedEffect(Unit) {
                            onChangeLiveUpdateGroup(settings.userGroups.single())
                        }
                    }
                    settings.userGroups.forEach { group ->
                        AppSelectableChip(
                            selected = group == settings.liveUpdateGroup,
                            onClick = {
                                if (!settings.liveUpdateNotification) return@AppSelectableChip
                                onChangeLiveUpdateGroup(group)
                            },
                            label = { Text(text = group.name) }
                        )
                    }
                }
            }

            GroupStyledText(text = stringResource(R.string.feature_settings_notifications))
            SwitchableOption(
                title = stringResource(R.string.feature_settings_notifications),
                checked = settings.isNotificationsEnabled,
                onCheckedChange = onNotificationsEnabledChange,
                shape = AppCardDefaults.TopCard,
                icon = Icons.Alert,
            )
            SwitchableOption(
                title = stringResource(R.string.feature_settings_notifications_scheduleChanges),
                checked = SubscribeTopic.SCHEDULE_CHANGE in settings.subscribedTopics,
                onCheckedChange = { checked ->
                    if (checked) {
                        onSubscribeToTopic(SubscribeTopic.SCHEDULE_CHANGE)
                    } else {
                        onUnsubscribeToTopic(SubscribeTopic.SCHEDULE_CHANGE)
                    }
                },
                shape = AppCardDefaults.MiddleCard,
                enabled = settings.isNotificationsEnabled
            )
            SwitchableOption(
                title = stringResource(R.string.feature_settings_notifications_broadcast),
                checked = SubscribeTopic.BROADCAST in settings.subscribedTopics,
                onCheckedChange = { checked ->
                    if (checked) {
                        onSubscribeToTopic(SubscribeTopic.BROADCAST)
                    } else {
                        onUnsubscribeToTopic(SubscribeTopic.BROADCAST)
                    }
                },
                shape = AppCardDefaults.BottomCard,
                enabled = settings.isNotificationsEnabled
            )

            Spacer(Modifier.height(LocalExternalPadding.current.calculateBottomPadding() + 16.dp))
        }
    }
}

@Composable
private fun OptionalEnableNotificationsCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    var showRationaleDialog by remember { mutableStateOf(false) }
    var areNotificationsEnabled by remember {
        mutableStateOf(context.checkNotificationsEnabled())
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            areNotificationsEnabled = context.checkNotificationsEnabled()

            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val showRationaleSystem = activity?.shouldShowRequestPermissionRationale(
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == true

                    if (!showRationaleSystem) {
                        showRationaleDialog = true
                    }
                }
            }
        }
    )

    LifecycleResumeEffect(Unit) {
        areNotificationsEnabled = context.checkNotificationsEnabled()
        if (areNotificationsEnabled) {
            showRationaleDialog = false
        }
        onPauseOrDispose {}
    }

    fun onRequestPermissionClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                context.openNotificationSettings()
            } else {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            context.openNotificationSettings()
        }
    }

    AnimatedVisibility(
        visible = !areNotificationsEnabled,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        MainOptionCard(
            text = {
                Text(
                    text = stringResource(R.string.feature_settings_notificationsAreDisallowed),
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            modifier = modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = ::onRequestPermissionClick,
            ) {
                Text(
                    text = stringResource(R.string.feature_settings_allow),
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colorScheme.brand,
                    maxLines = 1
                )
            }
        }
    }

    if (showRationaleDialog) {
        AppAlertDialog(
            title = stringResource(R.string.feature_settings_allowNotifications),
            description = stringResource(R.string.feature_settings_allowNotifications_description),
            onDismissRequest = { showRationaleDialog = false }
        ) {
            AppContentButton(
                onClick = { showRationaleDialog = false },
                type = ButtonType.SECONDARY,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(android.R.string.cancel))
            }
            AppContentButton(
                onClick = {
                    context.openNotificationSettings()
                    showRationaleDialog = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.feature_settings_allow))
            }
        }
    }
}