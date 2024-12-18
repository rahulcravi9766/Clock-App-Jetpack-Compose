package com.example.composelearnings.ui.state

data class StopWatchUiState(
   val isPlaying: Boolean = false,
   val milliSecond: String = "00",
   val second: String = "00:00",
   val minute: String = ""
)