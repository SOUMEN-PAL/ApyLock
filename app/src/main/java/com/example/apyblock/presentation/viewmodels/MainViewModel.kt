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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: AppDataRepository) : ViewModel() {


    private var _bannedAppsListState =
        MutableStateFlow<BannedAppFetchingState>(BannedAppFetchingState.Loading())
    val bannedAppListState = _bannedAppsListState.asStateFlow()

    private var _allAppsDataList =
        MutableStateFlow<AllAppsFetchingState>(AllAppsFetchingState.Loading())
    val allAppsDataList = _allAppsDataList.asStateFlow()


    val selectedScreenIndex = mutableIntStateOf(0)
    val searchQuery = mutableStateOf("")
    val isSearching = mutableStateOf(false)

    fun getBannedApps() {
        viewModelScope.launch {
            try {
                _bannedAppsListState.value =
                    BannedAppFetchingState.Success(repository.getBannedApps())
            } catch (e: SQLiteException) {
                _bannedAppsListState.value =
                    BannedAppFetchingState.Error("Database error: ${e.message}")
            } catch (e: Exception) {
                _bannedAppsListState.value =
                    BannedAppFetchingState.Error("Error fetching banned list: ${e.message}")
            }
        }
    }


    fun addAppData(appData: AppDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAppData(appData)
        }
    }

    fun updateTime(appData: AppDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTime(appData)
        }
    }

    private var cachedAppList: List<AppDataModel> = emptyList()

    fun getAllAppInSystem(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (cachedAppList.isEmpty()) { // Check if cache is empty
                repository.getAllAppInSystem(context) { allAppModelList ->
                    cachedAppList = allAppModelList // Update cache
                    viewModelScope.launch(Dispatchers.Main) { // Update UI on the main thread
                        if (cachedAppList.isNotEmpty()) {
                            _allAppsDataList.value = AllAppsFetchingState.Success(cachedAppList as MutableList<AppDataModel>)
                        } else {
                            _allAppsDataList.value = AllAppsFetchingState.Error("No Apps Installed")
                        }
                    }
                }
            } else {
                // Use cached data
                withContext(Dispatchers.Main) {
                    _allAppsDataList.value = AllAppsFetchingState.Success(cachedAppList as MutableList<AppDataModel>)
                }
            }
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
                if (matchedAppList.isNotEmpty()) {
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

    suspend fun getAPPIcon(context: Context, packageName: String): Drawable? {
        return withContext(Dispatchers.IO) {
            try {
                val packageManager = context.packageManager
                val applicationInfo =
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                applicationInfo.loadIcon(packageManager)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }

    fun deleteAppFromBannedList(packageName: String) {
        viewModelScope.launch {
            repository.removeFromBannedAppsList(packageName)
        }
    }

}