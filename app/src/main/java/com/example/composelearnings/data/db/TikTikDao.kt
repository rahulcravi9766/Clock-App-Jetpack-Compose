package com.example.composelearnings.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.composelearnings.data.StopWatchModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TikTikDao {

    //Stop Watch
    @Insert
    suspend fun insertStopWatchData(stopWatchModel: StopWatchModel)

    @Query("SELECT * FROM stop_watch_table")
    fun getStopWatchData(): Flow<StopWatchModel>
}