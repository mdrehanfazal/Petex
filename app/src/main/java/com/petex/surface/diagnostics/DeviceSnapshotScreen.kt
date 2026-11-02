package com.petex.surface.diagnostics

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.petex.surface.theme.PetexColors
import java.text.DateFormat
import java.util.Date

@Composable
fun DeviceSnapshotScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var snapshot by remember { mutableStateOf(captureDeviceSnapshot(context)) }
    Column(Modifier.fillMaxSize().background(PetexColors.Void).verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            SnapshotControl("← ROOM", onBack)
            Text("ON THIS DEVICE", style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
        }
        Spacer(Modifier.height(16.dp))
        Text("Device snapshot", style = MaterialTheme.typography.headlineMedium, color = PetexColors.TextPrimary)
        Text("Current readings from your phone. No background monitoring.",
            style = MaterialTheme.typography.bodyMedium, color = PetexColors.TextDim)
        Text("TAKEN ${DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(snapshot.capturedAt))}",
            style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
        SnapshotCard("BATTERY") {
            Reading("Level", snapshot.batteryPercent?.let { "$it%" } ?: "Unavailable")
            Reading("Status", snapshot.batteryStatus ?: "Unavailable")
            Reading("Battery temperature", snapshot.batteryTemperatureCelsius?.let {
                String.format(java.util.Locale.getDefault(), "%.1f °C", it)
            } ?: "Unavailable")
        }
        SnapshotCard("STORAGE") {
            Reading("Available to apps", snapshot.availableBytes?.let { Formatter.formatFileSize(context, it) } ?: "Unavailable")
            Reading("Total data storage", snapshot.totalBytes?.let { Formatter.formatFileSize(context, it) } ?: "Unavailable")
        }
        Text("A snapshot is not a battery health test or storage forecast.",
            style = MaterialTheme.typography.bodyMedium, color = PetexColors.TextDim)
        SnapshotControl("REFRESH READINGS") { snapshot = captureDeviceSnapshot(context) }
    }
}

@Composable
private fun SnapshotCard(label: String, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth().background(PetexColors.Glass, RoundedCornerShape(18.dp))
        .border(1.dp, PetexColors.Hairline, RoundedCornerShape(18.dp)).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
        content()
    }
}

@Composable
private fun Reading(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = PetexColors.TextDim)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = PetexColors.TextPrimary)
    }
}

@Composable
private fun SnapshotControl(label: String, onClick: () -> Unit) {
    Box(Modifier.sizeIn(minHeight = 48.dp).clickable(role = Role.Button, onClick = onClick)
        .padding(horizontal = 4.dp), contentAlignment = Alignment.Center) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = PetexColors.Cyan)
    }
}
