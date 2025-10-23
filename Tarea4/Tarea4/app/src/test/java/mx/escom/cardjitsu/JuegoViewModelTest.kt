package mx.escom.cardjitsu.presentacion

import mx.escom.cardjitsu.juego.dominio.Elemento
import mx.escom.cardjitsu.juego.dominio.ResultadoRonda
import org.junit.Assert.assertEquals
import org.junit.Test

class JuegoViewModelTest {

    @Test
    fun flujoBasico_ganaJ1_y_pasaDeFase() {
        val vm = JuegoViewModel(androidx.lifecycle.SavedStateHandle())

        vm.procesar(IntentJuego.ElegirJ1(Elemento.AGUA))
        vm.procesar(IntentJuego.ElegirJ2(Elemento.FUEGO))
        vm.procesar(IntentJuego.Revelar)

        val s = vm.estado.value
        assertEquals(ResultadoRonda.GANA_J1, s.resultadoRonda)
        assertEquals(1, s.marcadorJ1)
        assertEquals(FaseUI.MOSTRANDO_RESULTADO, s.fase)
    }
}
