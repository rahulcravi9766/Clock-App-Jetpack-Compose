package com.example.composelearnings.data

import com.example.composelearnings.utils.Common

data class TimerModel(
    val id: Int = 1,
    val hour: String = "00",
    val minute: String = "00",
    val second: String = "00",
    val listOfNumbers: List<Int> = listOf(),
    val totalTime: String = Common.TIMER,
    val isPlaying: Boolean = false,

    )