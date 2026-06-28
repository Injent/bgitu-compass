package ru.bgitu.app.feature.schedule.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class ZParticle(
    val id: Long,
    val startXOffset: Dp,
    val size: Int,
    val rotation: Float
)

class ZParticleState(
    val particle: ZParticle,
    val onFinished: () -> Unit
) {
    val alpha = Animatable(0f)
    val offsetY = Animatable(0.dp, Dp.VectorConverter)
    val driftX = Animatable(0.dp, Dp.VectorConverter)

    suspend fun animate() {
        coroutineScope {
            launch {
                offsetY.animateTo(
                    targetValue = (-100).dp,
                    animationSpec = tween(2500, easing = LinearOutSlowInEasing)
                )
                onFinished()
            }
            launch {
                driftX.animateTo(
                    targetValue = particle.startXOffset / 2,
                    animationSpec = tween(2500, easing = LinearEasing)
                )
            }
            launch {
                alpha.animateTo(1f, tween(500))
                alpha.animateTo(0f, tween(2000))
            }
        }
    }
}

fun DrawScope.drawZSymbol(
    isFireZ: Boolean,
    state: ZParticleState,
    symbolColor: Color,
    fireZPainter: Painter,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    baseOffset: Offset
) {
    val particle = state.particle
    val x = baseOffset.x + (particle.startXOffset + state.driftX.value).toPx()
    val y = baseOffset.y + (state.offsetY.value - 30.dp).toPx()

    if (state.alpha.value <= 0f) return

    if (isFireZ) {
        val sizePx = 32.dp.toPx()
        drawContext.canvas.save()
        drawContext.canvas.translate(x + sizePx / 2, y + sizePx / 2)
        drawContext.canvas.rotate(particle.rotation)
        drawContext.canvas.translate(-sizePx / 2, -sizePx / 2)
        with(fireZPainter) {
            draw(
                size = Size(sizePx, sizePx),
                alpha = state.alpha.value,
                colorFilter = ColorFilter.tint(symbolColor)
            )
        }
        drawContext.canvas.restore()
    } else {
        val textLayoutResult = textMeasurer.measure(
            text = "Z",
            style = textStyle.copy(
                fontSize = particle.size.sp,
                color = symbolColor.copy(alpha = state.alpha.value)
            )
        )
        drawContext.canvas.save()
        drawContext.canvas.translate(
            x + textLayoutResult.size.width / 2f,
            y + textLayoutResult.size.height / 2f
        )
        drawContext.canvas.rotate(particle.rotation)
        drawContext.canvas.translate(
            -textLayoutResult.size.width / 2f,
            -textLayoutResult.size.height / 2f
        )
        drawText(textLayoutResult)
        drawContext.canvas.restore()
    }
}
