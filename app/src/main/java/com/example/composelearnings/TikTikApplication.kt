package com.example.composelearnings

import android.app.Application
import com.example.composelearnings.data.AppContainer
import com.example.composelearnings.data.AppDataContainer

class TikTikApplication: Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}