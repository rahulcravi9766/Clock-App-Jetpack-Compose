package com.example.composelearnings.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composelearnings.R
import com.example.composelearnings.ui.state.StopWatchUiState
import com.example.composelearnings.ui.viewmodel.StopWatchScreenViewModel

@Composable
fun StopWatchScreen(modifier: Modifier, viewModel: StopWatchScreenViewModel = viewModel()) {

    val stopWatchUiState by viewModel.uiState.collectAsState()


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier.weight(4f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(color = Color.DarkGray)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center

            ) {
                BlinkingText(stopWatchUiState, viewModel)
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (stopWatchUiState.milliSecond != "00" || stopWatchUiState.second != "00") {
                RefreshButton(50.dp, Icons.Default.Refresh, viewModel, true) {
                    viewModel.onRefreshButtonClick()
                }
            } else {
                Spacer(modifier = Modifier.size(50.dp))
            }
            PlayPauseButtons(90.dp, stopWatchUiState.isPlaying) {
                viewModel.onPlayPauseButtonClick(!stopWatchUiState.isPlaying)
            }
            Spacer(modifier = Modifier.size(50.dp))
        }
    }
}

@Composable
fun BlinkingText(stopWatchUiState: StopWatchUiState, viewModel: StopWatchScreenViewModel){
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val textColor = if (alpha > 0.5f && viewModel.isPaused) Color.Transparent else Color.Black
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val minutes = if (stopWatchUiState.minute.isNotEmpty()){
            "${stopWatchUiState.minute}:"
        }else{
            ""
        }
        Text(
            text = "$minutes${stopWatchUiState.second}",
            fontSize = 70.sp,
            fontWeight = FontWeight.W400,
            color = textColor
        )
  //      Row(horizontalArrangement = Arrangement.Center) {
//            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = stopWatchUiState.milliSecond,
                fontSize = 40.sp,
                fontWeight = FontWeight.W400,
                color = textColor
            )
      //  }
    }
}

@Composable
fun RefreshButton(
    size: Dp,
    icon: ImageVector,
    viewModel: StopWatchScreenViewModel,
    value: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,

    ) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing), label = ""
    )
    IconToggleButton(
        modifier = modifier.size(size),
        checked = value,
        onCheckedChange = {
            rotationAngle += 360f
            onClick()
        }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Check",
                modifier = Modifier
                    .size(size / 3)
                    .rotate(animatedRotation),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun PlayPauseButtons(
    size: Dp,
    isPlaying: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {


    IconToggleButton(
        modifier = modifier.size(size),
        checked = isPlaying,
        onCheckedChange = {
            onClick()
        }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (isPlaying) {
                Image(
                    painter = painterResource(id = R.drawable.material_symbols__pause),
                    contentDescription = "pause",
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Check",
                    modifier = Modifier.size(size / 3)
                )
            }
        }
    }
}