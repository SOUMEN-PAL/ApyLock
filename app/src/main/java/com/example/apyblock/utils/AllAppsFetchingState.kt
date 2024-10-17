package com.example.apyblock.utils

import com.example.apyblock.domain.models.AppDataModel

sealed class AllAppsFetchingState {

        class Success(val appList : MutableList<AppDataModel>) : AllAppsFetchingState()
        class Loading() : AllAppsFetchingState()
        class Error(val e : String) : AllAppsFetchingState()

}