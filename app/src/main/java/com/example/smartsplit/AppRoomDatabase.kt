package com.example.smartsplit

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartsplit.data.ExpenseEntity
import com.example.smartsplit.data.ExpenseSplitEntity
import com.example.smartsplit.data.GroupEntity
import com.example.smartsplit.data.MemberEntity
import com.example.smartsplit.data.AppDao

// put every Room entity class used in your app inside entities = [...]


@Database(
    entities = [
        GroupEntity::class,
        MemberEntity::class,
        ExpenseEntity::class,
        ExpenseSplitEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
