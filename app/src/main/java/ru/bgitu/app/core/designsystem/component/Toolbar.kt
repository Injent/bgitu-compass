package ru.bgitu.app.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = AppTheme.colorScheme.background2,
        navigationIconContentColor = AppTheme.colorScheme.foreground1,
        titleContentColor = AppTheme.colorScheme.foreground1
    ),
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium
            ) {
                title()
            }
        },
        navigationIcon = {
            Box(modifier = Modifier.padding(start = 8.dp)) {
                navigationIcon()
            }
        },
        actions = actions,
        colors = colors,
        modifier = modifier
    )
}