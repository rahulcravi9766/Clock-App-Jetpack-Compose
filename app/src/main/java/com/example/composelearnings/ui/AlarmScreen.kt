package com.example.composelearnings.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.createOneShot
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composelearnings.R
import com.example.composelearnings.data.AlarmExtraFeatures
import com.example.composelearnings.ui.state.AlarmUiState
import com.example.composelearnings.ui.viewmodel.AlarmScreenViewModel
import com.example.composelearnings.utils.AlarmReceiver
import com.example.composelearnings.utils.Common
import java.util.Calendar


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmScreen(modifier: Modifier, viewModel: AlarmScreenViewModel = viewModel()) {
    val miniPadding = dimensionResource(R.dimen.padding_mini)
    val alarmUiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxWidth()

                .padding(
                    start = miniPadding,
                    end = miniPadding,
                    top = paddingValues.calculateTopPadding()
                )

        ) {
            AlarmTimings(alarmUiState, viewModel)
        }
    }

    if (alarmUiState.openTimePicker) {
        OpenTimePicker(onConfirm = { time ->
            viewModel.selectedTime(time)
            viewModel.openTimePicker(false)
        }, onDismiss = {
            viewModel.openTimePicker(false)
        }, viewModel, alarmUiState)
    }

    if (alarmUiState.openAlarmsListDialog) {
        SoundsDialogueBox(viewModel, alarmUiState)
    }
    if (alarmUiState.hasAlarmScheduled) {
        val intent = Intent(context, AlarmReceiver::class.java)
        Log.d("AlarmReceiver", "Intent is not null ${intent}")
        val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)


        intent.apply {
            if (alarmUiState.selectedAlarmSound == null) {
                //val soundUri = "android.resource://" + context.packageName + "/" + item.sound
                putExtra("soundUri", "android.resource://" + context.packageName + "/" + alarmUiState.alarmSound[0].sound)
            } else {
                putExtra("soundUri", "android.resource://" + context.packageName + "/" + alarmUiState.selectedAlarmSound?.sound)
            }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmUiState.timeMillis,
                    pendingIntent
                )
            } else {
                requestExactAlarmPermission(context)
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmUiState.selectedTimeData?.timeInMillis ?: 0,
                pendingIntent
            )
        }

        val receiver = ComponentName(context, AlarmReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmTimings(alarmUiState: AlarmUiState, viewModel: AlarmScreenViewModel) {
    var expandedState by remember { mutableStateOf(true) }
    var switchButtonState by remember { mutableStateOf(false) }
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
                    modifier = Modifier
                        .weight(6f)
                        .clickable {
                            viewModel.openTimePicker(true)
                        },
                    text = alarmUiState.selectedTime,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W400
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
                    text = alarmUiState.selectedDay,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W400
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
                    DaysButton(checked, alarmUiState, viewModel)
                    Spacer(modifier = Modifier.height(15.dp))
                    AlarmFeatures(alarmUiState, viewModel)

                }
            }
        }
    }
}

