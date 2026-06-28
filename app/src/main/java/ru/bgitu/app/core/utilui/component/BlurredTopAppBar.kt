package ru.bgitu.app.core.utilui.component

import androidx.compose.animation.core.EaseInQuart
import androidx.compose.foundation.background
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.LocalFeatures

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun BlurredTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val features = LocalFeatures.current

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = AppTheme.colorScheme.foreground1
        ),
        title = {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.merge(
                    fontWeight = FontWeight.SemiBold
                ),
                content = title
            )
        },
        navigationIcon = navigationIcon,
        modifier = modifier
            .then(
                if (features.blurEnabled) {
                    Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.ultraThick(AppTheme.colorScheme.background2)
                    ) {
                        blurRadius = 15.dp
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 1f,
                            endIntensity = 0f,
                            easing = EaseInQuart,
                            preferPerformance = !features.highQualityBlur
                        )
                        blurEnabled = true
                    }
                } else Modifier.background(AppTheme.colorScheme.background2)
            )
    )
}