package com.example.composelearnings.data

import android.content.Context
import com.example.composelearnings.data.db.TikTikDatabase

interface AppContainer {
    val tikTikRepository: TikTikRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val tikTikRepository: TikTikRepository by lazy {
        OfflineRepository(TikTikDatabase.getDatabase(context).tikTikDao())
    }
}