package com.example.apyblock.presentation.viewmodels

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.repository.AppDataRepository
import com.example.apyblock.utils.AllAppsFetchingState
import com.example.apyblock.utils.BannedAppFetchingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppDataRepository) : ViewModel() {


    private var _bannedAppsListState = MutableStateFlow<BannedAppFetchingState>(BannedAppFetchingState.Loading())
    val bannedAppListState = _bannedAppsListState.asStateFlow()

    private var _allAppsDataList = MutableStateFlow<AllAppsFetchingState>(AllAppsFetchingState.Loading())
    val allAppsDataList = _allAppsDataList.asStateFlow()


    fun getBannedApps(){
        viewModelScope.launch {
            try {
                _bannedAppsListState.value = BannedAppFetchingState.Success(repository.getBannedApps())
            } catch (e: SQLiteException) { // More specific exception handling
                _bannedAppsListState.value = BannedAppFetchingState.Error("Database error: ${e.message}")
            } catch (e: Exception) {
                _bannedAppsListState.value = BannedAppFetchingState.Error("Error fetching banned list: ${e.message}")
            }
        }
    }


    fun addAppData(appData : AppDataModel){
        viewModelScope.launch {
            repository.addAppData(appData)
        }
    }

    fun updateTime(appData: AppDataModel){
        viewModelScope.launch {
            repository.updateTime(appData)
        }
    }

    fun getAllAppInSystem(context: Context){
        val packageManager = context.packageManager
        val allAppsData = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val allAppModelList: MutableList<AppDataModel> = mutableListOf()
        for(appInfo in allAppsData){
            allAppModelList.add(
                AppDataModel(
                    packageName = appInfo.packageName,
                    appName = appInfo.loadLabel(packageManager).toString(),
                    blocked = false,
                    startTime = null,
                    endTime = null
                )
            )
        }
        if(allAppModelList.size>0){
            _allAppsDataList.value = AllAppsFetchingState.Success(allAppModelList)
        }else{
            _allAppsDataList.value = AllAppsFetchingState.Error("No Apps Installed")
        }
    }

}