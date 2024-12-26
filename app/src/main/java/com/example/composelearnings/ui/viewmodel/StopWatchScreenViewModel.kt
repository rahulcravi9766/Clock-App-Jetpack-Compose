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
    private var elapsedMillis = 0L
    private var seconds: Long = 0
    private var milliseconds: Long = 0
    private var totalSeconds: Long = 0
    private var minutes: Long = 0
    private var dbData: StopWatchModel? = null

    init {
        getStopWatchData()
    }

    private fun getStopWatchData() {
        viewModelScope.launch {
            tikTikRepository.getStopWatchData().collect { data ->

                if (data != null){
                   // dbData = data
                    isPaused = true
                    _uiState.update { state ->
                        state.copy(
                            isPlaying = data.isPlaying,
                            milliSecond = data.milliSecond.toLong().addZero(),
                            second = if (data.second == "0:00") data.second else data.second.toLong()
                                .addZero(),
                            minute = data.minute
                        )
                    }
                    elapsedMillis = data.elapsedMillis

                    Log.d("StopWatchVM", "getStopWatchData: $data")
                }else{
                    Log.d("StopWatchVMm", "getStopWatchData: $data")
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
//                data?.let {
//                    dbData = it
//                    isPaused = true
//                    _uiState.update { state ->
//                        state.copy(
//                            isPlaying = it.isPlaying,
//                            milliSecond = it.milliSecond.toLong().addZero(),
//                            second = if (it.second == "0:00") it.second else it.second.toLong()
//                                .addZero(),
//                            minute = it.minute
//                        )
//                    }
//                    elapsedMillis = data.elapsedMillis
//
//                    Log.d("StopWatchVM", "getStopWatchData: $data")
//                } ?: {
//                    _uiState.update { state ->
//                        state.copy(
//                            milliSecond = Common.MILLISECOND,
//                            second = Common.SECONDS,
//                            minute = "",
//                            elapsedMillis = 0L,
//                            isPlaying = false
//                        )
//                    }
//                }
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
                elapsedMillis += 10L
                totalSeconds = elapsedMillis / 1000
                minutes = totalSeconds / 60
                seconds = totalSeconds % 60
                milliseconds = elapsedMillis % 1000 / 10
                Log.d("stopwatch", "$seconds.$milliseconds")
                _uiState.update { currentState ->
                    currentState.copy(
                        milliSecond = milliseconds.addZero(),
                        second = seconds.addZero(),
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
        stopWatchJob?.cancel()
        viewModelScope.launch {
            async {
                tikTikRepository.deleteAllData()
            }.await()
           getStopWatchData()
        }
    }

    private fun validateStopWatchData() {
        viewModelScope.launch {
            val data = StopWatchModel(
                id = 1,
                milliSecond = milliseconds.plus(1).toString(),
                second = seconds.toString(),
                minute = minutes.toString(),
                elapsedMillis = elapsedMillis,
            )
            tikTikRepository.insertOrUpdateStopWatchData(data)
        }

        Log.d(
            "CurrentStoppedTime",
            "minutes: $minutes, seconds: $seconds, milliseconds: ${_uiState.value.milliSecond} , ${
                milliseconds.plus(1)
            }"
        )
    }
}