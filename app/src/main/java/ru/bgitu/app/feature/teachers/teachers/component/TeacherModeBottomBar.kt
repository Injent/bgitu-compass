package ru.bgitu.app.feature.teachers.teachers.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.icon.Edit
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.utilui.LocalFeatures

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun TeacherModeBottomBar(
    teacher: Teacher?,
    onClickSettings: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val features = LocalFeatures.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(AppCardDefaults.SingleCard)
            .then(
                if (features.blurEnabled) {
                    Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(AppTheme.colorScheme.background2)
                    ) {
                        blurRadius = 15.dp
                        blurEnabled = true
                    }
                } else Modifier.background(AppTheme.colorScheme.background2)
            )
            .padding(4.dp)
    ) {
        IconButton(
            onClick = onClickSettings,
            modifier = Modifier
                .padding(horizontal = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Edit,
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground2
            )
        }

        Text(
            text = teacher?.fullName ?: stringResource(R.string.feature_teachers_notSelected),
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.foreground1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}