package com.example.apyblock

import android.app.Application
import com.example.apyblock.data.db.AppDatabase
import com.example.apyblock.domain.repository.AppDataRepository

class ApyLockApplication : Application() {
    lateinit var appRepository: AppDataRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize(){
        val database = AppDatabase.getDatabase(applicationContext)
        if(database != null){
            appRepository = AppDataRepository(database)
        }
    }
}