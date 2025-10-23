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
import mx.escom.cardjitsu.ui.pantallas.PantallaMenu
import mx.escom.cardjitsu.ui.pantallas.PantallaPartida
import mx.escom.cardjitsu.ui.tema.FondoDojo
import mx.escom.cardjitsu.ui.tema.TemaCardJitsu

enum class Pantalla { MENU, PARTIDA, BLUETOOTH }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TemaCardJitsu {
                FondoDojo {
                    var pantalla by rememberSaveable { mutableStateOf(Pantalla.MENU) }
                    var modo by rememberSaveable { mutableStateOf(ModoJuego.LOCAL) }


                    val vm: JuegoViewModel = viewModel(
                        key = "vm_${modo.name}",
                        factory = JuegoViewModelFactory(modo)
                    )


                    val btServicio = remember {
                        val bm = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                        BluetoothServicio(bm.adapter)
                    }


                    LaunchedEffect(btServicio) {
                        btServicio.mensajes.collect { msg: Mensaje ->
                            vm.onMensajeBt(msg)
                        }
                    }

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
                                // Primero vamos a la pantalla de conexión BT
                                pantalla = Pantalla.BLUETOOTH
                            }
                        )

                        Pantalla.BLUETOOTH -> PantallaBluetooth(
                            // Le pasamos el servicio y vinculamos el VM
                            servicio = btServicio,
                            vincularVm = { vm.vincularBluetooth(btServicio) },
                            onConectado = {
                                // Ya conectados → modo BT y a la partida
                                modo = ModoJuego.BT
                                pantalla = Pantalla.PARTIDA
                            },
                            onVolver = { pantalla = Pantalla.MENU }
                        )

                        Pantalla.PARTIDA -> PantallaPartida(
                            modo = modo,
                            vm = vm, // usamos el VM compartido
                            onVolverMenu = {
                                // Si estabas en BT y regresas, cerramos sockets
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
