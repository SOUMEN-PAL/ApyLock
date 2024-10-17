package com.example.apyblock.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apyblock.domain.models.AppDataModel

@Database(entities = [AppDataModel::class] , version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDataDAO(): AppDAO

    companion object{
        @Volatile
        private var INSTANCE:AppDatabase? = null

        fun getDatabase(context : Context): AppDatabase?{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "blocked_apps"
                    ).build()
                }
            }
            return INSTANCE!!
        }

    }
}