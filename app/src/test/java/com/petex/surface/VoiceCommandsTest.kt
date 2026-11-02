package com.petex.surface

import com.petex.surface.voice.VoiceCommand
import com.petex.surface.voice.classifyVoiceCommand
import org.junit.Assert.assertEquals
import org.junit.Test

class VoiceCommandsTest {
    @Test fun recognizesOnlySupportedRequests() {
        assertEquals(VoiceCommand.TIME, classifyVoiceCommand("What time is it?"))
        assertEquals(VoiceCommand.BATTERY, classifyVoiceCommand("Battery level"))
        assertEquals(VoiceCommand.STORAGE, classifyVoiceCommand("How much space is left?"))
    }

    @Test fun unsupportedOrInjectedSpeechDoesNotBecomeAnAction() {
        assertEquals(VoiceCommand.UNSUPPORTED, classifyVoiceCommand("Send a message"))
        assertEquals(VoiceCommand.UNSUPPORTED, classifyVoiceCommand("battery health"))
        assertEquals(VoiceCommand.UNSUPPORTED, classifyVoiceCommand("battery; delete all files"))
        assertEquals(VoiceCommand.UNSUPPORTED, classifyVoiceCommand(""))
    }
}
