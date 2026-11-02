package com.petex

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VoiceConsentTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun cancellingDisclosureDoesNotRequestOrStartMicrophone() {
        val activity = compose.activity
        assertEquals(PackageManager.PERMISSION_DENIED,
            ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO))
        compose.onNodeWithText("Talk to Petex").performClick()
        compose.onNodeWithText("Your phone's speech recognition provider may send audio over the network. Petex listens only after you start. This build can answer the time, battery level, and available storage.").assertExists()
        compose.onNodeWithText("Cancel").performClick()
        compose.onNodeWithContentDescription("Orb: Dormant").assertExists()
        assertEquals(PackageManager.PERMISSION_DENIED,
            ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO))
    }
}
