package com.example.actividad1.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Paleta Guinda (IPN)
val GuindaPrimary = Color(0xFF6B2E5F)
val GuindaOnPrimary = Color(0xFFFFFFFF)
val GuindaBackground = Color(0xFFFDF7FB)
val GuindaOnBackground = Color(0xFF2B1B29)

// Paleta Azul (ESCOM)
val AzulPrimary = Color(0xFF003D6D)
val AzulOnPrimary = Color(0xFFFFFFFF)
val AzulBackground = Color(0xFFF5F8FD)
val AzulOnBackground = Color(0xFF1A2027)

// Esquemas de color para ambos temas
val LightGuindaScheme = lightColorScheme(
    primary = GuindaPrimary,
    onPrimary = GuindaOnPrimary,
    background = GuindaBackground,
    onBackground = GuindaOnBackground
)

val DarkGuindaScheme = darkColorScheme(
    primary = GuindaPrimary,
    onPrimary = GuindaOnPrimary,
    background = Color(0xFF1B0E1A),
    onBackground = Color(0xFFF0D9EF)
)

val LightAzulScheme = lightColorScheme(
    primary = AzulPrimary,
    onPrimary = AzulOnPrimary,
    background = AzulBackground,
    onBackground = AzulOnBackground
)

val DarkAzulScheme = darkColorScheme(
    primary = AzulPrimary,
    onPrimary = AzulOnPrimary,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFE2E8F0)
)
