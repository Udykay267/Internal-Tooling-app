package com.example.internaltool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.internaltool.data.Report
import com.example.internaltool.data.ReportDatabase
import com.example.internaltool.data.ReportWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = ReportDatabase.getDatabase(this)
        val dao = db.reportDao()

        val request = PeriodicWorkRequestBuilder<ReportWorker>(1, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "backup_reports",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        setContent {
            ReportFormScreen { name, message ->
                lifecycleScope.launch {
                    dao.insert(Report(name = name, message = message))
                }
            }
        }
    }
}

@Composable
fun ReportFormScreen(onSubmit: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var report by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Daily Report", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Name") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = report,
            onValueChange = { report = it },
            label = { Text("Today's Report") },
            modifier = Modifier.height(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (name.isNotBlank() && report.isNotBlank()) {
                onSubmit(name, report)
            }
        }) {
            Text("Submit")
        }
    }
}
