package ru.bgitu.app.feature.schedule.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bgitu.app.R
import ru.bgitu.app.core.designsystem.icon.Icons
import ru.bgitu.app.core.designsystem.icon.SleepingCat
import ru.bgitu.app.core.designsystem.theme.AppTheme
import kotlin.random.Random

@Composable
fun SleepyCatAnimation(
    isFireZ: Boolean = false,
    modifier: Modifier = Modifier
) {
    val zParticleStates = remember { mutableStateListOf<ZParticleState>() }
    var particleDirection by remember { mutableFloatStateOf(1f) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val catPainter = rememberVectorPainter(Icons.SleepingCat)
    val fireZPainter = painterResource(R.drawable.fire_z)
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)

    val catColor = AppTheme.colorScheme.cat
    val backgroundColor = AppTheme.colorScheme.background2

    LaunchedEffect(lifecycleOwner) {
        var idCounter = 0L

        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            zParticleStates.clear()
            while (true) {
                val newZ = ZParticle(
                    id = idCounter++,
                    startXOffset = Random.nextInt(0, 30).dp,
                    size = Random.nextInt(20, 35),
                    rotation = Random.nextInt(15, 26).toFloat() * particleDirection
                )
                particleDirection *= -1f
                
                val state = ZParticleState(newZ) {
                    zParticleStates.removeAll { it.particle.id == newZ.id }
                }
                zParticleStates.add(state)
                launch { state.animate() }
                
                delay(1000)
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scaleY by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingScale"
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.size(width = 200.dp, height = 250.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.99f)
        ) {
            val intrinsicSize = catPainter.intrinsicSize
            val catWidth = 170.dp.toPx()
            val catHeight = if (intrinsicSize != Size.Unspecified && intrinsicSize.width > 0) {
                catWidth * (intrinsicSize.height / intrinsicSize.width)
            } else {
                catWidth
            }
            val catSize = Size(catWidth, catHeight)

            val catOffset = Offset(
                (size.width - catSize.width) / 2,
                (size.height - catSize.height)
            )
            
            val zBaseOffset = catOffset + Offset(catSize.width / 2 + 24.dp.toPx(), catSize.height / 2)

            // --- ШАГ 1: Рисуем саму кошку ---
            withTransform({
                translate(catOffset.x, catOffset.y)
                scale(1f, scaleY, pivot = Offset(catSize.width / 2, catSize.height))
            }) {
                with(catPainter) {
                    draw(size = catSize, colorFilter = ColorFilter.tint(catColor))
                }
            }

            // --- ШАГ 2: Часть Z ВНУТРИ кошки (цвет фона) ---
            drawContext.canvas.withSaveLayer(Rect(Offset.Zero, size), Paint()) {
                zParticleStates.forEach { state ->
                    drawZSymbol(
                        isFireZ = isFireZ,
                        state = state,
                        symbolColor = backgroundColor,
                        fireZPainter = fireZPainter,
                        textMeasurer = textMeasurer,
                        textStyle = textStyle,
                        baseOffset = zBaseOffset
                    )
                }

                drawContext.canvas.withSaveLayer(Rect(Offset.Zero, size), Paint().apply { blendMode = BlendMode.DstIn }) {
                    withTransform({
                        translate(catOffset.x, catOffset.y)
                        scale(1f, scaleY, pivot = Offset(catSize.width / 2, catSize.height))
                    }) {
                        with(catPainter) {
                            draw(size = catSize, colorFilter = ColorFilter.tint(backgroundColor))
                        }
                    }
                }
            }

            // --- ШАГ 3: Часть Z СНАРУЖИ кошки (цвет кошки) ---
            drawContext.canvas.withSaveLayer(Rect(Offset.Zero, size), Paint()) {
                zParticleStates.forEach { state ->
                    drawZSymbol(
                        isFireZ = isFireZ,
                        state = state,
                        symbolColor = catColor,
                        fireZPainter = fireZPainter,
                        textMeasurer = textMeasurer,
                        textStyle = textStyle,
                        baseOffset = zBaseOffset
                    )
                }

                drawContext.canvas.withSaveLayer(Rect(Offset.Zero, size), Paint().apply { blendMode = BlendMode.DstOut }) {
                    withTransform({
                        translate(catOffset.x, catOffset.y)
                        scale(1f, scaleY, pivot = Offset(catSize.width / 2, catSize.height))
                    }) {
                        with(catPainter) {
                            draw(size = catSize, colorFilter = ColorFilter.tint(catColor))
                        }
                    }
                }
            }
        }
    }
}
