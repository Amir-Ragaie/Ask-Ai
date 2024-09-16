package com.example.project

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModelHelper(private val apiKey: String) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )

    suspend fun generateContent(text: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(text)
                response.text
            } catch (e: Exception) {
                null
            }
        }
    }
}