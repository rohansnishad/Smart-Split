package com.example.smartsplit

import android.content.Context
import androidx.room.Room

object AppDatabase {
    @Volatile
    private var INSTANCE: AppRoomDatabase? = null

    fun getInstance(context: Context): AppRoomDatabase {
        return INSTANCE ?: synchronized(this) {
            val inst = Room.databaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java,
                "smartsplit.db"
            )
                // .fallbackToDestructiveMigration() // optional for dev
                .build()
            INSTANCE = inst
            inst
        }
    }
}
