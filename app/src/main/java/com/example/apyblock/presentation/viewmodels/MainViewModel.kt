package com.example.apyblock.presentation.viewmodels

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.domain.repository.AppDataRepository
import com.example.apyblock.utils.AllAppsFetchingState
import com.example.apyblock.utils.BannedAppFetchingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppDataRepository) : ViewModel() {


    private var _bannedAppsListState = MutableStateFlow<BannedAppFetchingState>(BannedAppFetchingState.Loading())
    val bannedAppListState = _bannedAppsListState.asStateFlow()

    private var _allAppsDataList = MutableStateFlow<AllAppsFetchingState>(AllAppsFetchingState.Loading())
    val allAppsDataList = _allAppsDataList.asStateFlow()


    val selectedScreenIndex = mutableIntStateOf(0)
    val searchQuery = mutableStateOf("")
    val isSearching = mutableStateOf(false)

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

        repository.getAllAppInSystem(context = context , onSuccess = {allAppModelList->
            if(allAppModelList.size>0){
                _allAppsDataList.value = AllAppsFetchingState.Success(allAppModelList)
            }else{
                _allAppsDataList.value = AllAppsFetchingState.Error("No Apps Installed")
            }
        })
    }

    fun getAppsContainingLetters(context: Context) {
        var job: Job? = null
        job?.cancel()
        job = viewModelScope.launch {
            delay(500L)
            repository.getSearchedApps(context = context, letter = searchQuery.value) { matchedAppList ->
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

    fun getAPPIcon(context: Context , packageName : String) : Drawable?{
        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            applicationInfo.loadIcon(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

}