package com.example.apyblock.domain.repository

import android.content.Context
import android.content.pm.PackageManager
import com.example.apyblock.data.db.AppDatabase
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.models.AppTimeModel
import com.example.apyblock.utils.AllAppsFetchingState
import com.example.apyblock.utils.BannedAppFetchingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class AppDataRepository(private val appDatabase: AppDatabase) {

    suspend fun getBannedApps(): List<AppDataModel> {
        return try {
            appDatabase.appDataDAO().getBanedApps()
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of error
        }
    }

    suspend fun getBannedAppNames(): List<String> {
        return try {
            appDatabase.appDataDAO().getBannedAppNames()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAppTimeDetails(appPackageName: String): AppTimeModel {
        return try {
            appDatabase.appDataDAO().getAppTimeDetails(appPackageName)
        } catch (e: Exception) {
            AppTimeModel(null, null)
        }
    }

    suspend fun updateTime(appData: AppDataModel) {
        appDatabase.appDataDAO().updateAppData(appData = appData)
    }

//    suspend fun addAppData(appData: AppDataModel) {
//        appDatabase.appDataDAO().addAppData(appData = appData)
//    }

    suspend fun addAppData(appData: AppDataModel) {
        val existingApp = appDatabase.appDataDAO().getAppByPackageName(appData.packageName) // Fetch app
        if (existingApp == null) { // Check if exist
            appDatabase.appDataDAO().addAppData(appData = appData) // If not, add
        } else {
            //Update the existing row with `OnConflictStrategy` or notify the user.

        }
    }


    suspend fun removeFromBannedAppsList(packageName: String) {
        appDatabase.appDataDAO().removeFromBannedAppsList(packageName)
    }

    suspend fun getAllAppInSystem(
        context: Context,
        onSuccess: (MutableList<AppDataModel>) -> Unit
    ) {
        withContext(Dispatchers.IO){
            val packageManager = context.packageManager
            val allAppsData = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val bannedAppNames = runCatching { getBannedAppNames() }.getOrElse { emptyList() }

            val allAppModelList = allAppsData.map {appInfo->
                async {
                    AppDataModel(
                        packageName = appInfo.packageName,
                        appName = appInfo.loadLabel(packageManager).toString(),
                        blocked = bannedAppNames.contains(appInfo.packageName),
                        startTime = null,
                        endTime = null
                    )
                }
            }.awaitAll()

            onSuccess(allAppModelList.toMutableList())
        }
    }

    suspend fun getSearchedApps(
        context: Context,
        letter: String,
        onSuccess: (MutableList<AppDataModel>) -> Unit
    ) {
        val packageManager = context.packageManager
        val installedPackages =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val matchingApps = mutableListOf<AppDataModel>()

        for (packageInfo in installedPackages) {
            val appName = packageManager.getApplicationLabel(packageInfo).toString()

            if (appName.contains(letter, ignoreCase = true)) { // Case-insensitive search
                val packageName = packageInfo.packageName
                matchingApps.add(AppDataModel(packageName, appName))
            }
        }

        val bannedAppNames = runCatching { getBannedAppNames() }.getOrElse { emptyList() }
        matchingApps.forEach { app ->
            if (bannedAppNames.contains(app.packageName)) {
                app.blocked = true
            }
        }
        onSuccess(matchingApps)
    }

}