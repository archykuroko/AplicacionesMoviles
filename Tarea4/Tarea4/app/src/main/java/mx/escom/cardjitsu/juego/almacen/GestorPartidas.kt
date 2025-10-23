package mx.escom.cardjitsu.juego.almacen

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.util.UUID

class GestorPartidas(private val context: Context) {
    private val dir: File by lazy {
        File(context.filesDir, "partidas").apply { if (!exists()) mkdirs() }
    }

    // Config del serializador (tolerante y bonito)
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private fun archivo(id: String) = File(dir, "$id.json")

    fun listar(): List<PartidaResumen> =
        dir.listFiles { f -> f.extension == "json" }
            ?.mapNotNull { f ->
                runCatching {
                    val snap = json.decodeFromString<PartidaSnapshot>(f.readText())
                    PartidaResumen(
                        id = snap.id,
                        timestamp = snap.timestamp,
                        modo = snap.modo,
                        marcadorJ1 = snap.marcadorJ1,
                        marcadorJ2 = snap.marcadorJ2,
                        objetivoVictorias = snap.objetivoVictorias
                    )
                }.getOrNull()
            }
            ?.sortedByDescending { it.timestamp }
            ?: emptyList()

    fun guardar(snapshot: PartidaSnapshot): String {
        archivo(snapshot.id).writeText(json.encodeToString(snapshot))
        return snapshot.id
    }

    fun nuevaDesde(snapshot: PartidaSnapshot): String {
        val nuevoId = UUID.randomUUID().toString()
        val copia = snapshot.copy(id = nuevoId, timestamp = System.currentTimeMillis())
        return guardar(copia)
    }

    fun cargar(id: String): PartidaSnapshot? =
        runCatching { json.decodeFromString<PartidaSnapshot>(archivo(id).readText()) }.getOrNull()

    fun eliminar(id: String): Boolean = archivo(id).delete()
}
