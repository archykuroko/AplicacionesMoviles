package mx.escom.cardjitsu.red.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

class BluetoothServicio(private val adapter: BluetoothAdapter) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _estado = MutableStateFlow<EstadoBt>(EstadoBt.Listo)
    val estado = _estado.asStateFlow()

    private val _mensajes = MutableSharedFlow<Mensaje>(extraBufferCapacity = 32)
    val mensajes = _mensajes.asSharedFlow()

    private var server: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null
    private var lectorJob: Job? = null

    fun apagar() {
        try { lectorJob?.cancel() } catch (_:Exception){}
        try { socket?.close() } catch (_:Exception){}
        try { server?.close() } catch (_:Exception){}
        _estado.value = EstadoBt.Listo
    }

    // HOST
    @SuppressLint("MissingPermission")
    fun iniciarHost(nombreServicio: String = "CardJitsu") {
        apagar()
        _estado.value = EstadoBt.Esperando
        server = adapter.listenUsingRfcommWithServiceRecord(nombreServicio, JITSU_UUID)
        scope.launch {
            try {
                val s = server!!.accept()
                socket = s
                _estado.value = EstadoBt.Conectado(s.remoteDevice.name ?: "Invitado")
                escuchar(s)
            } catch (e: Exception) {
                _estado.value = EstadoBt.Error("Host falló: ${e.message}")
                apagar()
            }
        }
    }

    // CLIENTE
    @SuppressLint("MissingPermission")
    fun conectar(device: BluetoothDevice) {
        apagar()
        _estado.value = EstadoBt.Conectando(device.name ?: "Host")
        scope.launch {
            try {
                val s = device.createRfcommSocketToServiceRecord(JITSU_UUID)
                adapter.cancelDiscovery()
                s.connect()
                socket = s
                _estado.value = EstadoBt.Conectado(device.name ?: "Host")
                escuchar(s)
            } catch (e: Exception) {
                _estado.value = EstadoBt.Error("Conexión fallida: ${e.message}")
                apagar()
            }
        }
    }

    private fun escuchar(s: BluetoothSocket) {
        lectorJob?.cancel()
        lectorJob = scope.launch {
            val br = BufferedReader(InputStreamReader(s.inputStream))
            while (isActive) {
                val line = br.readLine() ?: break
                runCatching { Mensaje.deJson(line) }
                    .onSuccess { _mensajes.emit(it) }
                    .onFailure { _mensajes.emit(Mensaje(TipoMsg.ERROR, mapOf("e" to it.message.orEmpty()))) }
            }
            _estado.value = EstadoBt.Listo
        }
    }

    fun enviar(msg: Mensaje) {
        val s = socket ?: return
        scope.launch {
            try {
                PrintWriter(s.outputStream, true).println(msg.aJson())
            } catch (e: Exception) {
                _estado.value = EstadoBt.Error("Envio falló: ${e.message}")
            }
        }
    }
}
