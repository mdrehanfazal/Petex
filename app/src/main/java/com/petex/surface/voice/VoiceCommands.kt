package com.petex.surface.voice

import java.util.Locale

/** Deliberately small, local allowlist. Recognized speech is data, never code. */
enum class VoiceCommand { TIME, BATTERY, STORAGE, UNSUPPORTED }

fun classifyVoiceCommand(text: String): VoiceCommand {
    val words = text.lowercase(Locale.ROOT).replace(Regex("[^\\p{L}\\p{N} ]"), " ")
        .trim().replace(Regex("\\s+"), " ")
    return when (words) {
        "time", "what time is it", "what is the time", "tell me the time", "current time" -> VoiceCommand.TIME
        "battery", "battery level", "what is my battery level", "what is the battery level",
        "how much battery is left", "how much battery do i have" -> VoiceCommand.BATTERY
        "storage", "available storage", "storage available", "how much storage is available",
        "how much storage do i have", "how much space is left" -> VoiceCommand.STORAGE
        else -> VoiceCommand.UNSUPPORTED
    }
}
