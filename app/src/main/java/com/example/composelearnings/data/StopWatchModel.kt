package com.example.composelearnings.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composelearnings.utils.Common

@Entity(tableName = "stop_watch_table")
data class StopWatchModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val milliSecond: String = Common.MILLISECOND,
    val second: String = Common.SECONDS,
    var elapsedMillis: Long = 0L,
    val minute: String = "",
    val isPlaying: Boolean = false,
)
