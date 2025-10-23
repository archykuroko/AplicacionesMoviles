@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package mx.escom.cardjitsu.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import mx.escom.cardjitsu.juego.dominio.Elemento
import mx.escom.cardjitsu.juego.dominio.ResultadoRonda
import mx.escom.cardjitsu.presentacion.*
import mx.escom.cardjitsu.ui.tema.AzulAgua
import mx.escom.cardjitsu.ui.tema.CianNieve
import mx.escom.cardjitsu.ui.tema.RojoFuego
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.animation.core.Animatable
import kotlin.random.Random

@Composable
fun PantallaPartida(
    modo: ModoJuego,
    vm: JuegoViewModel = viewModel(
        key = "vm_${modo.name}",
        factory = JuegoViewModelFactory(modo)
    ),
    gestor: mx.escom.cardjitsu.juego.almacen.GestorPartidas,
    onVolverMenu: () -> Unit
) {
    val estado by vm.estado.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Card Jitsu ES", color = Color.White, style = MaterialTheme.typography.titleLarge) },
                actions = {
                    TextButton(onClick = onVolverMenu) { Text("Menú") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { inner ->

        Box(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                MarcadorHud(
                    j1 = estado.marcadorJ1,
                    j2 = estado.marcadorJ2,
                    objetivo = estado.objetivoVictorias
                )

                IndicadorTurno(estado.fase)

                // Panel Jugador 1
                PanelSeleccion(
                    titulo = if (modo == ModoJuego.VS_BOT) "Tú (J1)" else "Jugador 1",
                    habilitado = estado.fase == FaseUI.SELECCION_J1,
                    onElegir = { vm.procesar(IntentJuego.ElegirJ1(it)) },
                )

                // Tip animado para VS_BOT
                AnimatedVisibility(
                    visible = (modo == ModoJuego.VS_BOT && estado.fase == FaseUI.SELECCION_J1),
                    enter = fadeIn(animationSpec = tween(1200)),
                    exit = fadeOut(animationSpec = tween(600))
                ) {
                    Text(
                        text = "Tip: el bot aprende de tu última ronda 😉",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB0BEC5),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    )
                }

                // Panel Jugador 2 o aviso del bot
                if (modo == ModoJuego.LOCAL) {
                    PanelSeleccion(
                        titulo = "Jugador 2",
                        habilitado = estado.fase == FaseUI.SELECCION_J2,
                        onElegir = { vm.procesar(IntentJuego.ElegirJ2(it)) },
                    )
                } else {
                    if (estado.fase == FaseUI.LISTO_PARA_REVELAR) {
                        Text("El bot ya eligió. Puedes Revelar.", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Acciones (botones blancos)
                PanelAcciones(
                    estado = estado,
                    onRevelar = { vm.procesar(IntentJuego.Revelar) },
                    onNuevaRonda = { vm.procesar(IntentJuego.NuevaRonda) },
                    onReiniciar = { vm.procesar(IntentJuego.ReiniciarPartida) },
                    onGuardar = {
                        val snap = vm.crearSnapshot()
                        val id = gestor.guardar(snap)
                    }
                )

                // Banner + Resumen (con animación y sin NPE)
                val resultado = estado.resultadoRonda
                AnimatedVisibility(
                    visible = resultado != null,
                    enter = fadeIn(tween(250)) + scaleIn(tween(250), initialScale = 0.92f),
                    exit  = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.92f)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        resultado?.let {
                            BannerResultado(it, estado.hayGanadorPartida)
                        }
                        if (estado.seleccionJ1 != null && estado.seleccionJ2 != null) {
                            ResumenSelecciones(
                                selJ1 = estado.seleccionJ1!!,
                                selJ2 = estado.seleccionJ2!!
                            )
                        }
                    }
                }

                estado.mensajeError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }

            // 🎉 Confetti overlay cuando se gana la PARTIDA
            ConfettiOverlay(
                visible = estado.hayGanadorPartida,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun PanelSeleccion(
    titulo: String,
    habilitado: Boolean,
    onElegir: (Elemento) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonElemento("🔥", "Fuego", RojoFuego, habilitado) { onElegir(Elemento.FUEGO) }
                BotonElemento("💧", "Agua",  AzulAgua,  habilitado) { onElegir(Elemento.AGUA) }
                BotonElemento("❄️", "Nieve", CianNieve, habilitado) { onElegir(Elemento.NIEVE) }
            }
            if (!habilitado) Text("Esperando turno…", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun PanelAcciones(
    estado: EstadoUIJuego,
    onRevelar: () -> Unit,
    onNuevaRonda: () -> Unit,
    onReiniciar: () -> Unit,
    onGuardar: () -> Unit
) {
    val whiteButtons = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = Color.Black,
        disabledContainerColor = Color.White.copy(alpha = 0.30f),
        disabledContentColor = Color.Black.copy(alpha = 0.50f)
    )

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onRevelar,
            enabled = estado.puedeRevelar,
            colors = whiteButtons,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
            modifier = Modifier.weight(1f)
        ) { Text("Revelar") }

        Button(
            onClick = onNuevaRonda,
            enabled = estado.fase == FaseUI.MOSTRANDO_RESULTADO,
            colors = whiteButtons,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
            modifier = Modifier.weight(1f)
        ) { Text("Nueva ronda") }

        Button(
            onClick = onReiniciar,
            colors = whiteButtons,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
            modifier = Modifier.weight(1f)
        ) { Text("Reiniciar") }

        Button(
            onClick = onGuardar,
            colors = whiteButtons,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
            modifier = Modifier.weight(1f)
        ) { Text("Guardar") }


    }
}

@Composable
private fun MarcadorHud(j1: Int, j2: Int, objetivo: Int) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                "J1: $j1",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Objetivo: $objetivo",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "J2: $j2",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun IndicadorTurno(fase: FaseUI) {
    val texto = when (fase) {
        FaseUI.SELECCION_J1 -> "Turno: Jugador 1"
        FaseUI.SELECCION_J2 -> "Turno: Jugador 2"
        FaseUI.LISTO_PARA_REVELAR -> "Listo para revelar"
        FaseUI.MOSTRANDO_RESULTADO -> "Resultado mostrado"
        FaseUI.FIN_PARTIDA -> "¡Partida finalizada!"
    }
    AssistChip(
        onClick = {},
        label = { Text(texto) },
        enabled = false
    )
}

@Composable
private fun BannerResultado(resultado: ResultadoRonda, fin: Boolean) {
    val (texto, color) = when (resultado) {
        ResultadoRonda.GANA_J1 -> (if (fin) "¡Gana J1 la partida!" else "J1 gana la ronda") to RojoFuego
        ResultadoRonda.GANA_J2 -> (if (fin) "¡Gana J2 la partida!" else "J2 gana la ronda") to AzulAgua
        ResultadoRonda.EMPATE  -> "Empate" to CianNieve
    }

    // Haptic: distinto si es fin de partida
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(resultado, fin) {
        haptic.performHapticFeedback(
            if (fin) HapticFeedbackType.LongPress else HapticFeedbackType.TextHandleMove
        )
    }

    // Pop: animación de escala con spring
    val scale = remember { Animatable(0.94f) }
    LaunchedEffect(resultado, fin) {
        scale.snapTo(0.94f)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.55f, stiffness = 500f)
        )
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = color.copy(alpha = 0.14f))
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(texto, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun ResumenSelecciones(selJ1: Elemento, selJ2: Elemento) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Selecciones de la ronda", style = MaterialTheme.typography.titleMedium)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("J1: ${emojiDe(selJ1)} ${nombreDe(selJ1)}")
                Text("J2: ${emojiDe(selJ2)} ${nombreDe(selJ2)}")
            }
        }
    }
}

