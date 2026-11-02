package com.petex.surface.room

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petex.surface.intent.IntentBar
import com.petex.surface.orb.OrbState
import com.petex.surface.orb.PetexOrb
import com.petex.surface.presence.PresenceStrip
import com.petex.surface.theme.PetexColors

/** The early room is intentionally honest: no listening, no simulated conversation. */
@Composable
fun TheRoom(showOrbStudy: (() -> Unit)? = null) {
    Column(
        Modifier.fillMaxSize().background(PetexColors.Void).padding(horizontal = 24.dp),
    ) {
        PresenceStrip(Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 8.dp))
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("A MORE THOUGHTFUL SPACE", style = MaterialTheme.typography.labelMedium,
                    color = PetexColors.TextDim, textAlign = TextAlign.Center)
                Spacer(Modifier.height(4.dp))
                PetexOrb(OrbState.Dormant)
                Text("A little more room\nto think.", style = MaterialTheme.typography.headlineMedium,
                    color = PetexColors.TextPrimary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(14.dp))
                Text("An early look at Petex. Voice and actions are not connected yet.",
                    modifier = Modifier.sizeIn(maxWidth = 290.dp), style = MaterialTheme.typography.bodyMedium,
                    color = PetexColors.TextDim, textAlign = TextAlign.Start)
            }
        }
        IntentBar(Modifier.fillMaxWidth())
        Row(Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("DESIGNED TO BE QUIET", style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
            if (showOrbStudy != null) {
                Box(Modifier.sizeIn(minHeight = 48.dp).clickable(role = Role.Button, onClick = showOrbStudy),
                    contentAlignment = Alignment.Center) {
                    Text("ORB STATES  ↗", style = MaterialTheme.typography.labelMedium, color = PetexColors.TextPrimary)
                }
            }
        }
    }
}
