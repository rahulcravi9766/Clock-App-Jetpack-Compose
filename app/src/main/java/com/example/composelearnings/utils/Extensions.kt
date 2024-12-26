package com.example.composelearnings.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.w3c.dom.Text


fun Long.addZero(): String {
    return if (this <= 9) {
        "0$this"
    } else {
        this.toString()
    }
}

@Composable
fun CircleButton(isText: Boolean = false, item: Any, fontSize: Int = 30, iconSize: Int = 34, buttonSize: Int = 100) {
    Card(shape = CircleShape) {
        Box(
            modifier = Modifier.size(buttonSize.dp).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (isText){
                Text(item as String, fontSize = fontSize.sp, fontWeight = FontWeight.W400)
            }else{
                Icon(
                    imageVector = item as ImageVector,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize.dp).aspectRatio(1f)
                )
            }
        }
    }
}