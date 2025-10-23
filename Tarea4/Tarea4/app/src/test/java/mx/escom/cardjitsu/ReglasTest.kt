package mx.escom.cardjitsu.juego.dominio

import org.junit.Assert.assertEquals
import org.junit.Test

class ReglasTest {
    @Test fun aguaVenceFuego() {
        assertEquals(ResultadoRonda.GANA_J1, Reglas.enfrentar(Elemento.AGUA, Elemento.FUEGO))
    }
    @Test fun fuegoVenceNieve() {
        assertEquals(ResultadoRonda.GANA_J1, Reglas.enfrentar(Elemento.FUEGO, Elemento.NIEVE))
    }
    @Test fun nieveVenceAgua() {
        assertEquals(ResultadoRonda.GANA_J1, Reglas.enfrentar(Elemento.NIEVE, Elemento.AGUA))
    }
    @Test fun empateMismoElemento() {
        assertEquals(ResultadoRonda.EMPATE, Reglas.enfrentar(Elemento.AGUA, Elemento.AGUA))
    }
}
