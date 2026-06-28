package ru.bgitu.app.core.utilui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun LargeStatusCard(
    title: String,
    subtitle: String,
    action: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(34.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        modifier = modifier
            .width(IntrinsicSize.Max)
            .widthIn(min = 200.dp)
            .background(AppTheme.colorScheme.background3, shape)
            .padding(top = 24.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides AppTheme.colorScheme.cat) {
            icon()
        }
        Text(
            text = buildAnnotatedString {
                appendLine(title)
                withStyle(
                    SpanStyle(
                        color = AppTheme.colorScheme.foreground2,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(subtitle)
                }
            },
            color = AppTheme.colorScheme.foreground1,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 14.dp, top = 8.dp)
                .padding(horizontal = 32.dp)
        )
        AppContentButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(bottom = 14.dp)
        ) {
            Text(text = action)
        }
    }
}