@Composable
fun AlarmFeatures(alarmUiState: AlarmUiState, viewModel: AlarmScreenViewModel) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    viewModel.setUpAlarmSound()
    val features = listOf(
        AlarmExtraFeatures(
            title = alarmUiState.selectedAlarmSound?.title ?: "Select Alarm",
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

                when (index) {
                    0 -> {
                        viewModel.openAlarmSoundsDialog(true)
                    }

                    1 -> {

                        if (!alarmUiState.canVibrate) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(
                                    createOneShot(
                                        500L,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )

                            }
                        }
                        viewModel.setUpVibration(!alarmUiState.canVibrate)
                    }
                }
            }) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = item.title,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = item.title, fontSize = 15.sp)

                if (index == 1) {
                    Spacer(modifier = Modifier.weight(fill = true, weight = 1f))
                    IconToggleButton(
                        modifier = Modifier.size(30.dp),
                        checked = alarmUiState.canVibrate,
                        onCheckedChange = {
                            viewModel.setUpVibration(!alarmUiState.canVibrate)

                        }) {
                        val tint =
                            if (alarmUiState.canVibrate) MaterialTheme.colorScheme.primaryContainer else Color.White

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(color = Color.LightGray)
                                .padding(1.dp)
                                .clip(CircleShape)
                                .background(color = tint), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check",
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CheckBox(viewModel: AlarmScreenViewModel, alarmUiState: AlarmUiState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(fill = true, weight = 1f))
        IconToggleButton(
            modifier = Modifier.size(30.dp),
            checked = alarmUiState.canVibrate,
            onCheckedChange = {
                viewModel.setUpVibration(!alarmUiState.canVibrate)

            }) {

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color = Color.LightGray)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check",
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}

@Composable
fun DaysButton(
    checked: MutableState<Boolean>,
    alarmUiState: AlarmUiState,
    viewModel: AlarmScreenViewModel
) {

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
                    viewModel.selectedDays(index, item, alarmUiState.selectedDaysIndexed)
                }) {

                val daySelected = alarmUiState.selectedDaysIndexed.any { it.second == index }
                val tint =
                    if (daySelected) MaterialTheme.colorScheme.primaryContainer else Color.White

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    viewModel: AlarmScreenViewModel,
    alarmUiState: AlarmUiState,
) {
    val currentTime = Calendar.getInstance()
    val context = LocalContext.current
    val timePickerState = if (viewModel.timePicker != null) {
        viewModel.timePicker
    } else {
        rememberTimePickerState(
            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = currentTime.get(Calendar.MINUTE),
            is24Hour = false,
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
            .background(MaterialTheme.colorScheme.surfaceBright),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        timePickerState?.let {
            TimePicker(
                state = it,
            )
        }
        Button(onClick = onDismiss) {
            Text("Dismiss")
        }
        Button(onClick = {
            timePickerState?.let {
                onConfirm(it)
                //    scheduleAlarm(context, alarmUiState)
            }

        }) {
            Text("Confirm")
        }
    }
}


@Composable
fun SoundsDialogueBox(viewModel: AlarmScreenViewModel, alarmUiState: AlarmUiState) {
    var showDialog by remember { mutableStateOf(true) }
    val context = LocalContext.current
    Button(onClick = { showDialog = true }) {
        Text("Show Dialog")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Alarm Sounds") },
            text = {
                LazyColumn {
                    items(alarmUiState.alarmSound) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {

                            Text(
                                text = item.title,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {

                                    viewModel.selectedAlarmSound(item.copy(isSelected = true))
                                    alarmUiState.mediaPlayer?.reset()
                                    val soundUri =
                                        Uri.parse("android.resource://" + context.packageName + "/" + item.sound)
                                    val mediaPlayerNew = viewModel.initMediaPlayer(
                                        MediaPlayer.create(
                                            context,
                                            soundUri
                                        )
                                    )
                                    mediaPlayerNew?.start()
                                })
                            Spacer(modifier = Modifier.weight(1f))
                            if (alarmUiState.selectedAlarmSound?.title == item.title) {
                                CheckBox(viewModel, alarmUiState)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    alarmUiState.mediaPlayer?.stop()
                    alarmUiState.mediaPlayer?.reset()
                    viewModel.openAlarmSoundsDialog(false)
                    showDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = {
                    alarmUiState.mediaPlayer?.stop()
                    alarmUiState.mediaPlayer?.reset()
                    viewModel.openAlarmSoundsDialog(false)
                    showDialog = false
                }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@SuppressLint("NewApi")
fun scheduleAlarm(context: Context, alarmUiState: AlarmUiState, intent: Intent) {
    val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    intent.apply {
        if (alarmUiState.selectedAlarmSound == null) {
            putExtra("soundUri", "hai rahul")
        } else {
            putExtra("soundUri", "Hello")
        }
    }

    val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmUiState.timeMillis,
                pendingIntent
            )
        } else {
            requestExactAlarmPermission(context)
        }
    } else {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmUiState.timeMillis,
            pendingIntent
        )
    }
}

@SuppressLint("NewApi")
fun requestExactAlarmPermission(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (!alarmManager.canScheduleExactAlarms()) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        context.startActivity(intent)
    }
}

@Composable
@Preview
fun ExpandableCardPreview() {
    //SoundsDialogueBox()
}

