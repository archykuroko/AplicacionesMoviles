package mx.escom.cardjitsu.ui.pantallas

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import mx.escom.cardjitsu.red.bluetooth.BluetoothServicio
import mx.escom.cardjitsu.red.bluetooth.EstadoBt

@Composable
fun PantallaBluetooth(
    servicio: BluetoothServicio,
    vincularVm: () -> Unit,
    onConectado: () -> Unit,
    onVolver: () -> Unit
) {
    val ctx = LocalContext.current
    // Vinculamos el VM a este servicio
    LaunchedEffect(Unit) { vincularVm() }

    // Permisos según versión
    val permisos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
    else
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    fun tienePermisos(): Boolean =
        permisos.all { ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED }

    val pedirPermisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* No hacemos nada aquí; el usuario puede volver a tocar el botón */ }

    val estado by servicio.estado.collectAsState()
    val scope = rememberCoroutineScope()

    // Adapter para listar emparejados (lo tomamos del sistema)
    val btAdapter by remember {
        mutableStateOf(
            (ctx.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        )
    }

    // Para seleccionar a cuál emparejado conectarse
    var dispositivos by remember { mutableStateOf<List<BluetoothDevice>>(emptyList()) }
    var errorUi by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("PvP por Bluetooth", style = MaterialTheme.typography.titleLarge)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                if (!tienePermisos()) {
                    pedirPermisos.launch(permisos)
                    return@Button
                }
                servicio.iniciarHost()
            }) { Text("Anfitrión") }

            Button(onClick = {
                if (!tienePermisos()) {
                    pedirPermisos.launch(permisos)
                    return@Button
                }
                // Lista rápida de emparejados
                try {
                    dispositivos = btAdapter.bondedDevices?.toList().orEmpty()
                    errorUi = if (dispositivos.isEmpty())
                        "No hay dispositivos emparejados. Empareja en Ajustes del sistema."
                    else null
                } catch (se: SecurityException) {
                    errorUi = "Permisos de Bluetooth faltantes."
                }
            }) { Text("Unirse") }
        }

        // Si hay emparejados, mostramos los primeros 5 como botones
        dispositivos.take(5).forEach { dev ->
            OutlinedButton(
                onClick = {
                    if (!tienePermisos()) {
                        pedirPermisos.launch(permisos); return@OutlinedButton
                    }
                    try {
                        servicio.conectar(dev)
                    } catch (se: SecurityException) {
                        errorUi = "Permisos de Bluetooth faltantes."
                    } catch (e: Exception) {
                        errorUi = e.message
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(dev.name ?: dev.address ?: "Dispositivo")
            }
        }

        Text("Estado: ${estado::class.simpleName}", style = MaterialTheme.typography.bodyMedium)

        if (errorUi != null) {
            Text(errorUi!!, color = MaterialTheme.colorScheme.error)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (estado is EstadoBt.Conectado) {
                Button(onClick = onConectado) { Text("Ir a la partida") }
            }
            OutlinedButton(onClick = onVolver) { Text("Volver") }
        }
    }
}
