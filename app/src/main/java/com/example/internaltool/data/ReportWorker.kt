package com.example.internaltool.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ReportWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val db = ReportDatabase.getDatabase(context)
            val reports = db.reportDao().getAllReportsOnce()
            val file = File(context.filesDir, "report_backup.txt")
            FileOutputStream(file).bufferedWriter().use { writer ->
                reports.forEach {
                    writer.write("Name: ${it.name}, Report: ${it.message}\n")
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
