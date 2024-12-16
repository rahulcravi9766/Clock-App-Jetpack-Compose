package com.example.composelearnings.ui.viewmodel

import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import com.example.composelearnings.R
import com.example.composelearnings.data.AlarmSounds
import com.example.composelearnings.ui.state.AlarmUiState
import com.example.composelearnings.ui.state.getDayString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class AlarmScreenViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState = _uiState.asStateFlow()

    var timePicker: TimePickerState? = null

    private val alarms = listOf(
        AlarmSounds("Clock Alarm", R.raw.clock_alarm),
        AlarmSounds("Clock Alarm 2", R.raw.alarm_clock),
        AlarmSounds("Clock Alarm Short", R.raw.alarm_clock_short),
        AlarmSounds("Wind up", R.raw.wind_up_clock_alarm)
    )

    fun setUpAlarmSound() {
        _uiState.update { currentState ->
            currentState.copy(alarmSound = alarms)
        }
    }


    fun setUpVibration(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(canVibrate = value)
        }
    }

    fun openTimePicker(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(openTimePicker = value)
        }
    }

    fun openAlarmSoundsDialog(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(openAlarmsListDialog = value)
        }
    }

    fun selectedAlarmSound(value: AlarmSounds) {
        _uiState.update { currentState ->
            currentState.copy(selectedAlarmSound = value)
        }
    }

    fun initMediaPlayer(value: MediaPlayer?): MediaPlayer? {
        _uiState.update { currentState ->
            currentState.copy(mediaPlayer = value)
        }
        return value
    }

    fun selectedDays(days: MutableList<Int>, index: Int) {
        if (days.contains(index)) {
            days.remove(index)
        } else {
            days.add(index)
        }


        _uiState.update { currentState ->
            currentState.copy(
                selectedDays = days,
                isDaySelected = days.isNotEmpty(),
                selectedDay = getDayString(
                    timePicker?.hour ?: 0,
                    timePicker?.minute ?: 0,
                    days.isNotEmpty(),
                    _uiState.value.selectedDays
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectedTime(value: TimePickerState) {
        timePicker = value
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, value.hour)
        cal.set(Calendar.MINUTE, value.minute)
        cal.isLenient = false
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = formatter.format(cal.time)
        formatter.format(cal.time)

        _uiState.update { currentState ->
            currentState.copy(
                selectedTime = currentTime,
                selectedDay = getDayString(value.hour, value.minute, _uiState.value.isDaySelected, _uiState.value.selectedDays)
            )
        }
    }
}