package com.petex

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.petex.surface.orb.OrbState
import com.petex.surface.orb.PetexOrb
import com.petex.surface.room.TheRoom
import com.petex.surface.diagnostics.DeviceSnapshotScreen
import com.petex.surface.voice.VoiceController
import com.petex.surface.voice.VoiceUiState
import androidx.activity.compose.BackHandler
import androidx.compose.ui.graphics.toArgb
import com.petex.surface.theme.PetexColors
import com.petex.surface.theme.PetexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = PetexColors.Void.toArgb()
        window.navigationBarColor = PetexColors.Void.toArgb()
        setContent {
            PetexTheme {
                var page by remember { mutableStateOf("room") }
                var showVoiceDisclosure by remember { mutableStateOf(false) }
                var voiceDisclosureAccepted by remember { mutableStateOf(false) }
                var voiceState by remember { mutableStateOf(VoiceUiState()) }
                val voice = remember { VoiceController(this@MainActivity) { voiceState = it } }
                val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                    if (granted && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && page == "room") voice.start()
                    else if (!granted) voice.permissionDenied()
                }
                DisposableEffect(voice) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_STOP) voice.stop()
                    }
                    lifecycle.addObserver(observer)
                    onDispose { lifecycle.removeObserver(observer); voice.close() }
                }
                BackHandler(enabled = page != "room") { voice.stop(); page = "room" }
                if (showVoiceDisclosure) {
                    AlertDialog(
                        onDismissRequest = { showVoiceDisclosure = false },
                        title = { Text("Talk to Petex") },
                        text = { Text("Your phone's speech recognition provider may send audio over the network. Petex listens only after you start. This build can answer the time, battery level, and available storage.") },
                        confirmButton = { TextButton(onClick = {
                            showVoiceDisclosure = false
                            voiceDisclosureAccepted = true
                            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) voice.start()
                            else permission.launch(Manifest.permission.RECORD_AUDIO)
                        }) { Text("Continue") } },
                        dismissButton = { TextButton(onClick = { showVoiceDisclosure = false }) { Text("Cancel") } },
                        containerColor = PetexColors.Void,
                        titleContentColor = PetexColors.TextPrimary,
                        textContentColor = PetexColors.TextPrimary
                    )
                }
                when (page) {
                    "orb" -> if (BuildConfig.DEBUG) OrbDebugScreen(onBack = { page = "room" })
                    "snapshot" -> DeviceSnapshotScreen(onBack = { page = "room" })
                    else -> TheRoom(
                        voice = voiceState,
                        onVoiceTap = {
                            if (voiceState.orb == OrbState.Listening) voice.stop()
                            else if (!voiceDisclosureAccepted) showVoiceDisclosure = true
                            else if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) voice.start()
                            else permission.launch(Manifest.permission.RECORD_AUDIO)
                        },
                        showOrbStudy = if (BuildConfig.DEBUG) ({ voice.stop(); page = "orb" }) else null,
                        showDeviceSnapshot = { voice.stop(); page = "snapshot" }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrbDebugScreen(onBack: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
    val states = OrbState.all
    Column(
        modifier = Modifier.fillMaxSize().background(PetexColors.Void).drawBehind {
            if (states[index] == OrbState.Alarmed) {
                drawRect(Brush.radialGradient(
                    colors = listOf(Color.Transparent, PetexColors.Ember.copy(alpha = 0.12f)),
                    center = Offset(size.width / 2, size.height / 2), radius = size.maxDimension * 0.7f
                ))
            }
        }.verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DebugControl("Back to room", onBack)
        Text("PETEX / E1 VISUAL STUDY", style = MaterialTheme.typography.labelMedium, color = PetexColors.TextDim)
        PetexOrb(states[index])
        Text(states[index].label, style = MaterialTheme.typography.headlineMedium, color = PetexColors.TextPrimary)
        Text("Static preview only. No microphone, speech or actions are active.",
            modifier = Modifier.padding(top = 12.dp), style = MaterialTheme.typography.bodyMedium,
            color = PetexColors.TextDim)
        Row(modifier = Modifier.padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            DebugControl("Previous") { index = (index - 1 + states.size) % states.size }
            DebugControl("Next") { index = (index + 1) % states.size }
        }
    }
}

@Composable
private fun DebugControl(label: String, onClick: () -> Unit) {
    Box(Modifier.sizeIn(minWidth = 80.dp, minHeight = 48.dp).clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = PetexColors.TextPrimary)
    }
}
