package com.example.internaltool.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Report::class], version = 1)
abstract class ReportDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile private var INSTANCE: ReportDatabase? = null

        fun getDatabase(context: Context): ReportDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReportDatabase::class.java,
                    "report_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
