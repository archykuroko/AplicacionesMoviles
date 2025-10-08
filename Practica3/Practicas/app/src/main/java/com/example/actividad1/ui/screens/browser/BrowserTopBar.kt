package com.example.actividad1.ui.screens.browser

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserTopBar(
    onPickRoot: () -> Unit,
    onSettings: () -> Unit,
    query: String,
    onQuery: (String) -> Unit
) {
    var text by remember { mutableStateOf(query) }

    TopAppBar(
        title = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it; onQuery(it) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Buscar...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(onClick = onPickRoot) {
                Icon(Icons.Default.FolderOpen, contentDescription = "Seleccionar carpeta")
            }
            IconButton(onClick = onSettings) {
                Icon(Icons.Default.Settings, contentDescription = "Ajustes")
            }
        }
    )
}
