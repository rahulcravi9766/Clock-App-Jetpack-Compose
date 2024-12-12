package com.example.composelearnings.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.createOneShot
import android.os.Vibrator
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composelearnings.R
import com.example.composelearnings.data.AlarmExtraFeatures
import com.example.composelearnings.utils.Common

@Composable
fun AlarmScreen(modifier: Modifier) {
    val miniPadding = dimensionResource(R.dimen.padding_mini)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = miniPadding)

    ) {
        AlarmTimings()
    }
}

@Composable
fun AlarmTimings() {
    var expandedState by remember { mutableStateOf(true) }
    var switchButtonState by remember { mutableStateOf(false) }
    val selectedDaysIndex = remember { mutableStateOf(mutableListOf<Int>()) }
    val checked = remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f,
        label = "dropDown"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            ),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = "6:00 am",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W300
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop Down"
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = "Mon, Tue",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W200
                )
                Switch(
                    modifier = Modifier.weight(1f),
                    checked = switchButtonState,
                    onCheckedChange = {
                        switchButtonState = !switchButtonState
                    }
                )
            }

            if (expandedState) {
                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    DaysButton(selectedDaysIndex, checked)
                    Spacer(modifier = Modifier.height(15.dp))
                    AlarmFeatures()

                }
            }
        }
    }
}

@Composable
fun AlarmFeatures() {

    val features = listOf(
        AlarmExtraFeatures(
            title = "Ringtone",
            image = R.drawable.ci__bell_ring,
        ),
        AlarmExtraFeatures(
            title = "Vibrate",
            image = R.drawable.ic__baseline_vibration,
        ),
        AlarmExtraFeatures(
            title = "Delete",
            image = R.drawable.material_symbols__delete_outline,
        ),
    )
    LazyColumn {
        itemsIndexed(items = features) { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
//                when (index) {
//                    1 -> {
//                        VibrateDevice()
//                    }
//                }
            }) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = item.title,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = item.title, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun VibrateDevice() {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }

    val effect = createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(effect)

}


@Composable
fun DaysButton(selectedDaysIndex: MutableState<MutableList<Int>>, checked: MutableState<Boolean>) {


    LazyHorizontalGrid(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        rows = GridCells.Fixed(1),
    ) {

        itemsIndexed(items = Common.daysOfWeek) { index, item ->
            IconToggleButton(
                modifier = Modifier.size(30.dp), checked = checked.value, onCheckedChange = {
                    checked.value = !checked.value
                    if (selectedDaysIndex.value.contains(index)) {
                        selectedDaysIndex.value.remove(index)
                    } else selectedDaysIndex.value.add(index)
                }) {
                val tint =
                    if (selectedDaysIndex.value.contains(index)) Color(0xFFEC407A) else Color.White

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(color = Color.LightGray)
                        .padding(1.dp)
                        .clip(CircleShape)
                        .background(color = tint), contentAlignment = Alignment.Center
                ) {
                    Text(item.take(1))
                }
            }
        }
    }
}

@Composable
@Preview
fun ExpandableCardPreview() {
    //DaysButton(selectedDaysIndex, checked)
}