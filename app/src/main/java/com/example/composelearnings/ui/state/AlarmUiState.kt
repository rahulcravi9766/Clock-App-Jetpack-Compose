package com.example.composelearnings.ui.state

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import com.example.composelearnings.data.AlarmSounds
import java.time.LocalTime

data class AlarmUiState (
    val title: String = "",
    val canVibrate: Boolean = false,
    val openTimePicker: Boolean = false,
    val selectedTime: String = "Select Time",
    val selectedDay: String = "",
    val selectedDays: MutableList<Int> = mutableListOf(),
    val isDaySelected: Boolean = false,
    val alarmSound: List<AlarmSounds> = emptyList(),
    val openAlarmsListDialog: Boolean = false,
    val selectedAlarmSound: AlarmSounds? = null,
    val mediaPlayer: MediaPlayer? = null
)

@SuppressLint("NewApi")
fun getDayString(
    alarmHour: Int,
    alarmMinute: Int,
    isDaySelected: Boolean,
    selectedDays: MutableList<Int>
): String{

    Log.d("AlarmUiState", "getDayString: $alarmHour $alarmMinute $isDaySelected")
    val currentTime = LocalTime.now()
    val currentHour = currentTime.hour
    val currentMinute = currentTime.minute

    return if (isDaySelected){
        val efe = selectedDays.mapIndexed { index, i ->   }
        "add the selected day here"
    }else{
        if (alarmHour > currentHour || (alarmHour == currentHour && alarmMinute > currentMinute)) {
            "Today"
        } else {
            "Tomorrow"
        }
    }
}
