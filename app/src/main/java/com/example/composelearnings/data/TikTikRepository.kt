package com.example.composelearnings.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface TikTikRepository {

    fun getStopWatchData(): Flow<StopWatchModel>

    suspend fun insertStopWatchData(stopWatchModel: StopWatchModel)
}

