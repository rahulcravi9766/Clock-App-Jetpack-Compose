package com.example.composelearnings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composelearnings.utils.CircleButton
import com.example.composelearnings.utils.Common

@Composable
fun TimerScreen(modifier: Modifier) {
    AddTimer(modifier)

}

@Composable
fun AddTimer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = Common.TIMER,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 46.sp,
            fontWeight = FontWeight.W300
        )
        Spacer(modifier = Modifier.height(16.dp))
        ListOfNumbers()
        Spacer(modifier = Modifier.height(12.dp))
        Row( horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            CircleButton(item = Icons.Filled.PlayArrow, iconSize = 34)
        }
    }
}

@Composable
fun ListOfNumbers() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),

        ) {
        itemsIndexed(items = Common.numbers){ index, item ->
            NumberViews(item, index)
        }
    }
}

@Composable
fun NumberViews(item: Any, index: Int) {
    Card(shape = CircleShape) {
        Box(modifier = Modifier.fillMaxSize().aspectRatio(1f), contentAlignment = Alignment.Center) {
            if (item is Int) {
                Text(item.toString(), fontSize = 30.sp, fontWeight = FontWeight.W400)
            }else{
                if (index == 11){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(34.dp)
                    )
                }else{
                    Text(item as String, fontSize = 30.sp, fontWeight = FontWeight.W400)

                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun TimerScreenPreview() {
    AddTimer()
}