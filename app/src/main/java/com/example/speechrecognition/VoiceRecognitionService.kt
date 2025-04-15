package com.example.speechrecognition


import android.app.*
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import org.vosk.*
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.StorageService

class VoiceWakeupService : Service() {

    private lateinit var model: Model
    private lateinit var speechService: SpeechService

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "WakeupChannel",
                "Voice Wakeup Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        Log.d("VoiceService", "Service onCreate()")

        val notification = NotificationCompat.Builder(this, "WakeupChannel")
            .setContentTitle("Voice Wakeup Active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(101, notification)
        Log.d("VoiceService", "Foreground notification started")

        Thread {
            try {
                StorageService.unpack(this, "model", "model",
                    { model ->
                        Log.d("VoiceService", "Model unpacked and ready!")
                        this.model = model
                        startListening()
                    },
                    { exception ->
                        Log.e("VoiceService", "Error loading model: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                Log.e("VoiceService", "Model loading failed: ${e.message}")
            }
        }.start()
    }

    private fun startListening() {
        val recognizer = Recognizer(model, 16000.0f)
        speechService = SpeechService(recognizer, 16000.0f)

        speechService.startListening(object : RecognitionListener {
            override fun onPartialResult(hypothesis: String?) {
                hypothesis?.let {
                    Log.d("VoskPartial", it)
                    if (it.contains("Listen", true)) {  // Wake word
                        youraction()
                    }
                }
            }

            override fun onResult(hypothesis: String?) {}
            override fun onFinalResult(hypothesis: String?) {}
            override fun onError(e: Exception?) {
                Log.e("VoskError", e.toString())
            }

            override fun onTimeout() {}
        })
    }

    private fun youraction() {
        Toast.makeText(this, "Voice Word Detected", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        speechService.stop()
        speechService.shutdown()
        model.close()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
