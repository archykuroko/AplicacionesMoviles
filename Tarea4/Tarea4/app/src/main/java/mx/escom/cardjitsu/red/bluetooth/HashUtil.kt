package mx.escom.cardjitsu.red.bluetooth

import java.security.MessageDigest
fun sha256(texto: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(texto.toByteArray()).joinToString("") { "%02x".format(it) }
}
