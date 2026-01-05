package ste.archykuroko.examenfinal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import org.osmdroid.util.GeoPoint
import ste.archykuroko.examenfinal.theme.ExamenFinalTheme
import ste.archykuroko.examenfinal.tracking.TrackingInterval
import ste.archykuroko.examenfinal.tracking.TrackingViewModel
import ste.archykuroko.examenfinal.ui.OsmMap
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExamenFinalTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val nav = rememberNavController()
                    NavHost(navController = nav, startDestination = "main") {
                        composable("main") { MainScreen(onGoHistory = { nav.navigate("history") }) }
                        composable("history") { HistoryScreen(onBack = { nav.popBackStack() }) }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    onGoHistory: () -> Unit,
    vm: TrackingViewModel = viewModel()
) {
    val ui by vm.ui.collectAsState()
    val ctx = LocalContext.current

    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { res: Map<String, Boolean> ->
        val granted = (res[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (res[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        vm.setPermission(granted)
    }

    // Revisa permisos al entrar
    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
        vm.setPermission(fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED)
    }

    fun requestNeededPermissions() {
        val perms = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= 33) {
            perms.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permLauncher.launch(perms.toTypedArray())
    }

    Box(modifier = Modifier.fillMaxSize()) {

        val current = remember(ui.lat, ui.lon) {
            if (ui.lat != null && ui.lon != null) GeoPoint(ui.lat!!, ui.lon!!) else null
        }

        OsmMap(
            modifier = Modifier.fillMaxSize(),
            routePoints = ui.route,
            currentPoint = current
        )

        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(12.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusText = if (ui.isTracking) "ACTIVO" else "INACTIVO"
                    val statusColor = if (ui.isTracking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    Text("Rastreo: ", style = MaterialTheme.typography.titleMedium)
                    Text(statusText, color = statusColor, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    Text("Puntos: ${ui.route.size}")
                }

                Spacer(Modifier.height(6.dp))
                Text("Lat: ${ui.lat?.toString() ?: "-"}")
                Text("Lon: ${ui.lon?.toString() ?: "-"}")
                Text("Precisión: ${ui.accuracyMeters?.let { "${it} m" } ?: "-"}")

                ui.lastError?.let {
                    Spacer(Modifier.height(6.dp))
                    Text("⚠ $it", color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(10.dp))

                // Switch notificación ON/OFF
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = ui.notifEnabled,
                        onCheckedChange = vm::setNotifEnabled
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Notificación (ON/OFF)")
                }

                Spacer(Modifier.height(10.dp))

                IntervalSelector(selected = ui.interval, onSelect = vm::setInterval)

                Spacer(Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    Button(
                        onClick = {
                            if (!ui.hasLocationPermission) {
                                requestNeededPermissions()
                            } else {
                                vm.startBackgroundService()
                            }
                        },
                        enabled = !ui.isTracking
                    ) { Text("Iniciar") }

                    OutlinedButton(
                        onClick = vm::stopBackgroundService,
                        enabled = ui.isTracking
                    ) { Text("Detener") }

                    OutlinedButton(onClick = onGoHistory) { Text("Historial") }

                    OutlinedButton(onClick = vm::clearHistory) { Text("Limpiar") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntervalSelector(
    selected: TrackingInterval,
    onSelect: (TrackingInterval) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Intervalo:")
        Spacer(Modifier.width(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selected.label,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                label = { Text("Actualización") }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                TrackingInterval.values().forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt.label) },
                        onClick = {
                            onSelect(opt)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryScreen(
    onBack: () -> Unit,
    vm: TrackingViewModel = viewModel()
) {
    val ui by vm.ui.collectAsState()
    val df = remember { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack) { Text("Volver") }
            Spacer(Modifier.width(10.dp))
            Text("Historial", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(10.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(ui.historyDesc) { it ->
                val dt = df.format(Date(it.timestampMillis))
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(dt, style = MaterialTheme.typography.titleMedium)
                        Text("Lat: ${it.lat}")
                        Text("Lon: ${it.lon}")
                        Text("Precisión: ${it.accuracyMeters} m")
                    }
                }
            }
        }
    }
}
