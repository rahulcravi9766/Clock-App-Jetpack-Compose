package com.example.composelearnings.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.composelearnings.R
import com.example.composelearnings.data.BottomNavigationItem


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationScreen() {
    val items = listOf(
        BottomNavigationItem(
            "Alarm",
            R.drawable.ic__outline_alarm,
            R.drawable.ic__outline_alarm
        ),
        BottomNavigationItem(
            "Clock",
            R.drawable.mdi__clock_outline,
            R.drawable.mdi__clock_outline
        ),
        BottomNavigationItem(
            "Timer",
            R.drawable.mdi__timer_sand_complete,
            R.drawable.mdi__timer_sand_complete
        ),
        BottomNavigationItem(
            "Stopwatch",
            R.drawable.mdi__stopwatch_outline,
            R.drawable.mdi__stopwatch_outline
        ),
    )

    var selectedIconIndex by rememberSaveable {
        mutableIntStateOf(3)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = items[selectedIconIndex].title)
                },
            )
        },
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, bottomNavigationItem ->
                        NavigationBarItem(
                            label = {
                                Text(text = bottomNavigationItem.title)
                            },
                            selected = selectedIconIndex == index,
                            onClick = { selectedIconIndex = index },
                            icon = {
                                Image(
                                    painter = if (index == selectedIconIndex) painterResource(
                                        id = bottomNavigationItem.selectedIcon
                                    ) else painterResource(id = bottomNavigationItem.selectedIcon),
                                    contentDescription = bottomNavigationItem.title,
                                    modifier = Modifier
                                )
                            })
                    }
                }
            }
        ) { paddingValues ->
            when (selectedIconIndex) {
                0 -> {
                    AlarmScreen(modifier = Modifier.padding(paddingValues))
                }

                1 -> {
                    ClockScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                2 -> {
                }

                3 -> {
                    StopWatchScreen(modifier = Modifier.padding(paddingValues))
                }

            }
        }
    }
}




