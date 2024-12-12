package com.example.composelearnings.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composelearnings.ui.state.ClockUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class ClockScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ClockUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var currentTime: String

    init {
        setCurrentTime()
        setCurrentDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentTime() {
        viewModelScope.launch {
            currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm a"))
            _uiState.update { currentState ->
                currentState.copy(currentTime = currentTime)
            }
            delay(500)
            setCurrentTime()
        }
    }

    private fun setCurrentDate() {
        LocalDate.now().dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())?.let { day ->
            val formattedDate = "$day, ${LocalDate.now().dayOfMonth} ${
                LocalDate.now().month.getDisplayName(
                    TextStyle.SHORT, Locale.getDefault()
                )
            }"

            _uiState.update { currentState ->
                currentState.copy(date = formattedDate)
            }
        }
    }
}