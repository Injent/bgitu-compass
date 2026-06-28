package ru.bgitu.app.feature.update

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.icon.CatInBox
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.util.CollectEvent
import ru.bgitu.app.navigation.Navigator
import kotlin.math.roundToInt

@Parcelize
@Serializable
data object UpdateNavKey : NavKey, Parcelable

fun EntryProviderScope<NavKey>.updateScreen(navigator: Navigator) {
    entry<UpdateNavKey> {
        UpdateScreen(navigator = navigator)
    }
}

@Composable
private fun UpdateScreen(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    viewModel: UpdateViewModel = koinViewModel()
) {
    val installStatus by viewModel.installStatus.collectAsStateWithLifecycle()

    CollectEvent(viewModel.navigateToRoot) {
        navigator.goBack()
    }
    BackHandler { /* Nothing */ }

    UpdateScreenContent(
        installStatus = installStatus,
        onUpdateButtonClick = viewModel::startUpdateFlow,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun UpdateScreenContent(
    installStatus: InstallStatus,
    onUpdateButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background2
                ),
                title = {
                    Text(
                        text = stringResource(R.string.feature_update_updateAvailable),
                        color = AppTheme.colorScheme.foreground1,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        },
        bottomBar = {
            Column {
                AnimatedVisibility(
                    visible = installStatus is InstallStatus.Failed,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.feature_update_error),
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppTheme.colorScheme.destructive,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .background(AppTheme.colorScheme.background3, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    )
                }
                AppContentButton(
                    onClick = onUpdateButtonClick,
                    enabled = installStatus !is InstallStatus.Downloading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = when (installStatus) {
                            is InstallStatus.Downloading -> stringResource(R.string.feature_update_downloading)
                            is InstallStatus.Failed -> stringResource(R.string.feature_update_updateFailed)
                            is InstallStatus.Pending -> stringResource(R.string.feature_update_pending)
                            is InstallStatus.ReadyToInstall -> stringResource(R.string.feature_update_install)
                        }
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
            Icon(
                imageVector = Icons.CatInBox,
                contentDescription = null,
                tint = AppTheme.colorScheme.cat,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(192.dp)
            )
        }
    }
}