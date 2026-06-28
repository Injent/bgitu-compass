package ru.bgitu.app.feature.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.compose
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.glance.appwidget.host.AppWidgetHost
import com.google.android.glance.appwidget.host.rememberAppWidgetHostState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import org.koin.android.ext.android.inject
import ru.bgitu.app.MainActivity
import ru.bgitu.app.R
import ru.bgitu.app.core.data.schedule.GroupsRepository
import ru.bgitu.app.core.designsystem.component.AppCardDefaults
import ru.bgitu.app.core.designsystem.component.AppContentButton
import ru.bgitu.app.core.designsystem.component.AppSelectableChip
import ru.bgitu.app.core.designsystem.component.GroupStyledText
import ru.bgitu.app.core.designsystem.component.SingleChoiceOption
import ru.bgitu.app.core.designsystem.component.SliderOption
import ru.bgitu.app.core.designsystem.icon.Blur
import ru.bgitu.app.core.designsystem.icon.Group
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.Pallete
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.designsystem.theme.CompassTheme
import ru.bgitu.app.core.model.Group
import ru.bgitu.app.core.model.WidgetPrefs
import ru.bgitu.app.core.model.WidgetTheme
import ru.bgitu.app.core.util.now
import ru.bgitu.app.feature.widget.data.WidgetDataRepository
import ru.bgitu.app.feature.widget.glance.CompassWidget
import ru.bgitu.app.feature.widget.glance.ui.CompassPreviewWidget

class WidgetSettingsActivity : ComponentActivity() {

    private val widgetDataRepository by inject<WidgetDataRepository>()
    private val groupsRepository by inject<GroupsRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) finish()

        val glanceAppWidgetManager = GlanceAppWidgetManager(this)
        val glanceId = glanceAppWidgetManager.getGlanceIdBy(appWidgetId = appWidgetId)

        var widgetState by mutableStateOf(WidgetPrefs())

        lifecycleScope.launch {
            widgetState = widgetDataRepository.getPrefs(appWidgetId = appWidgetId)
        }

        setContent {
            CompassTheme {
                val groups by groupsRepository.groups.collectAsStateWithLifecycle(initialValue = emptyList())

                WidgetSettingsScreen(
                    glanceId = glanceId,
                    groups = groups,
                    state = widgetState,
                    onChange = { widgetState = it },
                    onExit = { code ->
                        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        setResult(code, resultValue)
                        if (code == RESULT_OK) {
                            lifecycleScope.launch {
                                widgetDataRepository.updatePrefs(appWidgetId = appWidgetId) {
                                    widgetState.copy(selectedDate = LocalDate.now())
                                }
                                withContext(Dispatchers.IO) {
                                    CompassWidget().update(context = applicationContext, id = glanceId)
                                }
                                finish()
                            }
                        } else {
                            finish()
                        }
                    }
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideLargeFontSize()
    }

    private fun overrideLargeFontSize() {
        Configuration().apply {
            setTo(baseContext.resources.configuration)
            fontScale = fontScale.coerceAtMost(1.5f)
            applyOverrideConfiguration(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WidgetSettingsScreen(
    glanceId: GlanceId,
    groups: List<Group>,
    state: WidgetPrefs,
    onChange: (WidgetPrefs) -> Unit,
    onExit: (Int) -> Unit
) {
    val context = LocalContext.current
    val previewWidget = remember { CompassPreviewWidget() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background2
                ),
                title = {
                    Text(
                        text = stringResource(R.string.feature_widget_settings),
                        style = MaterialTheme.typography.titleMedium.merge(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = { onExit(Activity.RESULT_CANCELED) }
                    ) {
                        Text(
                            text = stringResource(android.R.string.cancel),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppTheme.colorScheme.foreground1
                        )
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = state.group != null,
                        enter = slideInHorizontally { it },
                    ) {
                        TextButton(
                            onClick = { onExit(Activity.RESULT_OK) },
                            shape = CircleShape,
                            colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colorScheme.brand)
                        ) {
                            Text(
                                text = stringResource(R.string.feature_widget_save),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawWithContent {
                        drawRoundRect(
                            color = Color.Transparent,
                            blendMode = BlendMode.Clear,
                            cornerRadius = CornerRadius(
                                x = 24.dp.toPx(),
                                y = 24.dp.toPx()
                            )
                        )
                        drawContent()
                    }
                    .padding(vertical = 16.dp)
            ) {
                var widgetSize by remember { mutableStateOf(DpSize(360.dp, 224.dp)) }
                val hostState = rememberAppWidgetHostState()

                LaunchedEffect(state) {
                    val remoteViews = previewWidget.compose(
                        context = context,
                        id = glanceId,
                        size = widgetSize,
                        state = state
                    )
                    hostState.updateAppWidget(remoteViews)
                }

                AppWidgetHost(
                    displaySize = widgetSize,
                    state = hostState,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                SingleChoiceOption(
                    title = stringResource(R.string.feature_widget_group),
                    icon = Icons.Group,
                    shape = AppCardDefaults.SingleCard
                ) {
                    if (groups.isEmpty()) {
                        TextButton(
                            onClick = {
                                Intent(context, MainActivity::class.java).let {
                                    context.startActivity(it)
                                }
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.feature_widget_selectGroup),
                                color = AppTheme.colorScheme.brand
                            )
                        }
                    }

                    if (groups.size == 1) {
                        LaunchedEffect(Unit) {
                            onChange(state.copy(group = groups.single()))
                        }
                    }

                    groups.forEach { group ->
                        AppSelectableChip(
                            selected = group == state.group,
                            onClick = {
                                onChange(state.copy(group = group))
                            },
                            label = { Text(text = group.name) }
                        )
                    }
                }
                GroupStyledText(
                    text = stringResource(R.string.feature_widget_design)
                )
                SliderOption(
                    title = stringResource(R.string.feature_widget_transparency),
                    icon = Icons.Blur,
                    value = state.alpha,
                    onValueChange = { alpha ->
                        onChange(state.copy(alpha = alpha))
                    },
                    steps = 9,
                    shape = AppCardDefaults.TopCard
                )
                SingleChoiceOption(
                    title = stringResource(R.string.feature_widget_theme),
                    icon = Icons.Pallete,
                    shape = AppCardDefaults.BottomCard
                ) {
                    WidgetTheme.entries.forEach { theme ->
                        if (theme == WidgetTheme.DYNAMIC && SDK_INT < 31) return@forEach

                        AppSelectableChip(
                            selected = theme == state.uiTheme,
                            onClick = {
                                onChange(state.copy(uiTheme = theme))
                            },
                            label = {
                                Text(
                                    text = when (theme) {
                                        WidgetTheme.AUTO -> R.string.feature_widget_theme_auto
                                        WidgetTheme.DYNAMIC -> R.string.feature_widget_theme_dynamic
                                        WidgetTheme.DARK -> R.string.feature_widget_theme_dark
                                        WidgetTheme.LIGHT -> R.string.feature_widget_theme_light
                                    }.let { stringResource(it) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}