package com.petex.surface.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.format.Formatter
import com.petex.surface.diagnostics.captureDeviceSnapshot
import com.petex.surface.orb.OrbState
import java.text.DateFormat
import java.util.Date

/** Explicit push-to-talk only. No background or wake-word recording. */
data class VoiceUiState(
    val orb: OrbState = OrbState.Dormant,
    val heard: String? = null,
    val reply: String? = null,
    val notice: String? = null,
)

class VoiceController(private val context: Context, private val onChange: (VoiceUiState) -> Unit) {
    private val main = Handler(Looper.getMainLooper())
    private var state = VoiceUiState()
    private var recognizer: SpeechRecognizer? = null
    private var speechId = 0
    private var recognitionId = 0
    private var destroyed = false
    private var ttsReady = false
    private var tts: TextToSpeech? = null

    init {
        tts = try {
            TextToSpeech(context.applicationContext) { status ->
                if (!destroyed && status == TextToSpeech.SUCCESS) {
                    ttsReady = true
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(id: String?) = postSpeechState(id, true)
                        override fun onDone(id: String?) = postSpeechState(id, false)
                        @Deprecated("Deprecated in Android")
                        override fun onError(id: String?) = postSpeechState(id, false)
                    })
                }
            }
        } catch (_: Exception) { null }
    }

    private fun postSpeechState(id: String?, speaking: Boolean) {
        main.post {
            if (!destroyed && id == speechId.toString() && recognizer == null) {
                publish(state.copy(orb = if (speaking) OrbState.Speaking else OrbState.Dormant))
            }
        }
    }

    fun permissionDenied() = publish(state.copy(notice = "Microphone permission is required to talk."))

    /** Caller must have obtained RECORD_AUDIO permission. */
    fun start() {
        if (destroyed) return
        if (recognizer != null) { stop(); return }
        stopSpeech()
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            publish(state.copy(orb = OrbState.Dormant, notice = "No speech recognition service is available on this phone."))
            return
        }
        val id = ++recognitionId
        try {
            val instance = SpeechRecognizer.createSpeechRecognizer(context)
            recognizer = instance
            instance.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onError(error: Int) {
                    if (!isCurrent(id)) return
                    releaseRecognizer()
                    publish(state.copy(orb = OrbState.Dormant, notice = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "I didn't catch that. Try again."
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission is required to talk."
                        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "The phone's speech service couldn't connect."
                        else -> "Speech recognition wasn't available. Try again."
                    }))
                }
                override fun onResults(results: Bundle?) {
                    if (!isCurrent(id)) return
                    val heard = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                    releaseRecognizer()
                    if (heard.isNullOrBlank()) {
                        publish(state.copy(orb = OrbState.Dormant, notice = "I didn't catch that. Try again."))
                        return
                    }
                    val reply = replyTo(heard)
                    publish(VoiceUiState(heard = heard, reply = reply))
                    speak(reply)
                }
            })
            // Publish the visible microphone state before the service is asked to start.
            publish(VoiceUiState(orb = OrbState.Listening))
            instance.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            })
        } catch (_: Exception) {
            releaseRecognizer()
            publish(state.copy(orb = OrbState.Dormant, notice = "Couldn't start speech recognition."))
        }
    }

    private fun isCurrent(id: Int) = !destroyed && recognitionId == id && recognizer != null

    private fun replyTo(text: String): String = when (classifyVoiceCommand(text)) {
        VoiceCommand.TIME -> "It's ${DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())}."
        VoiceCommand.BATTERY -> captureDeviceSnapshot(context).batteryPercent?.let { "Battery is at $it percent." }
            ?: "Battery level is unavailable."
        VoiceCommand.STORAGE -> captureDeviceSnapshot(context).availableBytes?.let {
            "${Formatter.formatFileSize(context, it)} of storage is available to apps."
        } ?: "Available storage is unavailable."
        VoiceCommand.UNSUPPORTED -> "I can only answer the time, battery level, and available storage right now."
    }

    private fun speak(text: String) {
        val engine = tts
        if (!ttsReady || engine == null) {
            publish(state.copy(notice = "Spoken output isn't available; the answer is shown above."))
            return
        }
        val id = (++speechId).toString()
        if (engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, id) != TextToSpeech.SUCCESS) {
            publish(state.copy(notice = "Spoken output isn't available; the answer is shown above."))
        }
    }

    private fun stopSpeech() { speechId++; tts?.stop() }
    private fun releaseRecognizer() { recognizer?.destroy(); recognizer = null }
    private fun publish(value: VoiceUiState) { if (!destroyed) { state = value; onChange(value) } }

    fun stop() {
        recognitionId++
        recognizer?.cancel()
        releaseRecognizer()
        stopSpeech()
        publish(state.copy(orb = OrbState.Dormant))
    }

    fun close() { stop(); destroyed = true; tts?.shutdown(); tts = null }
}
