package ste.archykuroko.examenfinal.tracking

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import ste.archykuroko.examenfinal.data.DbProvider
import ste.archykuroko.examenfinal.data.LocationEntity

class TrackingService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var fused: FusedLocationProviderClient
    private lateinit var callback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        fused = LocationServices.getFusedLocationProviderClient(this)

        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                val dao = DbProvider.get(applicationContext).locationDao()

                scope.launch {
                    dao.insert(
                        LocationEntity(
                            lat = loc.latitude,
                            lon = loc.longitude,
                            accuracyMeters = loc.accuracy,
                            timestampMillis = loc.time
                        )
                    )
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intervalMs = intent?.getLongExtra(EXTRA_INTERVAL_MS, 10_000L) ?: 10_000L
        val notifEnabled = intent?.getBooleanExtra(EXTRA_NOTIF_ENABLED, true) ?: true

        if (notifEnabled) {
            startForeground(NOTIF_ID, buildNotification("Rastreo activo • ${intervalMs / 1000}s"))
        }

        startLocationUpdates(intervalMs)
        return START_STICKY
    }

    private fun startLocationUpdates(intervalMs: Long) {
        val req = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalMs)
            .setMinUpdateIntervalMillis(intervalMs)
            .setWaitForAccurateLocation(false)
            .build()

        // Asumimos permisos ya otorgados (los pide la UI antes de arrancar)
        fused.requestLocationUpdates(req, callback, mainLooper)
    }

    override fun onDestroy() {
        fused.removeLocationUpdates(callback)
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(text: String): Notification {
        val channelId = "tracking_channel"
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (nm.getNotificationChannel(channelId) == null) {
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Rastreo", NotificationManager.IMPORTANCE_LOW)
            )
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Ubicación en segundo plano")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val NOTIF_ID = 1001
        const val EXTRA_INTERVAL_MS = "intervalMs"
        const val EXTRA_NOTIF_ENABLED = "notifEnabled"
    }
}
