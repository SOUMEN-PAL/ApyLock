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
    private var bannedAppNamesCache : List<String>? = null

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

    suspend fun addAppData(appData: AppDataModel) {
        appDatabase.appDataDAO().addAppData(appData = appData)
    }

    suspend fun removeFromBannedAppsList(packageName:String){
        appDatabase.appDataDAO().removeFromBannedAppsList(packageName)
    }

    suspend fun getAllAppInSystem(context: Context, onSuccess: (MutableList<AppDataModel>) -> Unit) {
        withContext(Dispatchers.IO) {
            val packageManager = context.packageManager
            val allAppsData = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

            // Fetch banned app names only if cache is empty
            val bannedAppNames = bannedAppNamesCache ?: runCatching { getBannedAppNames() }
                .getOrElse { emptyList() }
                .also { bannedAppNamesCache = it } // Update cache

            val allAppModelList = allAppsData.map { appInfo ->
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

    suspend fun getSearchedApps(context: Context, letter: String, onSuccess: (MutableList<AppDataModel>) -> Unit) {
        withContext(Dispatchers.IO) {
            val packageManager = context.packageManager
            val installedPackages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

            // Fetch banned app names only if cache is empty
            val bannedAppNames = bannedAppNamesCache ?: runCatching { getBannedAppNames() }
                .getOrElse { emptyList() }
                .also { bannedAppNamesCache = it } // Update cache

            val matchingApps = installedPackages.filter { packageInfo ->
                val appName = packageManager.getApplicationLabel(packageInfo).toString()
                appName.contains(letter, ignoreCase = true)
            }.map { packageInfo ->
                async {
                    val appName = packageManager.getApplicationLabel(packageInfo).toString()
                    val packageName = packageInfo.packageName
                    AppDataModel(
                        packageName = packageName,
                        appName = appName,
                        blocked = bannedAppNames.contains(packageName)
                    )
                }
            }.awaitAll()

            onSuccess(matchingApps.toMutableList())
        }
    }

}