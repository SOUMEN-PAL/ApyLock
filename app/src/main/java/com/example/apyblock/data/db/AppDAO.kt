package com.example.apyblock.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.models.AppTimeModel

@Dao
interface AppDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAppData(appData : AppDataModel)

    @Query("SELECT * FROM blocked_apps")
    suspend fun getBanedApps() : List<AppDataModel>

    @Query("SELECT packageName FROM blocked_apps")
    suspend fun getBannedAppNames() : List<String>

    @Query("SELECT startTime,endTime FROM blocked_apps WHERE packageName= :appPackageName")
    suspend fun getAppTimeDetails(appPackageName : String) : AppTimeModel

    @Query("DELETE FROM blocked_apps WHERE packageName = :packageName")
    suspend fun removeFromBannedAppsList(packageName : String)

    @Update
    suspend fun updateAppData(appData: AppDataModel)

    @Query("SELECT * FROM blocked_apps WHERE packageName = :packageName")
    suspend fun getAppByPackageName(packageName: String): AppDataModel?
}