package ste.archykuroko.aguapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.compose.material.MaterialTheme
import ste.archykuroko.aguapp.presentation.AguaAppScreen

import androidx.work.*
import ste.archykuroko.aguapp.data.ResetWorker
import ste.archykuroko.aguapp.data.HydrateReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Reset diario
        val resetWork = PeriodicWorkRequestBuilder<ResetWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "resetWaterCounter",
            ExistingPeriodicWorkPolicy.REPLACE,
            resetWork
        )

        // Notificación cada hora
        val reminderWork = PeriodicWorkRequestBuilder<HydrateReminderWorker>(
            1, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "hydrationReminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            reminderWork
        )

        // UI
        setContent {
            MaterialTheme {
                AguaAppScreen()
            }
        }
    }
}
