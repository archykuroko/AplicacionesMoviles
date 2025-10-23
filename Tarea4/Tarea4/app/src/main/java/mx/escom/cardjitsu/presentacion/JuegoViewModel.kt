package mx.escom.cardjitsu.presentacion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import mx.escom.cardjitsu.juego.almacen.PartidaSnapshot
import mx.escom.cardjitsu.juego.dominio.ConfiguracionPartida
import mx.escom.cardjitsu.juego.dominio.Elemento
import mx.escom.cardjitsu.juego.dominio.Reglas
import mx.escom.cardjitsu.juego.dominio.ResultadoRonda
import mx.escom.cardjitsu.red.bluetooth.BluetoothServicio
import mx.escom.cardjitsu.red.bluetooth.Mensaje
import mx.escom.cardjitsu.red.bluetooth.TipoMsg
import mx.escom.cardjitsu.red.bluetooth.sha256
import kotlin.random.Random


/**
 * Orquesta el flujo del duelo:
 *  - LOCAL: J1 y J2 en el mismo dispositivo.
 *  - VS_BOT: J1 vs bot con mini heurística.
 *  - BT: J1 local vs J2 remoto (commit–reveal por Bluetooth).
 */
class JuegoViewModel(
    savedStateHandle: SavedStateHandle,
    private val modo: ModoJuego = ModoJuego.LOCAL
) : ViewModel() {

    // ---------- Configuración ----------
    private var configuracion = ConfiguracionPartida(victoriasObjetivo = 3)

    // ---------- Estado UI ----------
    private val _estado = MutableStateFlow(
        EstadoUIJuego(objetivoVictorias = configuracion.victoriasObjetivo)
    )
    val estado: StateFlow<EstadoUIJuego> = _estado

    // ---------- Soporte BOT ----------
    private var ultimoJ1: Elemento? = null
    private var ultimoBot: Elemento? = null
    private var ultimoResultado: ResultadoRonda? = null

    // ---------- Soporte Bluetooth ----------
    var bt: BluetoothServicio? = null
        private set

    // commit/reveal local y remoto
    private var miCommit: String? = null
    private var suCommit: String? = null
    private var miReveal: Pair<Elemento, String>? = null
    private var suReveal: Pair<Elemento, String>? = null

    /** Inyecta el servicio Bluetooth después de conectarte desde la UI. */
    fun vincularBluetooth(servicio: BluetoothServicio) {
        bt = servicio
    }

    /** Entradas desde la UI */
    fun procesar(intent: IntentJuego) {
        when (intent) {
            is IntentJuego.ElegirJ1 -> elegirJ1(intent.elemento)
            is IntentJuego.ElegirJ2 -> elegirJ2(intent.elemento)
            IntentJuego.Revelar -> revelar()
            IntentJuego.NuevaRonda -> nuevaRonda()
            IntentJuego.ReiniciarPartida -> reiniciarPartida()
        }
    }

    // ---------------- Intents ----------------

    private fun elegirJ1(e: Elemento) {
        when (modo) {
            ModoJuego.LOCAL, ModoJuego.VS_BOT -> {
                _estado.update { s ->
                    if (s.fase != FaseUI.SELECCION_J1 || s.hayGanadorPartida) return@update s
                    s.copy(
                        seleccionJ1 = e,
                        fase = FaseUI.SELECCION_J2,
                        mensajeError = null,
                        resultadoRonda = null
                    )
                }

                if (modo == ModoJuego.VS_BOT) {
                    val eleccionBot = elegirBot()
                    _estado.update { s ->
                        if (s.fase != FaseUI.SELECCION_J2 || s.hayGanadorPartida) return@update s
                        s.copy(
                            seleccionJ2 = eleccionBot,
                            fase = FaseUI.LISTO_PARA_REVELAR,
                            mensajeError = null
                        )
                    }
                }
            }

            ModoJuego.BT -> {
                // En BT: J1 es el local. Enviamos COMMIT.
                if (!_puedeTomarTurno(FaseUI.SELECCION_J1)) return
                _estado.update { it.copy(seleccionJ1 = e, fase = FaseUI.SELECCION_J2, mensajeError = null, resultadoRonda = null) }
                enviarCommit(e)
                // En BT no hay panel J2 local; esperamos su COMMIT desde red.
            }
        }
    }

    private fun elegirJ2(e: Elemento) {
        when (modo) {
            ModoJuego.LOCAL -> {
                _estado.update { s ->
                    if (s.fase != FaseUI.SELECCION_J2 || s.hayGanadorPartida) return@update s
                    s.copy(
                        seleccionJ2 = e,
                        fase = FaseUI.LISTO_PARA_REVELAR,
                        mensajeError = null,
                        resultadoRonda = null
                    )
                }
            }
            ModoJuego.VS_BOT, ModoJuego.BT -> {
                // Ignorado: en BOT elije la IA; en BT lo elige el rival remoto
            }
        }
    }

    private fun revelar() {
        when (modo) {
            ModoJuego.LOCAL, ModoJuego.VS_BOT -> revelarLocalOBot()
            ModoJuego.BT -> revelarBluetooth()
        }
    }

    private fun nuevaRonda() {
        _estado.update { s ->
            if (s.hayGanadorPartida) return@update s
            s.copy(
                seleccionJ1 = null,
                seleccionJ2 = null,
                resultadoRonda = null,
                fase = FaseUI.SELECCION_J1,
                mensajeError = null
            )
        }
        // limpiar protocolo BT
        miCommit = null; suCommit = null; miReveal = null; suReveal = null
    }

    private fun reiniciarPartida() {
        _estado.update {
            EstadoUIJuego(
                marcadorJ1 = 0,
                marcadorJ2 = 0,
                objetivoVictorias = configuracion.victoriasObjetivo,
                seleccionJ1 = null,
                seleccionJ2 = null,
                fase = FaseUI.SELECCION_J1,
                resultadoRonda = null,
                mensajeError = null
            )
        }
        // limpiar protocolo BT
        miCommit = null; suCommit = null; miReveal = null; suReveal = null
    }

    // ---------------- LÓGICA LOCAL / BOT ----------------

    private fun revelarLocalOBot() {
        _estado.update { s ->
            if (s.hayGanadorPartida) return@update s
            val p1 = s.seleccionJ1
            val p2 = s.seleccionJ2
            if (p1 == null || p2 == null || s.fase != FaseUI.LISTO_PARA_REVELAR) {
                return@update s.copy(mensajeError = "Falta selección para revelar.")
            }

            val resultado = Reglas.enfrentar(p1, p2)
            val (nuevoJ1, nuevoJ2) = when (resultado) {
                ResultadoRonda.GANA_J1 -> s.marcadorJ1 + 1 to s.marcadorJ2
                ResultadoRonda.GANA_J2 -> s.marcadorJ1 to s.marcadorJ2 + 1
                ResultadoRonda.EMPATE -> s.marcadorJ1 to s.marcadorJ2
            }
            val fin = (nuevoJ1 >= configuracion.victoriasObjetivo) || (nuevoJ2 >= configuracion.victoriasObjetivo)

            // memoria del bot
            ultimoJ1 = p1; ultimoBot = p2; ultimoResultado = resultado

            s.copy(
                marcadorJ1 = nuevoJ1,
                marcadorJ2 = nuevoJ2,
                resultadoRonda = resultado,
                fase = if (fin) FaseUI.FIN_PARTIDA else FaseUI.MOSTRANDO_RESULTADO,
                mensajeError = null
            )
        }
    }

    private fun elegirBot(): Elemento {
        fun counter(e: Elemento) = when (e) {
            Elemento.FUEGO -> Elemento.AGUA
            Elemento.AGUA -> Elemento.NIEVE
            Elemento.NIEVE -> Elemento.FUEGO
        }
        val j1Prev = ultimoJ1
        val resPrev = ultimoResultado
        if (j1Prev != null && resPrev == ResultadoRonda.GANA_J1) {
            if (Random.nextFloat() < 0.7f) return counter(j1Prev)
        }
        val valores = Elemento.values()
        return valores[Random.nextInt(valores.size)]
    }

    // ---------------- LÓGICA BLUETOOTH (commit–reveal) ----------------

    /** Envía COMMIT de mi elección (hash = sha256("ELECCION.NONCE")). */
    private fun enviarCommit(eleccion: Elemento) {
        val nonce = Random.nextBytes(8).joinToString("") { "%02x".format(it) }
        val h = sha256("${eleccion.name}.$nonce")
        miCommit = h
        miReveal = eleccion to nonce
        bt?.enviar(Mensaje(TipoMsg.COMMIT, mapOf("h" to h)))
    }

    /** En BT: al tocar Revelar, envío mi REVEAL. El resultado se decide al tener ambos reveals válidos. */
    private fun revelarBluetooth() {
        val fase = estado.value.fase
        if (fase != FaseUI.LISTO_PARA_REVELAR && fase != FaseUI.SELECCION_J2) {
            _estado.update { it.copy(mensajeError = "Aún no se puede revelar.") }
            return
        }
        val (ele, nonce) = miReveal ?: run {
            _estado.update { it.copy(mensajeError = "Primero elige tu elemento.") }; return
        }
        bt?.enviar(Mensaje(TipoMsg.REVEAL, mapOf("e" to ele.name, "n" to nonce)))
        // El cálculo final ocurre en onMensajeBt() cuando ambos reveals están listos
    }

    /** Manejo de mensajes entrantes desde el servicio BT (llama la pantalla). */
    fun onMensajeBt(m: Mensaje) {
        when (m.tipo) {
            TipoMsg.COMMIT -> {
                suCommit = m.data["h"]
                // Si ya tenemos nuestro commit también, habilitamos "Revelar"
                _estado.update { s ->
                    if (miCommit != null && s.fase != FaseUI.LISTO_PARA_REVELAR) s.copy(fase = FaseUI.LISTO_PARA_REVELAR)
                    else s
                }
            }

            TipoMsg.REVEAL -> {
                val eStr = m.data["e"] ?: return
                val nStr = m.data["n"] ?: return
                val ele = runCatching { Elemento.valueOf(eStr) }.getOrNull() ?: return
                val esperado = suCommit
                val calc = sha256("${ele.name}.$nStr")
                if (esperado != null && calc == esperado) {
                    suReveal = ele to nStr
                    // mostrar selección rival en UI:
                    _estado.update { s -> s.copy(seleccionJ2 = ele) }
                    intentarResolverBt()
                } else {
                    _estado.update { it.copy(mensajeError = "Hash inválido recibido (desincronizado).") }
                }
            }

            TipoMsg.NUEVA_RONDA -> nuevaRonda()
            TipoMsg.RESET -> reiniciarPartida()
            else -> { /* ignoramos otros por ahora */ }
        }
    }

    // ====== SNAPSHOT API ======

    fun crearSnapshot(): PartidaSnapshot {
        val s = estado.value
        return PartidaSnapshot(
            id = java.util.UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            modo = modo,
            objetivoVictorias = s.objetivoVictorias,
            marcadorJ1 = s.marcadorJ1,
            marcadorJ2 = s.marcadorJ2,
            fase = s.fase,
            resultadoRonda = s.resultadoRonda,
            seleccionJ1 = s.seleccionJ1,
            seleccionJ2 = s.seleccionJ2
        )
    }

    fun restaurar(snapshot: PartidaSnapshot) {
        _estado.value = EstadoUIJuego(
            objetivoVictorias = snapshot.objetivoVictorias,
            marcadorJ1 = snapshot.marcadorJ1,
            marcadorJ2 = snapshot.marcadorJ2,
            fase = snapshot.fase,
            resultadoRonda = snapshot.resultadoRonda,
            seleccionJ1 = snapshot.seleccionJ1,
            seleccionJ2 = snapshot.seleccionJ2,
            mensajeError = null
        )
    }

    /** Cuando tengo ambos reveals válidos, decido la ronda y limpio protocolo. */
    private fun intentarResolverBt() {
        val mio = miReveal?.first
        val suyo = suReveal?.first
        if (mio == null || suyo == null) return

        _estado.update { s ->
            val resultado = Reglas.enfrentar(mio, suyo)
            val (nuevoJ1, nuevoJ2) = when (resultado) {
                ResultadoRonda.GANA_J1 -> s.marcadorJ1 + 1 to s.marcadorJ2
                ResultadoRonda.GANA_J2 -> s.marcadorJ1 to s.marcadorJ2 + 1
                ResultadoRonda.EMPATE -> s.marcadorJ1 to s.marcadorJ2
            }
            val fin = (nuevoJ1 >= configuracion.victoriasObjetivo) || (nuevoJ2 >= configuracion.victoriasObjetivo)

            // limpiar protocolo para la siguiente ronda
            miCommit = null; suCommit = null; miReveal = null; suReveal = null

            s.copy(
                marcadorJ1 = nuevoJ1,
                marcadorJ2 = nuevoJ2,
                resultadoRonda = resultado,
                fase = if (fin) FaseUI.FIN_PARTIDA else FaseUI.MOSTRANDO_RESULTADO,
                mensajeError = null
            )
        }
    }

    // ---------------- Util ----------------
    private fun _puedeTomarTurno(faseRequerida: FaseUI): Boolean {
        val s = estado.value
        if (s.hayGanadorPartida) return false
        if (s.fase != faseRequerida) return false
        return true
    }
}



