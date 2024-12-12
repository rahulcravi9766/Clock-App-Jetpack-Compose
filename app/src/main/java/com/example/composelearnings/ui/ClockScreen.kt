package com.example.composelearnings.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClockScreen(title: String, modifier: Modifier) {

    var currentTime by remember { mutableStateOf(LocalTime.now())  }
    val currentDate = LocalDate.now()
    val dayOfWeekString = currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val formattedDate = "$dayOfWeekString, ${currentDate.dayOfMonth} ${currentDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}"

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000)
            Log.d("ClockScreen", "ClockScreen: $currentTime")
            currentTime = LocalTime.now()
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm a")),
                fontSize = 50.sp,
                fontWeight = FontWeight.W300
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = formattedDate, fontSize = 20.sp)

        }
    }
}