package com.example.midterm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.midterm.data.dao.EventDao
import com.example.midterm.entity.Event


@Database(entities = [Event::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "event_database"
                ).fallbackToDestructiveMigration() // 如果沒有找到遷移，則重建資料庫
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
