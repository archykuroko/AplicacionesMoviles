package mx.escom.cardjitsu.ui.tema

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mx.escom.cardjitsu.R

// Colores de elementos
val RojoFuego = Color(0xFFE53935)
val AzulAgua  = Color(0xFF1E88E5)
val CianNieve = Color(0xFF00ACC1)


private val Dark = darkColorScheme(
    surface = Color(0xFF11131A),
    onSurface = Color(0xFFEAEAF0),
    surfaceVariant = Color(0xFF1C2030),
    onSurfaceVariant = Color(0xFFCDD1DB),
    primary = RojoFuego,
    secondary = AzulAgua,
    tertiary = CianNieve
)

private val Light = lightColorScheme(
    surface = Color(0xFFF6F7FB),
    onSurface = Color(0xFF151820),
    surfaceVariant = Color(0xFFE9ECF3),
    onSurfaceVariant = Color(0xFF2A2F3A),
    primary = RojoFuego,
    secondary = AzulAgua,
    tertiary = CianNieve
)

// Fuente personalizada SOLO para títulos
val FuenteDojo = FontFamily(Font(R.font.japon, weight = FontWeight.Normal))

@Composable
fun TemaCardJitsu(content: @Composable () -> Unit) {
    val base = Typography()
    val tipografia = base.copy(
        headlineLarge  = TextStyle(fontFamily = FuenteDojo, fontSize = 32.sp),
        headlineMedium = TextStyle(fontFamily = FuenteDojo, fontSize = 28.sp),
        headlineSmall  = TextStyle(fontFamily = FuenteDojo, fontSize = 24.sp),
        titleLarge     = TextStyle(fontFamily = FuenteDojo, fontSize = 20.sp),
        titleMedium    = TextStyle(fontFamily = FuenteDojo, fontSize = 18.sp),
        titleSmall     = TextStyle(fontFamily = FuenteDojo, fontSize = 16.sp),
    )
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) Dark else Light,
        typography = tipografia,
        content = content
    )
}
