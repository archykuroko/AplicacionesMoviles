package mx.escom.cardjitsu.ui.tema

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Fondo “dojo” sutil: del gris azulado al púrpura oscuro
private val FondoArriba = Color(0xFF121826)   // azul grisáceo
private val FondoAbajo  = Color(0xFF1B1434)   // morado profundo

val GradienteDojo = Brush.verticalGradient(
    colors = listOf(FondoArriba, FondoAbajo)
)

@Composable
fun FondoDojo(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GradienteDojo)
    ) { content() }
}
