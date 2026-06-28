package ru.bgitu.app.feature.teachers.schedule.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import ru.bgitu.app.core.designsystem.component.BackButton
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.utilui.LocalFeatures

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TeacherScheduleTopAppBar(
    hazeState: HazeState,
    teacher: Teacher,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    val features = LocalFeatures.current

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = AppTheme.colorScheme.foreground1,
            containerColor = Color.Transparent
        ),
        title = {
            BasicText(
                text = teacher.fullName,
                style = MaterialTheme.typography.titleMedium.merge(
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = LocalContentColor.current
                ),
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 14.sp,
                    maxFontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
                maxLines = 1,
            )
        },
        navigationIcon = {
            BackButton(onClick = onBack)
        },
        actions = actions,
        modifier = modifier
            .then(
                if (features.blurEnabled) {
                    Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.ultraThick(AppTheme.colorScheme.background2)
                    ) {
                        blurEnabled = true
                        blurRadius = 15.dp
                        progressive = HazeProgressive.verticalGradient(
                            easing = CubicBezierEasing(0.5f, 0f, 1f, 1f),
                            startIntensity = 1f,
                            endIntensity = 0f,
                            preferPerformance = !features.highQualityBlur
                        )
                    }
                } else Modifier.background(AppTheme.colorScheme.background2)
            )
    )
}