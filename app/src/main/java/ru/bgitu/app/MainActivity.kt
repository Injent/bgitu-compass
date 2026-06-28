package ru.bgitu.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation3.runtime.NavKey
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject
import ru.bgitu.app.core.data.appupdater.AppUpdateManager
import ru.bgitu.app.core.data.notifications.AppNotifications
import ru.bgitu.app.core.datastore.SettingsRepository
import ru.bgitu.app.core.designsystem.theme.CompassTheme
import ru.bgitu.app.core.model.SubscribeTopic
import ru.bgitu.app.core.utilui.ClearFocusOnImeCloseEffect
import ru.bgitu.app.feature.schedule.ScheduleKey
import ru.bgitu.app.feature.teachers.TeachersKey
import ru.bgitu.app.ui.App
import ru.bgitu.app.ui.LaunchParams
import ru.bgitu.app.ui.rememberAppState
import ru.bgitu.app.ui.setStartAnimation
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()
    private val settings by inject<SettingsRepository>()
    private val appUpdateManager by inject<AppUpdateManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)
        Firebase.messaging.token
            .addOnSuccessListener {
                Log.e("TOKEN", it)
            }
            .addOnFailureListener {
                Log.e("TOKEN", "FAILED", it)
            }
        var uiState by mutableStateOf(MainActivityUiState())
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState = it }
            }
        }

        splashScreen.setStartAnimation()
        splashScreen.setKeepOnScreenCondition { uiState.loading }
        val userIsTeacher = runBlocking { settings.data.first().userIsTeacher }

        setContent {
            val appState = rememberAppState(
                startRoute = if (userIsTeacher) TeachersKey else ScheduleKey,
                settingsRepository = settings,
                appUpdateManager = appUpdateManager
            )

            ClearFocusOnImeCloseEffect()

            val features by appState.features.collectAsStateWithLifecycle()

            CompassTheme(
                darkTheme = uiState.shouldUseDarkTheme(isSystemInDarkTheme()),
                features = features
            ) {
                App(
                    appState = appState,
                    launchParams = getLaunchParams()
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideLargeFontSize()
    }

    // Preventing large fonts to destroy the UI
    private fun overrideLargeFontSize() {
        Configuration().apply {
            setTo(baseContext.resources.configuration)
            fontScale = fontScale.coerceAtMost(1.5f)
            applyOverrideConfiguration(this)
        }
    }

    private fun getLaunchParams(): LaunchParams {
        val uri = intent.data ?: return LaunchParams()

        return LaunchParams(
            date = uri.getQueryParameter("date")?.let { LocalDate.parse(it) },
            groupId = uri.getQueryParameter("groupId")?.toIntOrNull()
        )
    }
}
