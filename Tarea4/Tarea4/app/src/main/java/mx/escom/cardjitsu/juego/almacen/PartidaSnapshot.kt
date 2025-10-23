package mx.escom.cardjitsu.juego.almacen

import kotlinx.serialization.Serializable
import mx.escom.cardjitsu.juego.dominio.Elemento
import mx.escom.cardjitsu.juego.dominio.ResultadoRonda
import mx.escom.cardjitsu.presentacion.FaseUI
import mx.escom.cardjitsu.presentacion.ModoJuego

@Serializable
data class PartidaSnapshot(
    val id: String,
    val timestamp: Long,
    val modo: ModoJuego,
    val objetivoVictorias: Int,
    val marcadorJ1: Int,
    val marcadorJ2: Int,
    val fase: FaseUI,
    val resultadoRonda: ResultadoRonda? = null,
    val seleccionJ1: Elemento? = null,
    val seleccionJ2: Elemento? = null
)

@Serializable
data class PartidaResumen(
    val id: String,
    val timestamp: Long,
    val modo: ModoJuego,
    val marcadorJ1: Int,
    val marcadorJ2: Int,
    val objetivoVictorias: Int
)
