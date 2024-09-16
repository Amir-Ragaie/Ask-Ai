package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var outputTV: TextView
    private lateinit var micIV: ImageView
    private lateinit var stopBtn: Button
    private lateinit var ttsHelper: TTSHelper
    private lateinit var modelHelper: ModelHelper

    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outputTV = findViewById(R.id.idTVOutput)
        micIV = findViewById(R.id.idIVMic)
        stopBtn = findViewById(R.id.idBtnStop)
        ttsHelper = TTSHelper(this)
        modelHelper = ModelHelper("AIzaSyBcW0e9kCsmZ2ovx2e84POwA2qAoXUQy7g")

        micIV.setOnClickListener {
            SpeechRecognitionHelper.startSpeechRecognition(this)
        }

        stopBtn.setOnClickListener {
            ttsHelper.stopSpeaking()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SpeechRecognitionHelper.REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val res = SpeechRecognitionHelper.getSpeechResults(data)
            val recognizedText = res.getOrNull(0) ?: "No speech recognized"
            outputTV.text = recognizedText
            lifecycleScope.launch {
                try {
                    val response = modelHelper.generateContent(recognizedText)
                    response?.let { ttsHelper.speakOut(it) }
                } catch (e: Exception) {
                    outputTV.text = "Error generating response"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsHelper.shutdown()
    }
}
