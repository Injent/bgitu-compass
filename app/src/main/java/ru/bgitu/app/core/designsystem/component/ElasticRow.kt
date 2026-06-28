package ru.bgitu.app.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density

@Composable
fun ElasticRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable ElasticRowScope.() -> Unit
) {
    Layout(
        content = { ElasticRowScopeInstance.content() },
        modifier = modifier
    ) { measurables, constraints ->
        val totalWidth = constraints.maxWidth
        val count = measurables.size

        val weights = measurables.map { measurable ->
            (measurable.parentData as? ElasticWeightData)?.weight?.invoke() ?: 1f
        }

        val spacingPx = horizontalArrangement.spacing.roundToPx()
        val totalSpacing = spacingPx * (count - 1)
        val availableWidth = (totalWidth - totalSpacing).coerceAtLeast(0)

        val baseItemWidth = if (count > 0) availableWidth / count.toFloat() else 0f

        val finalWidths = FloatArray(count) { baseItemWidth }

        weights.forEachIndexed { index, weight ->
            if (weight > 1f) {
                val expansionRatio = weight - 1f
                val expansionPx = baseItemWidth * expansionRatio

                finalWidths[index] += expansionPx

                val leftIndex = index - 1
                val rightIndex = index + 1
                val hasLeft = leftIndex >= 0
                val hasRight = rightIndex < count

                when {
                    hasLeft && hasRight -> {
                        finalWidths[leftIndex] -= expansionPx / 2
                        finalWidths[rightIndex] -= expansionPx / 2
                    }
                    hasLeft -> {
                        finalWidths[leftIndex] -= expansionPx
                    }
                    hasRight -> {
                        finalWidths[rightIndex] -= expansionPx
                    }
                }
            }
        }

        val placeables = measurables.mapIndexed { index, measurable ->
            val childWidth = finalWidths[index].toInt().coerceAtLeast(0)

            measurable.measure(
                Constraints(
                    minWidth = childWidth,
                    maxWidth = childWidth,
                    minHeight = 0,
                    maxHeight = constraints.maxHeight
                )
            )
        }

        val height = placeables.maxOfOrNull { it.height } ?: 0

        layout(totalWidth, height) {
            var xPosition = 0
            placeables.forEach { placeable ->
                val y = verticalAlignment.align(placeable.height, height)
                placeable.placeRelative(x = xPosition, y = y)
                xPosition += placeable.width + spacingPx
            }
        }
    }
}

interface ElasticRowScope {
    fun Modifier.elasticWeight(weight: () -> Float): Modifier
}

private object ElasticRowScopeInstance : ElasticRowScope {
    override fun Modifier.elasticWeight(weight: () -> Float) = this.then(
        object : ParentDataModifier {
            override fun Density.modifyParentData(parentData: Any?) = ElasticWeightData(weight)
        }
    )
}

private data class ElasticWeightData(val weight: () -> Float)