package com.example.speechrecognition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.speechrecognition.ui.theme.SpeechRecognitionTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.FOREGROUND_SERVICE_MICROPHONE,
        Manifest.permission.POST_NOTIFICATIONS
    )

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()

        enableEdgeToEdge()
        setContent {
            SpeechRecognitionTheme {
                Scaffold { innerPadding ->
                    Home(
                        modifier =Modifier.padding(innerPadding),
                        startVoiceService = {startVoiceService()},
                        stopVoiceService = {stopVoiceService()}
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun checkPermissions() {
        val notGranted = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), 101)
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun hasPermissions(): Boolean {
        val granted= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED
        return granted
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun startVoiceService() {
        Log.d("VoiceService", "startVoiceService() called")


            Log.d("VoiceService", "Permissions granted, starting service...")
            val intent = Intent(this, VoiceWakeupService::class.java)
            ContextCompat.startForegroundService(this, intent)
            Toast.makeText(this, "Listening started...", Toast.LENGTH_SHORT).show()
    }

    fun stopVoiceService() {
        val intent = Intent(this, VoiceWakeupService::class.java)
        stopService(intent)
    }
}
