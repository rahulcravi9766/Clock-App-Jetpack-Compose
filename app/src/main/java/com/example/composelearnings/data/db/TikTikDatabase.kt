package com.example.composelearnings.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composelearnings.data.StopWatchModel

@Database(entities = [StopWatchModel::class], version = 1, exportSchema = true)
abstract class TikTikDatabase : RoomDatabase() {

    abstract fun tikTikDao(): TikTikDao

    companion object{
        @Volatile
        private var INSTANCE: TikTikDatabase? = null

        fun getDatabase(context: Context): TikTikDatabase {
            return INSTANCE ?: synchronized(this) {
                androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    TikTikDatabase::class.java,
                    "tik_tik_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}