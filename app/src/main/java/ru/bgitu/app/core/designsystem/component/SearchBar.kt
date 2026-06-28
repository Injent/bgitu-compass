package ru.bgitu.app.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.bgitu.app.core.designsystem.theme.AppTheme

@Composable
fun AppSearchBar(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    containerColor: Color = AppTheme.colorScheme.background1,
    placeholder: String = "",
) {
    val textStyle = LocalTextStyle.current.merge(color = AppTheme.colorScheme.foreground1)
    val cursorColor = AppTheme.colorScheme.brand

    BasicTextField(
        state = state,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            autoCorrectEnabled = false
        ),
        cursorBrush = remember { SolidColor(cursorColor) },
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = textStyle,
        modifier = modifier,
        readOnly = readOnly,
        decorator = { innerTextField ->
            @Composable
            fun Content() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(48.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides AppTheme.colorScheme.foreground1
                    ) {
                        leadingIcon?.invoke()
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    ) {
                        innerTextField()
                        Text(
                            text = placeholder,
                            color = AppTheme.colorScheme.foreground2,
                            modifier = Modifier
                                .alpha(if (state.text.isEmpty()) 1f else 0f)
                        )
                    }
                    CompositionLocalProvider(
                        LocalContentColor provides AppTheme.colorScheme.foreground1
                    ) {
                        trailingIcon?.invoke()
                    }
                }
            }

            if (readOnly) {
                Surface(
                    shape = CircleShape,
                    color = containerColor,
                    onClick = onClick,
                    content = { Content() },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                Surface(
                    shape = CircleShape,
                    color = containerColor,
                    content = { Content() },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    )
}