package com.example.apyblock.presentation.appInfoScreenUI

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.presentation.viewmodels.TimePickerViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerPrompt(modifier: Modifier = Modifier , timePickerViewModel: TimePickerViewModel , selectedTime: MutableState<String> , confi : String , appData : AppDataModel) {
    val calender = remember{
        Calendar.getInstance()
    }
    val showDialog = if(confi == "Select Start Time"){
        timePickerViewModel.showStartTimeDilaog
    }
    else{
        timePickerViewModel.showEndTimeDialog
    }

    val hour = calender[Calendar.HOUR_OF_DAY]
    val minute = calender[Calendar.MINUTE]

    val timePickerState = rememberTimePickerState(initialHour = hour , initialMinute = minute)

    if(showDialog.value){
        AlertDialog(
            onDismissRequest = {
                if(confi == "Select Start Time"){
                    timePickerViewModel.showStartTimeDialog(false)
                }
                else{
                    timePickerViewModel.showEndTimeDialog(false)
                }
            },
            title = {
                Text(text = confi)
            },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                Button(onClick = {
                    if(confi == "Select Start Time"){
                        timePickerViewModel.showStartTimeDialog(false)
                        timePickerViewModel.startTime.value = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
//                        timePickerViewModel.updateAppDataStartTime(appData = appData)
                    }
                    else{
                        timePickerViewModel.showEndTimeDialog(false)
                        timePickerViewModel.endTime.value = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    //    timePickerViewModel.updateAppDataEndTime(appData = appData)
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    if(confi == "Select Start Time"){
                        timePickerViewModel.showStartTimeDialog(false)
                    }
                    else{
                        timePickerViewModel.showEndTimeDialog(false)
                    }
                }) {
                    Text("Cancel")
                }
            }
        )
    }

}