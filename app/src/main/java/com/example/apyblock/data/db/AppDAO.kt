package com.example.apyblock.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.apyblock.domain.models.AppDataModel

@Dao
interface AppDAO {
    @Insert
    suspend fun addAppData(appData : AppDataModel)

    @Query("SELECT * FROM blocked_apps")
    suspend fun getBanedApps() : List<AppDataModel>

    @Query("SELECT packageName FROM blocked_apps")
    suspend fun getBannedAppNames() : List<String>

    @Query("SELECT startTime,endTime FROM blocked_apps WHERE packageName= :appPackageName")
    suspend fun getAppTimeDetails(appPackageName : String) : Pair<Long , Long>

    @Update
    suspend fun updateAppData(appData: AppDataModel)

}