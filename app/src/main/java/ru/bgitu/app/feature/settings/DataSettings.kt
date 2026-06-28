package ru.bgitu.app.feature.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.BackButton
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.designsystem.component.SwitchableOption
import ru.bgitu.app.core.designsystem.icon.Clock
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.Teacher
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.component.BlurredTopAppBar

@Composable
fun DataSettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onBack()
    }

    (uiState as? SettingsUiState.Success)?.let {
        DataSettingsScreenContent(
            settings = it.settings,
            onBack = onBack,
            onSetUserIsTeacher = viewModel::setUserIsTeacher,
            onSetSyncAllGroups = viewModel::setSyncAllGroups,
            onSetShouldShowTomorrowSchedule = viewModel::onSetShouldShowTomorrowSchedule
        )
    }
}

@Composable
private fun DataSettingsScreenContent(
    settings: UserEditableSettings,
    onBack: () -> Unit,
    onSetUserIsTeacher: (Boolean) -> Unit,
    onSetSyncAllGroups: (Boolean) -> Unit,
    onSetShouldShowTomorrowSchedule : (Boolean) -> Unit
) {
    val hazeState = rememberHazeState()
    val features = LocalFeatures.current

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            BlurredTopAppBar(
                title = { Text(text = stringResource(R.string.feature_settings_data)) },
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

            GroupStyledText(text = stringResource(R.string.feature_settings_teacherView))
            SwitchableOption(
                title = stringResource(R.string.feature_teachers_iAmATeacher),
                description = stringResource(R.string.feature_teachers_areYouATeacher_description),
                checked = settings.userIsTeacher,
                onCheckedChange = onSetUserIsTeacher,
                shape = AppCardDefaults.SingleCard,
                icon = Icons.Teacher
            )

            GroupStyledText(text = stringResource(R.string.feature_settings_sync))
            SwitchableOption(
                title = stringResource(R.string.feature_settings_syncAllGroups),
                description = stringResource(R.string.feature_settings_syncAllGroups_description),
                checked = settings.syncAllGroups,
                onCheckedChange = onSetSyncAllGroups,
                shape = AppCardDefaults.SingleCard,
                icon = Icons.Teacher
            )

            GroupStyledText(text = stringResource(R.string.feature_settings_misc))
            SwitchableOption(
                title = stringResource(R.string.feature_settings_shouldShowTomorrowSchedule),
                description = stringResource(R.string.feature_settings_shouldShowTomorrowSchedule_description),
                checked = settings.shouldShowTomorrowSchedule,
                onCheckedChange = onSetShouldShowTomorrowSchedule,
                shape = AppCardDefaults.SingleCard,
                icon = Icons.Clock
            )

            Spacer(Modifier.height(LocalExternalPadding.current.calculateBottomPadding() + 16.dp))
        }
    }
}