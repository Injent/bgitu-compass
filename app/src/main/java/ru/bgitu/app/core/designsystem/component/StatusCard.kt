package ru.bgitu.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun StatusCard(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        shape = AppCardDefaults.SingleCard,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .padding(16.dp)
        ) {
            icon()
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium,
            ) {
                title()
            }
        }
    }
}

@Composable
fun StatusCard(
    data: StatusCardData,
    modifier: Modifier = Modifier
) {
    StatusCard(
        title = {
            Text(
                text = stringResource(data.titleResId),
                textAlign = TextAlign.Center
            )
            data.description?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall
                        .merge(fontFamily = FontFamily.Monospace),
                    color = AppTheme.colorScheme.foreground2,
                    modifier = Modifier
                        .background(AppTheme.colorScheme.background4, RoundedCornerShape(12.dp))
                        .heightIn(max = 200.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                )
            }
        },
        icon = {
            Icon(
                imageVector = data.icon,
                contentDescription = null
            )
        },
        modifier = modifier
    )
}

data class StatusCardData(
    val titleResId: Int,
    val icon: ImageVector,
    val key: String? = null,
    val description: String? = null
)