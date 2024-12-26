package com.example.composelearnings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composelearnings.ui.BottomNavigationScreen
import com.example.composelearnings.ui.theme.ComposeLearningsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLearningsTheme(darkTheme = false) {
                BottomNavigationScreen()
            }
        }
    }
}
