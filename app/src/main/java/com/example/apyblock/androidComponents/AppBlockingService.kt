package com.example.apyblock.androidComponents

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.example.apyblock.ApyLockApplication
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.repository.AppDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AppBlockingService : AccessibilityService() {

    lateinit var repository: AppDataRepository
    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onServiceConnected() {
        super.onServiceConnected()
        repository = (application as ApyLockApplication).appRepository
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        serviceScope.launch {
            val bannedAppNames = repository.getBannedAppNames()
            if (event != null && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                val packageName = event.packageName.toString()
                if (bannedAppNames.contains(packageName)) {
                    val timeDetail = repository.getAppTimeDetails(packageName)

                    val startTime = if (timeDetail.first != null) {
                        timeDetail.first
                    } else {
                        -1
                    }

                    val endTime = if (timeDetail.second != null) {
                        timeDetail.second
                    } else {
                        -1
                    }

                    performGlobalAction(GLOBAL_ACTION_BACK)
                    Log.d("blockingApp", "Abe padhle")
                }
            }
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


}