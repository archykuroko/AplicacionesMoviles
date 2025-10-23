package mx.escom.cardjitsu.presentacion

import mx.escom.cardjitsu.juego.dominio.Elemento
import mx.escom.cardjitsu.juego.dominio.ResultadoRonda

// Fases que entiende la UI
enum class FaseUI {
    SELECCION_J1,        // espera la elección de J1
    SELECCION_J2,        // espera la elección de J2
    LISTO_PARA_REVELAR,  // ambos eligieron; falta pulsar "Revelar"
    MOSTRANDO_RESULTADO, // ya se reveló; muestra banner con resultado
    FIN_PARTIDA          // alguien alcanzó el objetivo de victorias
}


// Estado completo para la UI

data class EstadoUIJuego(
    val marcadorJ1: Int = 0,
    val marcadorJ2: Int = 0,
    val objetivoVictorias: Int = 3,

    val seleccionJ1: Elemento? = null,
    val seleccionJ2: Elemento? = null,

    val fase: FaseUI = FaseUI.SELECCION_J1,
    val resultadoRonda: ResultadoRonda? = null,   // nulo si aún no se revela
    val mensajeError: String? = null              // para un feedback rápido
) {
    val puedeRevelar: Boolean get() = seleccionJ1 != null && seleccionJ2 != null && fase == FaseUI.LISTO_PARA_REVELAR
    val hayGanadorPartida: Boolean get() = fase == FaseUI.FIN_PARTIDA
}
