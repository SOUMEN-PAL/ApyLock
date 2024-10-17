package com.example.apyblock.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("blocked_apps")
data class AppDataModel(
    @PrimaryKey
    val packageName : String,
    val appName : String,
    val blocked : Boolean = false,
    val startTime: Long? = null,
    val endTime : Long? = null
)
