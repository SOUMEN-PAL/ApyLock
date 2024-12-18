package com.example.apyblock.presentation.viewmodels

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.repository.AppDataRepository
import com.example.apyblock.utils.AllAppsFetchingState
import com.example.apyblock.utils.BannedAppFetchingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppDataRepository) : ViewModel() {


    private var _bannedAppsListState =
        MutableStateFlow<BannedAppFetchingState>(BannedAppFetchingState.Loading())
    val bannedAppListState = _bannedAppsListState.asStateFlow()

    private var _allAppsDataList =
        MutableStateFlow<AllAppsFetchingState>(AllAppsFetchingState.Loading())
    val allAppsDataList = _allAppsDataList.asStateFlow()

    val appBlockedStates = mutableMapOf<String, MutableState<Boolean>>()

    val selectedScreenIndex = mutableIntStateOf(0)
    val searchQuery = mutableStateOf("")
    val isSearching = mutableStateOf(false)

    fun getBannedApps() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _bannedAppsListState.value =
                    BannedAppFetchingState.Success(repository.getBannedApps())
            } catch (e: SQLiteException) { // More specific exception handling
                _bannedAppsListState.value =
                    BannedAppFetchingState.Error("Database error: ${e.message}")
            } catch (e: Exception) {
                _bannedAppsListState.value =
                    BannedAppFetchingState.Error("Error fetching banned list: ${e.message}")
            }
        }
    }


//    fun addAppData(appData: AppDataModel) {
//        viewModelScope.launch {
//            repository.addAppData(appData)
//            appBlockedStates[appData.packageName]?.value = true
//        }
//    }

    fun addAppData(appData: AppDataModel) {
        viewModelScope.launch {
            try {
                repository.addAppData(appData)
                appBlockedStates[appData.packageName]?.value = true
            } catch (e: Exception) {
                Log.e("dataError" , "To many request")
            }
        }
    }

    fun updateTime(appData: AppDataModel) {
        viewModelScope.launch {
            repository.updateTime(appData)
        }
    }

    fun getAllAppInSystem(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllAppInSystem(
                context = context,
                onSuccess = { allAppModelList ->
                    if (allAppModelList.size > 0) {
                        _allAppsDataList.value = AllAppsFetchingState.Success(allAppModelList)
                    } else {
                        _allAppsDataList.value = AllAppsFetchingState.Error("No Apps Installed")
                    }

                    allAppModelList.forEach { appData ->
                        appBlockedStates[appData.packageName] = mutableStateOf(appData.blocked)
                    }
                }
            )
        }
    }


    var job: Job? = null
    fun getAppsContainingLetters(context: Context) {

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            repository.getSearchedApps(
                context = context,
                letter = searchQuery.value
            ) { matchedAppList ->
                if (matchedAppList.size > 0) {
                    _allAppsDataList.value = AllAppsFetchingState.Searching(matchedAppList)
                } else {
                    _allAppsDataList.value = AllAppsFetchingState.Error(e = "No App Found")
                }
            }
        }
    }

    fun resetSearchedList() {
        _allAppsDataList.value = AllAppsFetchingState.Loading()
    }

    fun getAPPIcon(context: Context, packageName: String): Drawable? {
        return try {
            val packageManager = context.packageManager
            val applicationInfo =
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            applicationInfo.loadIcon(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun deleteAppFromBannedList(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromBannedAppsList(packageName)
            appBlockedStates[packageName]?.value = false
        }
    }

}