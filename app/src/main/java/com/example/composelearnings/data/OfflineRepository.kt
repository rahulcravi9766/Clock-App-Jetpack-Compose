package com.example.composelearnings.data

import com.example.composelearnings.data.db.TikTikDao
import kotlinx.coroutines.flow.Flow

class OfflineRepository(private val tikTikDao: TikTikDao) : TikTikRepository {
    override fun getStopWatchData(): Flow<StopWatchModel> = tikTikDao.getStopWatchData()

    override suspend fun insertStopWatchData(stopWatchModel: StopWatchModel) =
        tikTikDao.insertStopWatchData(stopWatchModel)
}