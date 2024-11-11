package com.example.apyblock.presentation.viewmodels


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.apyblock.domain.models.AppDataModel
import java.text.SimpleDateFormat
import java.util.*


class TimePickerViewModel : ViewModel() {
    private val _showStartTimeDialog = mutableStateOf(false)
    val showStartTimeDilaog = _showStartTimeDialog


    private val _showEndTimeDialog = mutableStateOf(false)
    val showEndTimeDialog = _showEndTimeDialog

    var startTime = mutableStateOf("")
    var endTime = mutableStateOf("")

    fun showStartTimeDialog(state : Boolean) {
        _showStartTimeDialog.value = state
    }

    fun showEndTimeDialog(state : Boolean) {
        _showEndTimeDialog.value = state
    }


//    fun updateAppDataStartTime(appData : AppDataModel){
//        appData.startTime = timeToMilliseconds(startTime.value)
//
//    }
//
//    fun updateAppDataEndTime(appData : AppDataModel){
//        appData.endTime = timeToMilliseconds(endTime.value)
//    }


    fun reset(){
        startTime.value = ""
        endTime.value = ""
        _showStartTimeDialog.value = false
        _showEndTimeDialog.value = false
    }

    fun timeToMilliseconds(time: String): Long {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC") // Set to UTC to get milliseconds since midnight
        return format.parse(time)?.time ?: 0L
    }


}