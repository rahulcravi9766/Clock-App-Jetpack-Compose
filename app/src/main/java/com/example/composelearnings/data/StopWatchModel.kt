package com.example.composelearnings.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composelearnings.utils.Common

@Entity(tableName = "stop_watch_table")
data class StopWatchModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val milliSecond: String = Common.MILLISECOND,
    val second: String = Common.SECONDS,
    val minute: String = "",
    val isPlaying: Boolean = false,
)
