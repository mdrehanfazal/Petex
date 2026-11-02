package com.petex.surface.intent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
import com.petex.surface.orb.OrbState
import com.petex.surface.voice.VoiceUiState
import com.petex.surface.theme.PetexColors

/** Explicit tap-to-talk control; the phone speech service may use a network. */
@Composable
fun IntentBar(voice: VoiceUiState, onTap: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("YOUR INPUT", style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
            Text(if (voice.orb == OrbState.Listening) "MICROPHONE LIVE" else "PUSH TO TALK", style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
        }
        Row(
            Modifier.fillMaxWidth().height(56.dp)
                .background(PetexColors.Glass, RoundedCornerShape(999.dp))
                .border(1.dp, PetexColors.Hairline, RoundedCornerShape(999.dp))
                .clickable(role = Role.Button, onClick = onTap)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(Modifier.size(36.dp).background(PetexColors.TextDim.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center) {
                val iconColor = if (voice.orb == OrbState.Listening) PetexColors.Cyan else PetexColors.TextDim
                Canvas(Modifier.size(20.dp)) {
                    drawRoundRect(iconColor, topLeft = Offset(size.width * 0.35f, size.height * 0.08f),
                        size = Size(size.width * 0.3f, size.height * 0.5f),
                        cornerRadius = CornerRadius(size.width * 0.15f), style = Stroke(1.3.dp.toPx()))
                    drawArc(iconColor, 0f, 180f, false, topLeft = Offset(size.width * 0.2f, size.height * 0.24f),
                        size = Size(size.width * 0.6f, size.height * 0.55f), style = Stroke(1.3.dp.toPx()))
                    drawLine(iconColor, Offset(size.width * 0.5f, size.height * 0.78f),
                        Offset(size.width * 0.5f, size.height * 0.95f), 1.3.dp.toPx())
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(if (voice.orb == OrbState.Listening) "Listening · tap to stop" else "Talk to Petex",
                    style = MaterialTheme.typography.bodyMedium, color = PetexColors.TextPrimary)
                Text("Time, battery, storage only", style = MaterialTheme.typography.labelMedium,
                    color = PetexColors.TextDim)
            }
        }
    }
}
