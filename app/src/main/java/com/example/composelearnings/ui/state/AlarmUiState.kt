package com.example.composelearnings.ui.state

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import com.example.composelearnings.data.AlarmSounds
import java.time.LocalTime
import java.util.Calendar

data class AlarmUiState(
    val title: String = "",
    val canVibrate: Boolean = false,
    val openTimePicker: Boolean = false,
    val selectedTime: String = "Select Time",
    val selectedTimeData: Calendar? = null,
    val timeMillis: Long = 0,
    val selectedDay: String = "",
    val selectedDaysIndexed: MutableList<Pair<String, Int>> = mutableListOf(),
    val isDaySelected: Boolean = false,
    val alarmSound: List<AlarmSounds> = emptyList(),
    val openAlarmsListDialog: Boolean = false,
    val selectedAlarmSound: AlarmSounds? = null,
    val selectedAlarmText: String = "",
    val mediaPlayer: MediaPlayer? = null,
    val hasAlarmScheduled: Boolean = false
)

@SuppressLint("NewApi")
fun getDayString(
    alarmHour: Int,
    alarmMinute: Int,
    isDaySelected: Boolean,
    selectedDaysIndexed: MutableList<Pair<String, Int>>
): String {

    Log.d("AlarmUiState", "getDayString: $alarmHour $alarmMinute $isDaySelected")
    val currentTime = LocalTime.now()
    val currentHour = currentTime.hour
    val currentMinute = currentTime.minute

    return if (isDaySelected) {
        if (selectedDaysIndexed.size == 7){
            "Everyday"
        }else{
            selectedDaysIndexed.sortBy { it.second }
            selectedDaysIndexed.joinToString { it.first.take(3) }
        }

    } else {
        if (alarmHour > currentHour || (alarmHour == currentHour && alarmMinute > currentMinute)) {
            "Today"
        } else {
            "Tomorrow"
        }
    }
}
