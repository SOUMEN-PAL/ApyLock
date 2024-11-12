package com.example.apyblock.presentation.viewmodels


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.apyblock.domain.models.AppDataModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

    //String to millisecond converter
    @RequiresApi(Build.VERSION_CODES.O)
    fun timeToMilliseconds(time: String): Long {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val localTime = LocalTime.parse(time, formatter)
        return LocalDate.now().atTime(localTime)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

}