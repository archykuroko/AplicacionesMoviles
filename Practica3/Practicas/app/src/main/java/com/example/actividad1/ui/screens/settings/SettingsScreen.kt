package com.example.actividad1.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.actividad1.data.datastore.PrefsRepository
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val ctx = LocalContext.current
    val prefs = remember { PrefsRepository(ctx) }
    val scope = rememberCoroutineScope()

    val current by prefs.themeFlow.collectAsState(initial = "guinda")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Apariencia", style = MaterialTheme.typography.headlineSmall)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Radio(
                selected = current.equals("guinda", ignoreCase = true),
                onClick = { scope.launch { prefs.setTheme("guinda") } },
                label = "Guinda (IPN)"
            )
            Radio(
                selected = current.equals("azul", ignoreCase = true),
                onClick = { scope.launch { prefs.setTheme("azul") } },
                label = "Azul (ESCOM)"
            )
        }

        Divider()

        // Botones directos (por si prefieres acciones explícitas)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { scope.launch { prefs.setTheme("guinda") } }) { Text("Usar Guinda") }
            Button(onClick = { scope.launch { prefs.setTheme("azul") } }) { Text("Usar Azul") }
        }
    }
}

@Composable
private fun Radio(selected: Boolean, onClick: () -> Unit, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors()
        )
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
