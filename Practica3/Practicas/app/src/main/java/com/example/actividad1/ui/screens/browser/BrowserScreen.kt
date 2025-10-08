package com.example.actividad1.ui.screens.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.actividad1.domain.model.FsType
import com.example.actividad1.ui.components.EmptyState
import com.example.actividad1.vm.BrowserViewModel
import com.example.actividad1.vm.BrowserViewModel.ClipMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    onOpenText: (String) -> Unit,
    onOpenImage: (String) -> Unit,
    onOpenSettings: () -> Unit,
    vm: BrowserViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val state by vm.state.collectAsState()
    val clip by vm.clip.collectAsState()

    // Diálogos
    var showNewFolder by remember { mutableStateOf(false) }
    var newFolderName by remember { mutableStateOf("") }

    var pendingRenameUri by remember { mutableStateOf<Uri?>(null) }
    var renameText by remember { mutableStateOf("") }

    var pendingDeleteUri by remember { mutableStateOf<Uri?>(null) }

    // Feedback
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Selector SAF para árbol raíz
    val pickRoot = rememberLauncherForActivityResult(OpenDocumentTree()) { uri ->
        if (uri != null) {
            vm.setTreeUri(
                uri = uri,
                flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            )
        }
    }

    Scaffold(
        topBar = {
            BrowserTopBar(
                onPickRoot = { pickRoot.launch(null) },
                onSettings = onOpenSettings,
                query = state.query,
                onQuery = vm::setQuery
            )
        },
        floatingActionButton = {
            if (state.currentTree != null) {
                if (clip != null) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            vm.pasteHere { _, msg ->
                                scope.launch { snackbar.showSnackbar(msg) }
                            }
                        }
                    ) {
                        Text(if (clip?.mode == ClipMode.CUT) "Mover aquí" else "Pegar aquí")
                    }
                } else {
                    FloatingActionButton(onClick = { showNewFolder = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Nueva carpeta")
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            // Breadcrumb
            if (state.currentTree != null) {
                BreadcrumbRow(
                    context = ctx,
                    stack = state.pathStack,
                    onSegmentClick = vm::jumpTo
                )
                Divider()
            }

            // Contenido
            when {
                state.currentTree == null -> EmptyState(
                    text = "Elige tu carpeta raíz para empezar",
                    actionText = "Seleccionar carpeta",
                    onAction = { pickRoot.launch(null) }
                )

                state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                else -> BrowserContent(
                    items = state.items,
                    onItemClick = { item ->
                        when (item.type) {
                            FsType.FOLDER -> vm.openFolder(item.uri)
                            FsType.TEXT   -> onOpenText(item.uri.toString())
                            FsType.IMAGE  -> onOpenImage(item.uri.toString())
                            FsType.OTHER  -> openWithIntent(ctx, item.uri, item.mimeType)
                        }
                    },
                    onFav = vm::toggleFavorite,
                    onGoUp = vm::goUp,
                    onRenameRequest = { uri, currentName ->
                        pendingRenameUri = uri; renameText = currentName
                    },
                    onDeleteRequest = { uri ->
                        pendingDeleteUri = uri
                    },
                    onCopyRequest = { item ->
                        vm.startCopy(item)
                        scope.launch { snackbar.showSnackbar("Copiado: ${item.name}") }
                    },
                    onCutRequest = { item ->
                        vm.startCut(item)
                        scope.launch { snackbar.showSnackbar("Cortado: ${item.name}") }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // --- Diálogo: Nueva carpeta ---
    if (showNewFolder) {
        AlertDialog(
            onDismissRequest = { showNewFolder = false },
            title = { Text("Nueva carpeta") },
            text = {
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    singleLine = true,
                    placeholder = { Text("Nombre") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val name = newFolderName.trim()
                    if (name.isNotEmpty()) {
                        vm.createFolder(name) { _, msg ->
                            newFolderName = ""
                            showNewFolder = false
                            scope.launch { snackbar.showSnackbar(msg) }
                        }
                    }
                }) { Text("Crear") }
            },
            dismissButton = { TextButton(onClick = { showNewFolder = false }) { Text("Cancelar") } }
        )
    }

    // --- Diálogo: Renombrar ---
    pendingRenameUri?.let { target ->
        AlertDialog(
            onDismissRequest = { pendingRenameUri = null },
            title = { Text("Renombrar") },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val newName = renameText.trim()
                    if (newName.isNotEmpty()) {
                        vm.renameItem(target, newName) { _, msg ->
                            pendingRenameUri = null
                            scope.launch { snackbar.showSnackbar(msg) }
                        }
                    }
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { pendingRenameUri = null }) { Text("Cancelar") } }
        )
    }

    // --- Diálogo: Eliminar ---
    pendingDeleteUri?.let { target ->
        AlertDialog(
            onDismissRequest = { pendingDeleteUri = null },
            title = { Text("Eliminar") },
            text = { Text("¿Seguro que deseas eliminar este elemento? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteItem(target) { _, msg ->
                        pendingDeleteUri = null
                        scope.launch { snackbar.showSnackbar(msg) }
                    }
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { pendingDeleteUri = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun BreadcrumbRow(
    context: Context,
    stack: List<Uri>,
    onSegmentClick: (Int) -> Unit
) {
    val names = remember(stack) {
        stack.mapIndexed { idx, uri ->
            val name = DocumentFile.fromTreeUri(context, uri)?.name
                ?: uri.lastPathSegment?.substringAfterLast(':') ?: "Nivel ${idx + 1}"
            name
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val annotated = buildAnnotatedString {
            for (i in names.indices) {
                if (i > 0) withStyle(SpanStyle(fontWeight = FontWeight.Normal)) { append(" > ") }
                pushStringAnnotation(tag = "SEG", annotation = i.toString())
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(names[i]) }
                pop()
            }
        }
        ClickableText(
            text = annotated,
            style = MaterialTheme.typography.titleMedium,
            onClick = { offset ->
                annotated.getStringAnnotations(tag = "SEG", start = offset, end = offset)
                    .firstOrNull()?.let { onSegmentClick(it.item.toInt()) }
            }
        )
    }
}

fun openWithIntent(ctx: Context, uri: Uri, mime: String?) {
    val i = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mime ?: "*/*")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    ctx.startActivity(Intent.createChooser(i, "Abrir con…"))
}
