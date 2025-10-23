package mx.escom.cardjitsu.juego.dominio


//Estado puro de la partida (sin UI)
//La UI mandará intents y el ViewModel transformará este estado

data class EstadoPartida(
    val marcadorJ1: Int = 0,
    val marcadorJ2: Int = 0,
    val seleccionJ1: Elemento? = null,
    val seleccionJ2: Elemento? = null,
    val fase: Fase = Fase.SELECCION_J1,
    val finDePartida: Boolean = false
) {
    enum class Fase { SELECCION_J1, SELECCION_J2, REVELADO }
}
