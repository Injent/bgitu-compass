package ru.bgitu.app.feature.settings

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.icon.AboutApp
import ru.bgitu.app.core.designsystem.icon.Alert
import ru.bgitu.app.core.designsystem.icon.ArrowSmall
import ru.bgitu.app.core.designsystem.icon.ExternalLink
import ru.bgitu.app.core.designsystem.icon.Help
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.Max
import ru.bgitu.app.core.designsystem.icon.Pallete
import ru.bgitu.app.core.designsystem.icon.Sync
import ru.bgitu.app.core.designsystem.icon.Telegram
import ru.bgitu.app.core.designsystem.icon.VK
import ru.bgitu.app.core.designsystem.icon.Widget
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.util.TELEGRAM_BOT_URL
import ru.bgitu.app.core.util.openTab
import ru.bgitu.app.core.util.requestWidgetPin
import ru.bgitu.app.core.util.shouldShowWidgetPinRequest
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.feature.settings.component.InfoCard
import ru.bgitu.app.feature.settings.component.SettingsTopLevelCard

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigate: (NavKey) -> Unit
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(onBack = onBack)

    SettingsScreenContent(
        uiState = uiState,
        onNavigate = onNavigate
    )
}

@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    onNavigate: (NavKey) -> Unit
) {
    Scaffold(
        containerColor = AppTheme.colorScheme.background2
    ) { innerPadding ->
        val topPadding = innerPadding.calculateTopPadding() + 16.dp
        val bottomPadding = LocalExternalPadding.current.calculateBottomPadding() + 16.dp
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(topPadding))

            SettingsPanel(
                uiState = uiState,
                onNavigate = onNavigate,
            )

            Spacer(Modifier.height(bottomPadding))
        }
    }
}

@Composable
private fun SettingsPanel(
    uiState: SettingsUiState,
    onNavigate: (NavKey) -> Unit,
) {
    val context = LocalContext.current

    SettingsTopLevelCard(
        title = stringResource(R.string.feature_settings_design),
        description = stringResource(R.string.feature_settings_design_description),
        icon = Icons.Pallete,
        iconTint = AppTheme.colorScheme.designIcon,
        onClick = { onNavigate(DesignSettingsKey) },
        shape = AppCardDefaults.TopCard
    )
    SettingsTopLevelCard(
        title = stringResource(R.string.feature_settings_notifications),
        description = stringResource(R.string.feature_settings_notifications_description),
        icon = Icons.Alert,
        iconTint = AppTheme.colorScheme.notificationIcon,
        onClick = { onNavigate(NotificationsSettingsKey) },
        shape = AppCardDefaults.MiddleCard
    )
    val shouldShowWidgetPin = remember { shouldShowWidgetPinRequest(context) }

    SettingsTopLevelCard(
        title = stringResource(R.string.feature_settings_data),
        description = stringResource(R.string.feature_settings_data_description),
        icon = Icons.Sync,
        iconTint = AppTheme.colorScheme.syncIcon,
        onClick = { onNavigate(DataSettingsKey) },
        shape = if (shouldShowWidgetPin) AppCardDefaults.MiddleCard else AppCardDefaults.BottomCard
    )
    if (shouldShowWidgetPin) {
        SettingsTopLevelCard(
            title = stringResource(R.string.feature_settings_widget),
            description = stringResource(R.string.feature_settings_widget_description),
            icon = Icons.Widget,
            iconTint = AppTheme.colorScheme.widgetIcon,
            onClick = {
                requestWidgetPin(context)
            },
            shape = AppCardDefaults.BottomCard
        )
    }

    Spacer(Modifier.height(32.dp))

    InfoCard(
        icon = { Icon(imageVector = Icons.Help, contentDescription = null) },
        label = stringResource(R.string.feature_settings_help),
        shape = AppCardDefaults.TopCard
    ) {
        AnimatedVisibility(
            visible = uiState is SettingsUiState.Success,
            enter = fadeIn()
        ) {
            (uiState as? SettingsUiState.Success)?.settings?.let { settings ->
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    settings.telegramLinkSupport?.let {
                        IconButton(
                            onClick = { context.openTab(it) }
                        ) {
                            Icon(
                                imageVector = Icons.Telegram,
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }
                    }
                    settings.vkLinkSupport?.let {
                        IconButton(
                            onClick = { context.openTab(it) }
                        ) {
                            Icon(
                                imageVector = Icons.VK,
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }
                    }
                    settings.maxLinkSupport?.let {
                        IconButton(
                            onClick = { context.openTab(it) }
                        ) {
                            Icon(
                                imageVector = Icons.Max,
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.width(4.dp))
    }
    InfoCard(
        icon = { Icon(painter = painterResource(R.drawable.ic_notification), contentDescription = null) },
        label = stringResource(R.string.feature_settings_telegramBot),
        shape = AppCardDefaults.MiddleCard,
        onClick = { context.openTab(TELEGRAM_BOT_URL) }
    ) {
        Icon(
            imageVector = Icons.ExternalLink,
            contentDescription = null
        )
        Spacer(Modifier.width(12.dp))
    }
    InfoCard(
        icon = { Icon(imageVector = Icons.AboutApp, contentDescription = null) },
        label = stringResource(R.string.feature_settings_aboutApp),
        shape = AppCardDefaults.BottomCard,
        onClick = { onNavigate(AboutAppKey) }
    ) {
        Icon(
            imageVector = Icons.ArrowSmall,
            contentDescription = null
        )
        Spacer(Modifier.width(16.dp))
    }
}