package com.example.apyblock.domain.repository

import com.example.apyblock.data.db.AppDatabase
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.models.AppTimeModel
import com.example.apyblock.utils.BannedAppFetchingState
import kotlinx.coroutines.flow.MutableStateFlow

class AppDataRepository(private val appDatabase:AppDatabase) {

    suspend fun getBannedApps(): List<AppDataModel> {
        return try {
            appDatabase.appDataDAO().getBanedApps()
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of error
        }
    }

    suspend fun getBannedAppNames() : List<String>{
        return try{
            appDatabase.appDataDAO().getBannedAppNames()
        } catch (e: Exception){
            emptyList()
        }
    }

    suspend fun getAppTimeDetails(appPackageName : String):AppTimeModel{
        return try{
            appDatabase.appDataDAO().getAppTimeDetails(appPackageName)
        }catch (e: Exception){
            AppTimeModel(null , null)
        }
    }

    suspend fun updateTime(appData : AppDataModel){
        appDatabase.appDataDAO().updateAppData(appData = appData)
    }

    suspend fun addAppData(appData: AppDataModel){
        appDatabase.appDataDAO().addAppData(appData = appData)
    }

}