package ru.bgitu.app.feature.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.rememberHazeState
import ru.bgitu.app.BuildConfig
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.BackButton
import ru.bgitu.app.core.designsystem.icon.ArrowSmall
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.util.USER_AGREEMENT_URL
import ru.bgitu.app.core.util.openTab
import ru.bgitu.app.core.utilui.LocalExternalPadding
import ru.bgitu.app.core.utilui.component.BlurredTopAppBar
import ru.bgitu.app.feature.settings.component.DevContactCard
import ru.bgitu.app.feature.settings.model.DevContact

@Composable
fun AboutAppScreen(
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    AboutAppScreenContent(
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AboutAppScreenContent(
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = AppTheme.colorScheme.background2,
        topBar = {
            BlurredTopAppBar(
                title = { Text(text = stringResource(R.string.feature_settings_aboutApp)) },
                navigationIcon = {
                    BackButton(onClick = onBack)
                },
                hazeState = rememberHazeState() // Не используется
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(LocalExternalPadding.current)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(216.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                        .merge(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    color = AppTheme.colorScheme.foreground1
                )
                Text(
                    text = buildString {
                        appendLine()
                        stringResource(R.string.feature_settings_appVersionName, BuildConfig.VERSION_NAME)
                            .let(::append)
                        appendLine()
                        appendLine()
                        append(BuildConfig.VERSION_CODE)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.foreground2,
                    textAlign = TextAlign.Center
                )
            }
            UserAgreement()
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                DevContact.entries.shuffled().forEachIndexed { index, devContact ->
                    DevContactCard(
                        contact = devContact,
                        shape = AppCardDefaults.getVerticalListShape(
                            index = index,
                            size = DevContact.entries.size
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun UserAgreement(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                context.openTab(USER_AGREEMENT_URL)
            }
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.feature_settings_userAgreement),
            style = MaterialTheme.typography.bodyLarge,
            color = AppTheme.colorScheme.foreground1
        )
        Icon(
            imageVector = Icons.ArrowSmall,
            tint = AppTheme.colorScheme.foreground1,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 8.dp)
        )
    }
}