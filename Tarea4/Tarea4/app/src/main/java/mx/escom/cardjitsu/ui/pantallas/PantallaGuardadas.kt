package mx.escom.cardjitsu.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.escom.cardjitsu.juego.almacen.GestorPartidas
import mx.escom.cardjitsu.juego.almacen.PartidaResumen
import mx.escom.cardjitsu.presentacion.JuegoViewModel
import mx.escom.cardjitsu.presentacion.ModoJuego

@OptIn(ExperimentalMaterial3Api::class) // ← necesario en tu BOM
@Composable
fun PantallaGuardadas(
    gestor: GestorPartidas,
    vm: JuegoViewModel,
    onCargarAlaPartida: (ModoJuego) -> Unit,
    onVolver: () -> Unit
) {
    var lista by remember { mutableStateOf(emptyList<PartidaResumen>()) }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { lista = gestor.listar() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Partidas guardadas") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { inner ->
        if (lista.isEmpty()) {
            Text(
                "No hay partidas guardadas.",
                modifier = Modifier
                    .padding(inner)
                    .padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista) { item ->
                    ElevatedCard {
                        ListItem(
                            headlineContent = { Text("Modo: ${item.modo}") },
                            supportingContent = {
                                Text("J1: ${item.marcadorJ1}   Objetivo: ${item.objetivoVictorias}   J2: ${item.marcadorJ2}")
                            },
                            trailingContent = {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = {
                                        val snap = gestor.cargar(item.id)
                                        if (snap != null) {
                                            vm.restaurar(snap)
                                            onCargarAlaPartida(snap.modo)
                                        } else {
                                            scope.launch {
                                                snackbar.showSnackbar("No se pudo cargar la partida.")
                                            }
                                        }
                                    }) { Text("Continuar") }

                                    TextButton(onClick = {
                                        val ok = gestor.eliminar(item.id)
                                        if (ok) {
                                            lista = gestor.listar()
                                        } else {
                                            scope.launch {
                                                snackbar.showSnackbar("No se pudo eliminar.")
                                            }
                                        }
                                    }) { Text("Eliminar") }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
