package com.example.composelearnings.data

data class AlarmExtraFeatures(
    val title: String,
    val image: Int
)

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)


data class AlarmSounds(
    val title: String,
    val sound: Int,
    val isSelected: Boolean = false
)

data class SelectedDays(
    val day: String,
    val index: Int
)
//data class AddNewAlarm(
//
//)
