package com.petex

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Captures actual emulator/device output. Images still require human visual review. */
@RunWith(AndroidJUnit4::class)
class OrbVisualTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun cycleAndCaptureEveryState() {
        val states = listOf("Dormant", "Attending", "Listening", "Thinking", "Speaking", "Acting", "Alarmed")
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val output = File(context.filesDir, "e1").apply { check(isDirectory || mkdirs()) }
        states.forEachIndexed { index, state ->
            compose.onNodeWithContentDescription("Orb visual study: $state. Not connected to device activity.")
                .assertExists()
            compose.onNodeWithText(state).assertExists()
            val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
            File(output, "${index + 1}-${state.lowercase()}.png").outputStream().use {
                check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it))
            }
            compose.onNodeWithText("NEXT").performClick()
        }
        compose.onNodeWithText("Dormant").assertExists()
        compose.onNodeWithText("PREVIOUS").performClick()
        compose.onNodeWithText("Alarmed").assertExists()
    }
}
