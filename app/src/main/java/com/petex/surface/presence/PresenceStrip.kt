package com.petex.surface.presence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petex.surface.theme.PetexColors

@Composable
fun PresenceStrip(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(7.dp).background(PetexColors.Cyan.copy(alpha = 0.7f), CircleShape))
            Text("PETEX", style = MaterialTheme.typography.labelMedium, color = PetexColors.TextPrimary)
        }
        Text("EARLY PREVIEW", modifier = Modifier
            .border(1.dp, PetexColors.Hairline, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
    }
}
