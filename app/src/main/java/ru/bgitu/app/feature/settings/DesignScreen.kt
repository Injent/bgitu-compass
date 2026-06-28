package ru.bgitu.app.feature.settings

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import ru.bgitu.app.core.designsystem.component.SwitchableOption
import ru.bgitu.app.core.designsystem.icon.Blur
import ru.bgitu.app.core.designsystem.icon.Hd
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.UiTheme
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.component.BlurredTopAppBar
import ru.bgitu.app.core.utilui.plus
import ru.bgitu.app.feature.settings.component.PhoneUiSection

@Composable
fun DesignScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onBack()
    }

    (uiState as? SettingsUiState.Success)?.let {
        DesignScreenContent(
            settings = it.settings,
            onBack = onBack,
            onChangeUiTheme = viewModel::updateUiTheme,
            onChangeBlurQuality = viewModel::updateBlurQuality,
            onChangeBlurEnabled = viewModel::updateBlurEnabled
        )
    }
}

@Composable
private fun DesignScreenContent(
    settings: UserEditableSettings,
    onBack: () -> Unit,
    onChangeUiTheme: (UiTheme) -> Unit,
    onChangeBlurQuality: (Boolean) -> Unit,
    onChangeBlurEnabled: (Boolean) -> Unit,
) {
    val hazeState = rememberHazeState()
    val features = LocalFeatures.current

    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            BlurredTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.feature_settings_design)
                    )
                },
                navigationIcon = {
                    BackButton(
                        onClick = onBack
                    )
                },
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

            PhoneUiSection(
                uiTheme = settings.uiTheme,
                onChangeUiTheme = onChangeUiTheme
            )
            if (SDK_INT >= 31) {
                Spacer(Modifier.height(30.dp))
                SwitchableOption(
                    title = stringResource(R.string.feature_settings_blur),
                    description = stringResource(R.string.feature_settings_blur_description),
                    shape = AppCardDefaults.TopCard,
                    checked = settings.blurEnabled,
                    onCheckedChange = onChangeBlurEnabled,
                    icon = Icons.Blur
                )
                SwitchableOption(
                    title = stringResource(R.string.feature_settings_highQualityBlur),
                    description = stringResource(R.string.feature_settings_highQualityBlur_description),
                    shape = AppCardDefaults.BottomCard,
                    checked = settings.highQualityBlur,
                    onCheckedChange = onChangeBlurQuality,
                    enabled = settings.blurEnabled,
                    icon = Icons.Hd
                )
            }

            Spacer(Modifier.height(LocalExternalPadding.current.calculateBottomPadding() + 16.dp))
        }
    }
}