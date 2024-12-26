package com.example.composelearnings.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composelearnings.data.StopWatchModel
import com.example.composelearnings.data.TikTikRepository
import com.example.composelearnings.utils.Common
import com.example.composelearnings.utils.addZero
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StopWatchScreenViewModel(private val tikTikRepository: TikTikRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(StopWatchModel())
    val uiState = _uiState.asStateFlow()
    private var stopWatchJob: Job? = null
    private var isRunning = false
    var isPaused = false

    init {
        getStopWatchData()
    }

    private fun getStopWatchData() {
        viewModelScope.launch {
            tikTikRepository.getStopWatchData().collect { data ->

                if (data != null) {
                    isPaused = true
                    _uiState.update { state ->
                        state.copy(
                            isPlaying = data.isPlaying,
                            milliSecond = data.milliSecond.toLong().addZero(),
                            second = if (data.second == "0:00") data.second else data.second.toLong()
                                .addZero(),
                            minute = data.minute,
                            elapsedMillis = data.elapsedMillis
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            milliSecond = Common.MILLISECOND,
                            second = Common.SECONDS,
                            minute = "",
                            elapsedMillis = 0L,
                            isPlaying = false
                        )
                    }
                }
            }
        }
    }

    fun onPlayPauseButtonClick(value: Boolean) {
        if (value) {
            isRunning = true
            isPaused = false
        } else {
            isRunning = false
            isPaused = true
            validateStopWatchData()
        }
        stopWatchJob = viewModelScope.launch {
            while (value && !isPaused) {
                delay(10L)
                _uiState.value.elapsedMillis += 10L
               val totalSeconds = _uiState.value.elapsedMillis / 1000
                _uiState.update { currentState ->
                    currentState.copy(
                        milliSecond = (_uiState.value.elapsedMillis % 1000 / 10).addZero(),
                        second = (totalSeconds % 60).addZero(),
                        minute = (totalSeconds / 60).toString()
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
        stopWatchJob?.cancel()
        viewModelScope.launch {
            val deleteData = async { tikTikRepository.deleteAllData() }
            getStopWatchData()
            deleteData.await()
        }
    }

    private fun validateStopWatchData() {
        viewModelScope.launch {
            val data = StopWatchModel(
                id = 1,
                milliSecond = _uiState.value.milliSecond,
                second = _uiState.value.second,
                minute = _uiState.value.minute,
                elapsedMillis = _uiState.value.elapsedMillis,
            )
            tikTikRepository.insertOrUpdateStopWatchData(data)
        }
    }
}