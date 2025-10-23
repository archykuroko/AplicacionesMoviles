package mx.escom.cardjitsu

import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mx.escom.cardjitsu.presentacion.JuegoViewModel
import mx.escom.cardjitsu.presentacion.JuegoViewModelFactory
import mx.escom.cardjitsu.presentacion.ModoJuego
import mx.escom.cardjitsu.red.bluetooth.BluetoothServicio
import mx.escom.cardjitsu.red.bluetooth.Mensaje
import mx.escom.cardjitsu.ui.pantallas.PantallaBluetooth
import mx.escom.cardjitsu.ui.pantallas.PantallaGuardadas
import mx.escom.cardjitsu.ui.pantallas.PantallaMenu
import mx.escom.cardjitsu.ui.pantallas.PantallaPartida
import mx.escom.cardjitsu.ui.tema.FondoDojo
import mx.escom.cardjitsu.ui.tema.TemaCardJitsu
import mx.escom.cardjitsu.juego.almacen.GestorPartidas

enum class Pantalla { MENU, PARTIDA, BLUETOOTH, GUARDADAS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TemaCardJitsu {
                FondoDojo {
                    var pantalla by rememberSaveable { mutableStateOf(Pantalla.MENU) }
                    var modo by rememberSaveable { mutableStateOf(ModoJuego.LOCAL) }

                    // VM compartido (clave por modo para recrearlo cuando cambias)
                    val vm: JuegoViewModel = viewModel(
                        key = "vm_${modo.name}",
                        factory = JuegoViewModelFactory(modo)
                    )

                    // Servicio Bluetooth único
                    val btServicio = remember {
                        val bm = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                        BluetoothServicio(bm.adapter)
                    }

                    // Reenvía mensajes BT al VM (activo en todo el ciclo)
                    LaunchedEffect(btServicio) {
                        btServicio.mensajes.collect { m: Mensaje -> vm.onMensajeBt(m) }
                    }

                    // Gestor de partidas (JSON en almacenamiento interno)
                    val gestorPartidas = remember { GestorPartidas(applicationContext) }

                    when (pantalla) {
                        Pantalla.MENU -> PantallaMenu(
                            onJugarLocal = {
                                modo = ModoJuego.LOCAL
                                pantalla = Pantalla.PARTIDA
                            },
                            onJugarVsBot = {
                                modo = ModoJuego.VS_BOT
                                pantalla = Pantalla.PARTIDA
                            },
                            onJugarBt = {
                                pantalla = Pantalla.BLUETOOTH
                            },
                            onPartidasGuardadas = {
                                pantalla = Pantalla.GUARDADAS
                            }
                        )

                        Pantalla.BLUETOOTH -> PantallaBluetooth(
                            servicio = btServicio,
                            vincularVm = { vm.vincularBluetooth(btServicio) },
                            onConectado = {
                                modo = ModoJuego.BT
                                pantalla = Pantalla.PARTIDA
                            },
                            onVolver = { pantalla = Pantalla.MENU }
                        )

                        Pantalla.GUARDADAS -> PantallaGuardadas(
                            gestor = gestorPartidas,
                            vm = vm,
                            onCargarAlaPartida = { modoGuardado ->
                                // Si el modo guardado difiere, cámbialo para recrear el VM
                                if (modo != modoGuardado) {
                                    modo = modoGuardado
                                }
                                pantalla = Pantalla.PARTIDA
                            },
                            onVolver = { pantalla = Pantalla.MENU }
                        )

                        Pantalla.PARTIDA -> PantallaPartida(
                            modo = modo,
                            vm = vm,
                            gestor = gestorPartidas,
                            onVolverMenu = {
                                // Si sales desde BT, cierra sockets
                                if (modo == ModoJuego.BT) btServicio.apagar()
                                pantalla = Pantalla.MENU
                            }
                        )
                    }
                }
            }
        }
    }
}
