package com.example.composelearnings.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composelearnings.data.TikTikRepository
import com.example.composelearnings.ui.state.StopWatchUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StopWatchScreenViewModel(private val tikTikRepository: TikTikRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(StopWatchUiState())
    val uiState = _uiState.asStateFlow()
    private lateinit var stopWatchJob: Job
    var isRunning = false
    var isPaused = false
    private var elapsedMillis = 0L
    private var seconds: Long = 0
    private var milliseconds: Long = 0
    private var totalSeconds: Long = 0
    private var minutes: Long = 0

    fun onPlayPauseButtonClick(value: Boolean) {

        if (value) {
            isRunning = true
            isPaused = false
        } else {
            isRunning = false
            isPaused = true
        }
        stopWatchJob = viewModelScope.launch {
            while (value && !isPaused) {
                delay(10L)
                elapsedMillis += 10L
                totalSeconds = elapsedMillis / 1000
                minutes = totalSeconds / 60
                seconds = totalSeconds % 60
                milliseconds = elapsedMillis % 1000 / 10
                Log.d("stopwatch", "$seconds.$milliseconds")
                _uiState.update { currentState ->
                    currentState.copy(
                        milliSecond = (if (milliseconds <= 9) {
                            "0$milliseconds"
                        } else {
                            milliseconds
                        }).toString(),
                        second = (if (seconds <= 9) {
                            "0$seconds"
                        } else {
                            seconds
                        }).toString(),
                        minute = minutes.toString()
                    )
                }
            }
        }
        _uiState.update { currentState ->
            currentState.copy(isPlaying = value)
        }
    }

    fun onRefreshButtonClick() {
        isRunning = false
        isPaused = false
        elapsedMillis = 0L
        stopWatchJob.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                milliSecond = "00",
                second = "0:00",
                minute = "",
                isPlaying = false
            )
        }
    }

    private fun validateStopWatchData(){

    }

}