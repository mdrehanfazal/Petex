package com.petex.surface.orb

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.petex.surface.theme.PetexColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Static E1 studies. Shapes do not imply microphone, speech, progress, or verification. */
@Composable
fun PetexOrb(state: OrbState, modifier: Modifier = Modifier, visualStudy: Boolean = true) {
    val accent = when (state) {
        OrbState.Thinking -> PetexColors.Violet
        OrbState.Acting -> PetexColors.Amber
        OrbState.Alarmed -> PetexColors.Ember
        else -> PetexColors.Cyan
    }
    Canvas(modifier = modifier.size(320.dp).semantics {
        contentDescription = if (visualStudy) "Orb visual study: ${state.label}. Not connected to device activity."
        else "Orb: ${state.label}"
    }) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = 96.dp.toPx().coerceAtMost(size.minDimension * 0.32f)
        val alpha = if (state == OrbState.Dormant) 0.30f else 1f
        val pixel = 1.dp.toPx()

        // Glow and body are radial gradients, not blur or elevation shadows.
        drawCircle(brush = Brush.radialGradient(
            colors = listOf(accent.copy(alpha = 0.20f * alpha), Color.Transparent),
            center = center, radius = radius * 1.6f
        ), radius = radius * 1.6f, center = center)
        drawCircle(brush = Brush.radialGradient(
            colors = listOf(accent.copy(alpha = 0.30f * alpha), accent.copy(alpha = 0.09f * alpha), Color.Transparent),
            center = center + Offset(-radius * 0.25f, -radius * 0.3f), radius = radius * 1.5f
        ), radius = radius, center = center)
        drawCircle(brush = Brush.radialGradient(
            colors = listOf(PetexColors.White.copy(alpha = (if (state == OrbState.Speaking) 0.62f else 0.24f) * alpha),
                accent.copy(alpha = 0.12f * alpha), Color.Transparent),
            center = center + Offset(-radius * 0.32f, -radius * 0.38f), radius = radius
        ), radius = radius, center = center)
        drawArc(accent.copy(alpha = 0.72f * alpha), 195f, 150f, false,
            topLeft = center - Offset(radius, radius), size = Size(radius * 2, radius * 2), style = Stroke(pixel))

        when (state) {
            OrbState.Attending -> {
                val ring = radius * 1.45f
                drawArc(accent.copy(alpha = 0.65f), 0f, 360f, false,
                    topLeft = center - Offset(ring, ring * 0.32f), size = Size(ring * 2, ring * 0.64f), style = Stroke(pixel))
            }
            OrbState.Listening -> repeat(24) { i ->
                val angle = 2.0 * PI * i / 24
                val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
                drawLine(accent.copy(alpha = 0.7f), center + direction * (radius * 1.12f),
                    center + direction * (radius * 1.21f), strokeWidth = pixel)
            }
            OrbState.Thinking -> repeat(48) { i ->
                val angle = 2.0 * PI * i / 48
                val position = center + Offset(cos(angle).toFloat() * radius * 1.25f,
                    sin(angle).toFloat() * radius * 0.38f)
                drawCircle(accent.copy(alpha = 0.25f + 0.5f * (i % 3) / 2), radius = 1.5.dp.toPx(), center = position)
            }
            OrbState.Speaking -> {
                // A neutral, static motif; no TTS envelope exists in E1.
                val heights = listOf(0.12f, 0.22f, 0.36f, 0.18f, 0.42f, 0.24f, 0.12f)
                heights.forEachIndexed { i, height ->
                    val x = center.x + (i - 3) * radius * 0.16f
                    drawLine(PetexColors.White.copy(alpha = 0.65f), Offset(x, center.y - radius * height / 2),
                        Offset(x, center.y + radius * height / 2), strokeWidth = 2.dp.toPx())
                }
            }
            OrbState.Acting -> {
                // An empty track and unfilled marks, never a claim of completed work.
                val ring = radius * 1.22f
                drawArc(accent.copy(alpha = 0.35f), 210f, 120f, false,
                    topLeft = center - Offset(ring, ring), size = Size(ring * 2, ring * 2), style = Stroke(pixel))
                repeat(5) { i ->
                    val angle = (210.0 + i * 30.0) * PI / 180.0
                    val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
                    drawLine(accent.copy(alpha = 0.4f), center + direction * (ring - 3.dp.toPx()),
                        center + direction * (ring + 3.dp.toPx()), strokeWidth = pixel)
                }
            }
            else -> Unit
        }
    }
}
