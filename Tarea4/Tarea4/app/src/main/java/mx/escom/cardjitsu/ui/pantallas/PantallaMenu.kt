package mx.escom.cardjitsu.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaMenu(
    onJugarLocal: () -> Unit,
    onJugarVsBot: () -> Unit,
    onJugarBt: () -> Unit,
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f)) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Card Jitsu ES", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onJugarLocal,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Jugador vs Jugador (local)") }

            Button(
                onClick = onJugarVsBot,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Jugador vs Bot") }

            Button(
                onClick = onJugarBt,
                modifier = Modifier.fillMaxWidth()
            ) { Text("PvP por Bluetooth") }
        }
    }
}