@Composable
private fun BotonElemento(
    emoji: String,
    etiqueta: String,
    colorBase: Color,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    val src = remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f, label = "scale"
    )

    Button(
        onClick = onClick,
        enabled = habilitado,
        interactionSource = src,
        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorBase.copy(alpha = 0.22f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = colorBase.copy(alpha = 0.10f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
        ),
        border = BorderStroke(1.2.dp, colorBase.copy(alpha = 0.55f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text("$emoji  $etiqueta", style = MaterialTheme.typography.titleSmall)
    }
}

// ---------- Confetti Overlay ----------
@Composable
private fun ConfettiOverlay(visible: Boolean, modifier: Modifier = Modifier) {
    if (!visible) return

    val colors = listOf(
        RojoFuego, AzulAgua, CianNieve,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )

    val particles = remember { mutableStateListOf<ConfettiParticle>() }

    LaunchedEffect(visible) {
        particles.clear()
        val start = System.currentTimeMillis()
        val rnd = Random(System.currentTimeMillis())

        while (System.currentTimeMillis() - start < 2200) {
            repeat(18) { particles += ConfettiParticle.random(rnd, colors) }
            repeat(2) { particles.forEach { it.step() } }
            particles.removeAll { it.life <= 0f }
            delay(16L)
        }
        particles.clear()
    }

    Canvas(modifier) {
        particles.forEach { p ->
            val end = Offset(p.x + p.dx * 6f, p.y + p.dy * 6f)
            drawLine(
                color = p.color.copy(alpha = p.life),
                start = Offset(p.x, p.y),
                end = end,
                strokeWidth = p.size,
                cap = StrokeCap.Round
            )
        }
    }
}

private data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var dx: Float,
    var dy: Float,
    var size: Float,
    var life: Float,
    val color: Color
) {
    fun step() {
        x += dx
        y += dy
        dy += 0.15f   // gravedad leve
        life -= 0.025f
    }

    companion object {
        fun random(rnd: Random, palette: List<Color>): ConfettiParticle {
            val w = 1080f
            val vx = (rnd.nextFloat() - 0.5f) * 8f
            val vy = rnd.nextFloat() * 4f + 1.5f
            return ConfettiParticle(
                x = rnd.nextFloat() * w,
                y = 0f,
                dx = vx,
                dy = vy,
                size = rnd.nextFloat() * 6f + 3f,
                life = 1f,
                color = palette[rnd.nextInt(palette.size)]
            )
        }
    }
}

// ---------- Utilidades ----------
private fun emojiDe(e: Elemento) = when (e) {
    Elemento.FUEGO -> "🔥"
    Elemento.AGUA  -> "💧"
    Elemento.NIEVE -> "❄️"
}

private fun nombreDe(e: Elemento) = when (e) {
    Elemento.FUEGO -> "Fuego"
    Elemento.AGUA  -> "Agua"
    Elemento.NIEVE -> "Nieve"
}

