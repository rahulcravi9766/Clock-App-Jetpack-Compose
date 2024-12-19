package com.example.composelearnings.data

import android.content.Context

interface AppContainer {
    val tikTikRepository: TikTikRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val tikTikRepository: TikTikRepository
        get() = TODO("Not yet implemented")


}