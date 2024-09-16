package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var outputTV: TextView
    private lateinit var micIV: ImageView
    private lateinit var ttsHelper: TTSHelper
    private lateinit var modelHelper: ModelHelper
    private val REQUEST_CODE_SPEECH_INPUT = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outputTV = findViewById(R.id.idTVOutput)
        micIV = findViewById(R.id.idIVMic)
        ttsHelper = TTSHelper(this)
        modelHelper = ModelHelper("AIzaSyBcW0e9kCsmZ2ovx2e84POwA2qAoXUQy7g")

        micIV.setOnClickListener {
            SpeechRecognitionHelper.startSpeechRecognition(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SpeechRecognitionHelper.REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val res = SpeechRecognitionHelper.getSpeechResults(data)
            outputTV.text = res.getOrNull(0) ?: "No speech recognized"
            lifecycleScope.launch {
                val response = modelHelper.generateContent(res[0])
                response?.let { ttsHelper.speakOut(it) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsHelper.shutdown()
    }
}