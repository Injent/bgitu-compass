package ru.bgitu.app.feature.schedule.component

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.component.AppAlertDialog
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.component.ButtonType
import ru.bgitu.app.core.util.findActivity
import ru.bgitu.app.core.util.openNotificationSettings

@Composable
fun TurnOnNotificationsDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    fun onRequestPermissionClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                context.openNotificationSettings()
            } else {
                val shouldLaunchSettings = ActivityCompat.shouldShowRequestPermissionRationale(
                    context.findActivity() ?: return,
                    Manifest.permission.POST_NOTIFICATIONS
                )
                if (shouldLaunchSettings) {
                    context.openNotificationSettings()
                } else {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            context.openNotificationSettings()
        }
    }

    AppAlertDialog(
        title = stringResource(R.string.feature_settings_wantToReceiveNotifications),
        description = stringResource(R.string.feature_settings_allowNotifications_description),
        onDismissRequest = onDismissRequest
    ) {
        AppContentButton(
            onClick = {
                onRequestPermissionClick()
                onDismissRequest()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.yes))
        }
        AppContentButton(
            onClick = onDismissRequest,
            type = ButtonType.SECONDARY,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.no))
        }
    }
}