package com.example.composelearnings.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.composelearnings.TikTikApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            StopWatchScreenViewModel(tikTikApplication().container.tikTikRepository)
        }
    }
}

fun CreationExtras.tikTikApplication(): TikTikApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TikTikApplication)