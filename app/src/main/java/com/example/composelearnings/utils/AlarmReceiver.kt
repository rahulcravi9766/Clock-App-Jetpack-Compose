package com.example.composelearnings.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val soundUriString = intent?.getStringExtra("soundUri")
        val soundUri = Uri.parse(soundUriString)
        val mediaPlayer = MediaPlayer.create(context, soundUri)
        mediaPlayer.start()
    }
}