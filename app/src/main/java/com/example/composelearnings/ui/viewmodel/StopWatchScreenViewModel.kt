package com.example.composelearnings.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.composelearnings.ui.state.StopWatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StopWatchScreenViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(StopWatchUiState())
    val uiState = _uiState.asStateFlow()


    fun onPlayPauseButtonClick(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isPlaying = value)
        }
    }
}