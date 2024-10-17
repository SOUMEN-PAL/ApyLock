package com.example.apyblock.utils

import com.example.apyblock.domain.models.AppDataModel

sealed class BannedAppFetchingState {
    class Success(val appList : List<AppDataModel>) : BannedAppFetchingState()
    class Loading() : BannedAppFetchingState()
    class Error(val e : String) : BannedAppFetchingState()
}