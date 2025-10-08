package com.example.actividad1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import com.example.actividad1.data.datastore.PrefsRepository

/**
 * Tema reactivo: escucha DataStore y recompone al cambiar entre "guinda" y "azul".
 */
@Composable
fun AppTheme(
    prefs: PrefsRepository,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val initial = runBlocking { prefs.themeFlow.first() }
    val theme = prefs.themeFlow.collectAsState(initial = initial).value

    val colors = when (theme.lowercase()) {
        "azul" -> if (darkTheme) DarkAzulScheme else LightAzulScheme
        else   -> if (darkTheme) DarkGuindaScheme else LightGuindaScheme
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}


@Composable
fun AppTheme(
    themePref: String,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (themePref.lowercase()) {
        "azul" -> if (darkTheme) DarkAzulScheme else LightAzulScheme
        else   -> if (darkTheme) DarkGuindaScheme else LightGuindaScheme
    }
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
