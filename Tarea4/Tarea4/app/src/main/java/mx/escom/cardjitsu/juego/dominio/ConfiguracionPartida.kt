package mx.escom.cardjitsu.juego.dominio

// Parámetros de la partida (ajustables en Ajustes después)

data class ConfiguracionPartida(
    val victoriasObjetivo: Int = 3   // “al mejor de 5”
)
