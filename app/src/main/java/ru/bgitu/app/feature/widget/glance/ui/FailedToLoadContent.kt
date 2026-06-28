package ru.bgitu.app.feature.widget.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import ru.bgitu.app.R

@Composable
fun FailedToLoadContent(
    title: String,
    actionText: String,
    action: Action,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
        modifier = GlanceModifier.fillMaxSize()
    ) {
        Text(
            text = title,
            style = LayoutTextStyles.titleText(TextAlign.Center)
        )
        Spacer(GlanceModifier.height(16.dp))
        Button(
            text = actionText,
            onClick = action
        )
    }
}