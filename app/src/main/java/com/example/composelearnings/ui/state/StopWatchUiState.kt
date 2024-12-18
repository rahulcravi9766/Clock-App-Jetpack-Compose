package com.example.composelearnings.ui.state

import com.example.composelearnings.utils.Common

data class StopWatchUiState(
   val isPlaying: Boolean = false,
   val milliSecond: String = Common.MILLISECOND,
   val second: String = Common.SECONDS,
   val minute: String = ""
)