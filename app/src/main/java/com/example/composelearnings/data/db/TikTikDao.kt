package com.example.composelearnings.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.composelearnings.data.StopWatchModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TikTikDao {

    //Stop Watch
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStopWatchData(stopWatchModel: StopWatchModel)

    @Query("SELECT * FROM stop_watch_table")
    fun getStopWatchData(): Flow<StopWatchModel>


    @Query("DELETE FROM stop_watch_table")
    suspend fun deleteAllData()

}