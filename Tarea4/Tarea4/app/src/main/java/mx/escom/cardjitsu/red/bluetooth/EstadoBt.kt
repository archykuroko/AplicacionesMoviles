package mx.escom.cardjitsu.red.bluetooth
sealed class EstadoBt {
    object Listo : EstadoBt()
    object Esperando : EstadoBt()
    data class Conectando(val nombre: String) : EstadoBt()
    data class Conectado(val nombre: String) : EstadoBt()
    data class Error(val mensaje: String) : EstadoBt()
}
