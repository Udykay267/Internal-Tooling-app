package com.example.internaltool.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT * FROM reports")
    suspend fun getAllReportsOnce(): List<Report>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: Report)
}
