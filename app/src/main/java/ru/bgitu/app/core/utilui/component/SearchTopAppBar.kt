package ru.bgitu.app.core.utilui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.delay
import ru.bgitu.app.core.designsystem.component.AppSearchBar
import ru.bgitu.app.core.designsystem.icon.ArrowBack
import ru.bgitu.app.core.designsystem.icon.Close
import ru.bgitu.app.core.designsystem.icon.CrossInSwitch
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.theme.AppTheme
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.core.utilui.TaperedBorderSide
import ru.bgitu.app.core.utilui.taperedBorder

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun SearchTopAppBar(
    hazeState: HazeState,
    onBack: () -> Unit,
    searchState: TextFieldState,
    searchBarModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    searchBarPlaceholder: String? = null
) {
    val features = LocalFeatures.current

    Box(
        modifier = modifier
            .fillMaxWidth()
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
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 16.dp)

    ) {
        val ime = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        var alreadyWasFocused by rememberSaveable { mutableStateOf(false) }

        AppSearchBar(
            containerColor = AppTheme.colorScheme.background1,
            state = searchState,
            placeholder = searchBarPlaceholder ?: stringResource(android.R.string.search_go),
            leadingIcon = {
                IconButton(
                    onClick = {
                        ime?.hide()
                        onBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = searchState.text.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        onClick = { searchState.clearText() }
                    ) {
                        Icon(
                            imageVector = Icons.Close,
                            contentDescription = null,
                        )
                    }
                }
            },
            modifier = searchBarModifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .clip(CircleShape)
                .taperedBorder(
                    side = TaperedBorderSide.Top,
                    strokeWidth = 1.dp,
                    color = AppTheme.colorScheme.foreground1.copy(.15f),
                    shape = CircleShape
                )
                .taperedBorder(
                    side = TaperedBorderSide.Bottom,
                    strokeWidth = 1.dp,
                    color = AppTheme.colorScheme.foreground1.copy(.25f),
                    shape = CircleShape
                )
        )

        LaunchedEffect(Unit) {
            if (alreadyWasFocused) return@LaunchedEffect
            alreadyWasFocused = true
            // Waiting for SearchField to render
            delay(300)
            focusRequester.requestFocus()
        }
    }
}