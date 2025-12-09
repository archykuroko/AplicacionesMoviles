package ste.archykuroko.aguapp.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ste.archykuroko.aguapp.R

class HydrateReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val channelId = "hydration_reminders"

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Recordatorios de Hidratación",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("¡Toma agua! 💧")
            .setContentText("Mantente hidratado, socio 😎")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // temporal 👍
            .build()

        notificationManager.notify(1001, notification)

        return Result.success()
    }
}
