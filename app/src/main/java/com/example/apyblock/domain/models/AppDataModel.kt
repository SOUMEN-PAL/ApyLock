package com.example.apyblock.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity("blocked_apps")
data class AppDataModel(
    @PrimaryKey
    val packageName : String,
    val appName : String,
    var blocked : Boolean = false,
    var startTime: Long? = null,
    var endTime : Long? = null
)
