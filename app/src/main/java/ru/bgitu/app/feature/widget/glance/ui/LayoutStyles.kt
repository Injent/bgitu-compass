package ru.bgitu.app.feature.widget.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle

enum class LayoutSize(val maxWidth: Dp) {
    // 2 column list - compact view e.g. reduced fonts.
    Small(maxWidth = 180.dp),

    // Single column list
    Medium(maxWidth = 260.dp),

    // 2 Column Grid
    Large(maxWidth = 644.dp);

    operator fun LayoutSize.compareTo(other: Dp): Int {
        return this.maxWidth.compareTo(other)
    }

    operator fun Dp.compareTo(other: LayoutSize): Int {
        return this.compareTo(other.maxWidth)
    }

    companion object {

        @Composable
        fun fromLocalSize(): LayoutSize {
            val width = LocalSize.current.width

            return if (width >= Medium.maxWidth) {
                Large
            } else if (width >= Small.maxWidth) {
                Medium
            } else {
                Small
            }
        }
    }
}

object LayoutTextStyles {
    /**
     * Style for the text displayed as title within each item.
     */
    @Composable
    fun titleText(textAlign: TextAlign = TextAlign.Start): TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = if (LayoutSize.fromLocalSize() == LayoutSize.Small) {
            14.sp // M3 Title Small
        } else {
            16.sp // M3 Title Medium
        },
        textAlign = textAlign,
        color = GlanceTheme.colors.onSurface
    )

    @Composable
    fun titleBarText(textAlign: TextAlign = TextAlign.Start): TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = GlanceTheme.colors.onSurface,
        textAlign = textAlign
    )

    /**
     * Style for the text displayed as supporting text within each item.
     */
    @Composable
    fun supportingText(textAlign: TextAlign = TextAlign.Start): TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp, // M3 Label Medium
            textAlign = textAlign,
            color = GlanceTheme.colors.onSurfaceVariant
        )
}
