package ru.bgitu.app.feature.teachers.teachers.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppAlertDialog
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.component.ButtonType

@Composable
fun SwitchToTeacherSuggestionDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppAlertDialog(
        title = stringResource(R.string.feature_teachers_areYouATeacher),
        description = stringResource(R.string.feature_teachers_areYouATeacher_description),
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        AppContentButton(
            onClick = onDismissRequest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.no)
            )
        }
        AppContentButton(
            onClick = onConfirm,
            type = ButtonType.SECONDARY,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.yes)
            )
        }
    }
}