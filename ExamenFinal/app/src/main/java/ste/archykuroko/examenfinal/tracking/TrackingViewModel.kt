package ste.archykuroko.examenfinal.tracking

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import ste.archykuroko.examenfinal.data.DbProvider
import ste.archykuroko.examenfinal.data.LocationEntity


data class TrackingUiState(
    val hasLocationPermission: Boolean = false,
    val isTracking: Boolean = false,
    val interval: TrackingInterval = TrackingInterval.S10,
    val notifEnabled: Boolean = true,

    val lat: Double? = null,
    val lon: Double? = null,
    val accuracyMeters: Float? = null,

    val route: List<GeoPoint> = emptyList(),
    val historyDesc: List<LocationEntity> = emptyList(),

    val lastError: String? = null
)

class TrackingViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = DbProvider.get(app).locationDao()

    private val _ui = MutableStateFlow(TrackingUiState())
    val ui: StateFlow<TrackingUiState> = _ui

    init {
        // Observa ruta ASC para polyline
        viewModelScope.launch {
            dao.observeAllAsc()
                .collect { list ->
                    _ui.update { st ->
                        st.copy(route = list.map { GeoPoint(it.lat, it.lon) })
                    }
                }
        }

        // Observa historial DESC para pantalla historial
        viewModelScope.launch {
            dao.observeAllDesc().collect { list ->
                val last = list.firstOrNull() // DESC => el más reciente
                _ui.update { st ->
                    st.copy(
                        historyDesc = list,
                        lat = last?.lat,
                        lon = last?.lon,
                        accuracyMeters = last?.accuracyMeters
                    )
                }
            }
        }

        // Estado inicial permisos (por si quieres consultarlo aquí)
        refreshPermissionState()
    }

    fun refreshPermissionState() {
        val ctx = getApplication<Application>()
        val fine = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val granted = fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
        _ui.update { it.copy(hasLocationPermission = granted) }
    }

    fun setPermission(granted: Boolean) {
        _ui.update { it.copy(hasLocationPermission = granted) }
        if (!granted) stopBackgroundService()
    }

    fun setInterval(interval: TrackingInterval) {
        _ui.update { it.copy(interval = interval) }
        // Si está corriendo en background, reiniciamos con el nuevo intervalo
        if (_ui.value.isTracking) {
            stopBackgroundService()
            startBackgroundService()
        }
    }

    fun setNotifEnabled(enabled: Boolean) {
        _ui.update { it.copy(notifEnabled = enabled) }
        // Si está corriendo y cambias el toggle, reinicia para aplicar
        if (_ui.value.isTracking) {
            stopBackgroundService()
            startBackgroundService()
        }
    }

    fun startBackgroundService() {
        if (!_ui.value.hasLocationPermission) {
            _ui.update { it.copy(lastError = "Sin permisos de ubicación.") }
            return
        }

        val ctx = getApplication<Application>()
        val i = Intent(ctx, TrackingService::class.java).apply {
            putExtra(TrackingService.EXTRA_INTERVAL_MS, _ui.value.interval.millis)
            putExtra(TrackingService.EXTRA_NOTIF_ENABLED, _ui.value.notifEnabled)
        }
        ContextCompat.startForegroundService(ctx, i)

        _ui.update { it.copy(isTracking = true, lastError = null) }
    }

    fun stopBackgroundService() {
        val ctx = getApplication<Application>()
        ctx.stopService(Intent(ctx, TrackingService::class.java))
        _ui.update { it.copy(isTracking = false) }
    }

    fun clearHistory() {
        viewModelScope.launch { dao.clear() }
    }
}
