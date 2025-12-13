package com.bme.ait.flaggame.ui.screen.gameScreen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.ait.flaggame.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val genModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    private val _scoreResult = MutableStateFlow<Int?>(null)
    val scoreResult = _scoreResult.asStateFlow()

    private val _isEvaluating = MutableStateFlow(false)
    val isEvaluating = _isEvaluating.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun evaluateDrawing(drawingBitmap: Bitmap, countryName: String) {
        viewModelScope.launch {
            _isEvaluating.value = true
            _scoreResult.value = null
            _error.value = null // Clear previous errors

            try {
                val prompt = content {
                    image(drawingBitmap)
                    text(
                        "This is a user's drawing of the flag of $countryName. " +
                                "Based on the accuracy of the colors, shapes, and layout, " +
                                "rate it (generously) on a scale of 0 to 100, 100 being picture perfect. " +
                                "If the drawing is completely blank, rate it a 0." +
                                "Return ONLY the integer score, with no extra text or explanation."
                    )
                }


                val response = genModel.generateContent(prompt)

                val score = response.text?.trim()?.toIntOrNull()
                if (score != null) {
                    _scoreResult.value = score
                } else {
                    _error.value = "Could not get a valid score. The response was: ${response.text}"
                }

            } catch (e: Exception) {
                android.util.Log.e("GEMINI_ERROR", "API call failed", e)
                _error.value = "API call failed. Please check your connection and try again."
            } finally {
                _isEvaluating.value = false
            }
        }
    }
    fun resetScore() {
        _scoreResult.value = null
        _error.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
