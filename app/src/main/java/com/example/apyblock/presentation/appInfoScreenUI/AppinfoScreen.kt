package com.example.apyblock.presentation.appInfoScreenUI

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apyblock.R
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.presentation.navigation.Screens
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.example.apyblock.presentation.viewmodels.TimePickerViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppInfoScreen(
    modifier: Modifier = Modifier ,
    appData: AppDataModel , mainViewModel: MainViewModel ,
    timePickerViewModel: TimePickerViewModel,
    navController: NavController
) {

    BackHandler {
        navController.popBackStack()
    }

    val context = LocalContext.current
    val packageName = appData.packageName
    val appName = appData.appName
    var appIcon by remember { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(key1 = packageName) { // Trigger when packageName changes
        appIcon = mainViewModel.getAPPIcon(context, packageName)
    }

    val appIconPainter = rememberDrawablePainter(drawable = appIcon)

    val startTime = timePickerViewModel.startTime
    val endTime = timePickerViewModel.endTime

    LaunchedEffect(key1 = appData) {
        // Initialize startTime and endTime only once or when appData changes
        if (startTime.value.isEmpty()) { // Check if already initialized
            startTime.value = appData.startTime?.let { formatStartTime(it) } ?: ""
        }
        if (endTime.value.isEmpty()) { // Check if already initialized
            endTime.value = appData.endTime?.let { formatStartTime(it) } ?: ""
        }
    }



//    val startTime = timePickerViewModel.startTime
//    timePickerViewModel.startTime.value = formatStartTime(appData.startTime!!)
//
//
//    val endTime = timePickerViewModel.endTime
//    timePickerViewModel.endTime.value = formatStartTime(appData.endTime!!)


//    val showStartTimeDialog = timePickerViewModel.showStartTimeDilaog
//
//    val showEndTimeDialog = timePickerViewModel.showEndTimeDialog

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = colorResource(id = R.color.background_dark),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.navigate(Screens.blockedAppScreen.route)
                },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(color = colorResource(id = R.color.white), shape = CircleShape)
                    ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Arrow Back",
                        tint = colorResource(
                            id = R.color.appGreen
                        )
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = appIconPainter,
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(180.dp)
            )

            Text(
                text = appName,
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            TimePickerPrompt(timePickerViewModel = timePickerViewModel, selectedTime = startTime, confi = "Select Start Time" , appData = appData)

            OutlinedTextField(
                value = startTime.value,
                singleLine = true,
                readOnly = true,
                onValueChange = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        timePickerViewModel.showStartTimeDialog(true)
                    }) {
                        Icon(imageVector = Icons.Filled.LockClock, contentDescription = "start Time")
                    }

                },
                label = {

                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.appGreen),
                    unfocusedBorderColor = colorResource(id = R.color.appGreen),
                    unfocusedTextColor = colorResource(id = R.color.white),
                    focusedTextColor = colorResource(id = R.color.white)
                )

            )



            TimePickerPrompt(timePickerViewModel = timePickerViewModel, selectedTime = endTime, confi = "Select End Time" , appData = appData)

            OutlinedTextField(
                value = endTime.value,
                singleLine = true,
                readOnly = true,
                onValueChange = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        timePickerViewModel.showEndTimeDialog(true)
                    }) {
                        Icon(imageVector = Icons.Filled.LockClock, contentDescription = "start Time")
                    }

                },
                label = {

                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.appGreen),
                    unfocusedBorderColor = colorResource(id = R.color.appGreen),
                    unfocusedTextColor = colorResource(id = R.color.white),
                    focusedTextColor = colorResource(id = R.color.white)
                )

            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatStartTime(startTimeInMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(startTimeInMillis)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern("HH:mm") // Customize the format as needed
    return dateTime.toJavaLocalDateTime().format(formatter)
}
