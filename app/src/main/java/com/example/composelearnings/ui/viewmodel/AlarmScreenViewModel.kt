package com.example.composelearnings.ui.viewmodel

import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import com.example.composelearnings.R
import com.example.composelearnings.data.AlarmSounds
import com.example.composelearnings.data.SelectedDays
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
            if (currentState.selectedAlarmSound == null) {
                currentState.copy(alarmSound = alarms, selectedAlarmSound = alarms[0])
            } else {
                currentState.copy(alarmSound = alarms)
            }
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

    fun selectedDays(
        index: Int,
        day: String,
        days: MutableList<Pair<String, Int>>
    ) {


        if (!days.any { it.first == day }) {
            days.add(Pair(day, index))
        } else {
            days.remove(Pair(day, index))
        }


        _uiState.update { currentState ->
            currentState.copy(
                selectedDaysIndexed = days,
                isDaySelected = days.isNotEmpty(),
                selectedDay = getDayString(
                    timePicker?.hour ?: 0,
                    timePicker?.minute ?: 0,
                    days.isNotEmpty(),
                    _uiState.value.selectedDaysIndexed
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
        cal.set(Calendar.SECOND, 0)
        cal.isLenient = false
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = formatter.format(cal.time)
        formatter.format(cal.time)

        Log.d("timeMilli", "${cal.timeInMillis}")
        _uiState.update { currentState ->
            currentState.copy(
                selectedTime = currentTime,
                selectedDay = getDayString(
                    value.hour,
                    value.minute,
                    _uiState.value.isDaySelected,
                    _uiState.value.selectedDaysIndexed
                ),
                selectedTimeData = cal,
                timeMillis = cal.timeInMillis,
                hasAlarmScheduled = true
            )
        }
    }

    fun selectRingtone() {

    }
}