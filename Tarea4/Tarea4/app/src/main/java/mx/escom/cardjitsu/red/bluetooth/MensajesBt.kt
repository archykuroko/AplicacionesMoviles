package mx.escom.cardjitsu.red.bluetooth

import org.json.JSONObject
import java.util.UUID

val JITSU_UUID: UUID = UUID.fromString("3b8f6d1a-2a9e-4d73-bfda-9a82e690c1b1")

enum class TipoMsg { HELLO, COMMIT, REVEAL, NUEVA_RONDA, RESET, ERROR }

data class Mensaje(val tipo: TipoMsg, val data: Map<String, String> = emptyMap()) {
    fun aJson(): String = JSONObject().apply {
        put("tipo", tipo.name); put("data", JSONObject(data))
    }.toString()
    companion object {
        fun deJson(raw: String): Mensaje {
            val o = JSONObject(raw)
            val t = TipoMsg.valueOf(o.getString("tipo"))
            val d = o.optJSONObject("data")?.let { j ->
                j.keys().asSequence().associateWith { j.getString(it) }
            } ?: emptyMap()
            return Mensaje(t, d)
        }
    }
}
