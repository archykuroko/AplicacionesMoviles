package mx.escom.cardjitsu.presentacion

import mx.escom.cardjitsu.juego.dominio.Elemento

// Acciones que puede emitir la UI hacia el ViewModel
sealed interface IntentJuego {
    data class ElegirJ1(val elemento: Elemento) : IntentJuego
    data class ElegirJ2(val elemento: Elemento) : IntentJuego
    data object Revelar : IntentJuego
    data object NuevaRonda : IntentJuego
    data object ReiniciarPartida : IntentJuego
}
