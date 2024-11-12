package com.example.apyblock.androidComponents

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.apyblock.ApyLockApplication
import com.example.apyblock.R
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.repository.AppDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class AppBlockingService : AccessibilityService() {

    lateinit var repository: AppDataRepository
    private lateinit var serviceScope: CoroutineScope
    private var bannedAppNames: List<String> = emptyList()

    override fun onServiceConnected() {
        super.onServiceConnected()
        repository = (application as ApyLockApplication).appRepository
        serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        // Fetch banned app names initially
//        serviceScope.launch {
//            while (true) {
//                bannedAppNames = repository.getBannedAppNames()
//                delay(60 * 1000) // Refresh every minute, or based on another trigger
//            }
//        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event != null && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            serviceScope.launch {
                    bannedAppNames = repository.getBannedAppNames()
            }

            Log.d("AppBlockingService", "Event: $event")
            Log.d("AppBlockingService", "Package name: ${event?.packageName}")
            val packageName = event?.packageName?.toString() ?: ""
            if (bannedAppNames.contains(packageName)) {
                serviceScope.launch {
                    // Perform database operations on a background thread
                    withContext(Dispatchers.IO) {
                        val timeDetail = repository.getAppTimeDetails(packageName)
                        // ... (other logic)
                        val startTime = timeDetail.startTime
                        val endTime = timeDetail.endTime
                        withContext(Dispatchers.Main) {
                            if (startTime != null && endTime != null) {
                                val currentDateTime = LocalDateTime.now()
                                val startDateTime = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
                                val endDateTime = Instant.ofEpochMilli(endTime).atZone(ZoneId.systemDefault()).toLocalDateTime()

                                if (startDateTime.isAfter(endDateTime)) { // Time range crosses midnight
                                    if (currentDateTime.isAfter(startDateTime) || currentDateTime.isBefore(endDateTime)) {
                                        // App should be blocked
                                        performGlobalAction(GLOBAL_ACTION_HOME)
                                        Log.d("blockingApp", "Blocking $packageName")
                                    }
                                } else { // Time range within the same day
                                    if (currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime)) {
                                        // App should be blocked
                                        performGlobalAction(GLOBAL_ACTION_HOME)
                                        Log.d("blockingApp", "Blocking $packageName")
                                    }
                                }
                            }
                        }
                    }

                    // Perform blocking action (consider alternatives)
//                    performGlobalAction(GLOBAL_ACTION_HOME)
//                    Log.d("blockingApp", "Blocking $packageName")
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



    private fun showBlockingOverlay(packageName: String) {
        // Show an overlay or notification informing the user about the block
    }

}