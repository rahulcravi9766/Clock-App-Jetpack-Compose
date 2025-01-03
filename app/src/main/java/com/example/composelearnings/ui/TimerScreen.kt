package com.example.composelearnings.ui

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composelearnings.data.TimerModel
import com.example.composelearnings.ui.viewmodel.AppViewModelProvider
import com.example.composelearnings.ui.viewmodel.TimerScreenViewModel
import com.example.composelearnings.utils.CircleButton
import com.example.composelearnings.utils.Common

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val timerUiState by viewModel.uiState.collectAsState()
    AddTimer(modifier, timerUiState, viewModel)

}

@Composable
fun AddTimer(
    modifier: Modifier = Modifier,
    timerUiState: TimerModel,
    viewModel: TimerScreenViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            val list: List<Int?> = timerUiState.listOfNumbers
            val timeText = if (list.isNotEmpty()) {
                "${list[0] ?: 0}${list[1] ?: 0}h ${list[2] ?: 0}${list[3] ?: 0}m ${list[4] ?: 0}${list[5] ?: 0}s"
            } else {
                Common.TIMER
            }
            val position = viewModel.findFirstDigitPosition(timeText)
            val annotatedString = if (position <= -1) {
                AnnotatedString(timeText)
            } else {
                buildAnnotatedString {
                    append(timeText.substring(0, position))
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append(timeText.substring(position))
                    }
                }
            }

            Log.d("position", "AddTimer: $position")
            Text(
                text = annotatedString,
                textAlign = TextAlign.Center,
                fontSize = 46.sp,
                fontWeight = FontWeight.W300,
                color = CardDefaults.cardColors().containerColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        ListOfNumbers(viewModel, timerUiState)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            CircleButton(item = Icons.Filled.PlayArrow, iconSize = 90, isPlaying = false)
        }
    }
}

@Composable
fun ListOfNumbers(viewModel: TimerScreenViewModel, timerUiState: TimerModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),

        ) {
        itemsIndexed(items = Common.numbers) { index, item ->
            NumberViews(item, index, viewModel, timerUiState)
        }
    }
}

@Composable
fun NumberViews(item: Any, index: Int, viewModel: TimerScreenViewModel, timerUiState: TimerModel) {
    Card(shape = CircleShape) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clickable {
                    viewModel.addedTime(item)
                    Log.d("TAG", "NumberViews: ${timerUiState.listOfNumbers}")
                },
            contentAlignment = Alignment.Center
        ) {
            if (item is Int) {
                Text(item.toString(), fontSize = 30.sp, fontWeight = FontWeight.W400)
            } else {
                if (index == 11) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(34.dp)
                    )
                } else {
                    Text(item as String, fontSize = 30.sp, fontWeight = FontWeight.W400)

                }
            }
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun TimerScreenPreview() {
    TimerScreen()
}