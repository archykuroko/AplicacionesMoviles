package com.example.actividad1.vm

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad1.data.datastore.PrefsRepository
import com.example.actividad1.domain.model.FsItem
import com.example.actividad1.domain.repo.FileRepository
import com.example.actividad1.domain.repo.SafOps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrowserViewModel(app: Application) : AndroidViewModel(app) {

    // Construye dependencias internamente (sin DI por ahora)
    private val prefs = PrefsRepository(app)
    private val saf = SafOps(app)
    private val repo = FileRepository(app, saf, prefs)

    data class UiState(
        val currentTree: Uri? = null,
        val pathStack: List<Uri> = emptyList(),
        val items: List<FsItem> = emptyList(),
        val query: String = "",
        val loading: Boolean = false,
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var favorites: Set<String> = emptySet()

    init {
        viewModelScope.launch {
            prefs.favoritesFlow.collect { f ->
                favorites = f
                refresh()
            }
        }
        viewModelScope.launch {
            prefs.treeUriFlow.collect { s ->
                val uri = s?.let(Uri::parse)
                _state.update { it.copy(currentTree = uri, pathStack = uri?.let { listOf(it) } ?: emptyList()) }
                refresh()
            }
        }
    }

    /** Guarda permisos persistentes y fija árbol raíz */
    fun setTreeUri(uri: Uri, flags: Int) {
        val cr = getApplication<Application>().contentResolver
        try {
            cr.takePersistableUriPermission(
                uri,
                flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            )
        } catch (_: SecurityException) { /* ignore */ }

        viewModelScope.launch { prefs.setTreeUri(uri.toString()) }
    }

    fun openFolder(uri: Uri) {
        _state.update { it.copy(pathStack = it.pathStack + uri) }
        refresh()
    }

    fun goUp() {
        _state.update { st ->
            if (st.pathStack.size > 1) st.copy(pathStack = st.pathStack.dropLast(1)) else st
        }
        refresh()
    }

    fun setQuery(q: String) {
        _state.update { it.copy(query = q) }
        refresh()
    }

    fun toggleFavorite(item: FsItem) = viewModelScope.launch {
        prefs.toggleFavorite(item.uri.toString())
    }


    // Ir a un nivel específico del breadcrumb
    fun jumpTo(index: Int) {
        _state.update { st ->
            if (index in st.pathStack.indices) st.copy(pathStack = st.pathStack.take(index + 1)) else st
        }
        refresh()
    }

    // Crear carpeta en el directorio actual
    fun createFolder(name: String, onResult: (Boolean, String) -> Unit = {_,_->}) {
        val current = _state.value.pathStack.lastOrNull() ?: _state.value.currentTree ?: return
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val parent = androidx.documentfile.provider.DocumentFile.fromTreeUri(getApplication(), current)
                val ok = parent?.createDirectory(name) != null
                onResult(ok, if (ok) "Carpeta creada" else "No se pudo crear")
                refresh()
            }.onFailure { e -> onResult(false, e.message ?: "Error") }
        }
    }

    fun renameItem(uri: android.net.Uri, newName: String, onResult: (Boolean, String) -> Unit = {_,_->}) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val df = androidx.documentfile.provider.DocumentFile.fromSingleUri(getApplication(), uri)
                val ok = df?.renameTo(newName) == true
                onResult(ok, if (ok) "Renombrado" else "No se pudo renombrar")
                refresh()
            }.onFailure { e -> onResult(false, e.message ?: "Error") }
        }
    }

    fun deleteItem(uri: android.net.Uri, onResult: (Boolean, String) -> Unit = {_,_->}) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val df = androidx.documentfile.provider.DocumentFile.fromSingleUri(getApplication(), uri)
                val ok = df?.delete() == true
                onResult(ok, if (ok) "Eliminado" else "No se pudo eliminar")
                refresh()
            }.onFailure { e -> onResult(false, e.message ?: "Error") }
        }
    }


    private fun refresh() {
        val st = _state.value
        val current = st.pathStack.lastOrNull() ?: st.currentTree ?: return
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                _state.update { it.copy(loading = true, error = null) }
                val list = repo.list(current, favorites).let { l ->
                    val q = st.query.trim()
                    if (q.isEmpty()) l
                    else l.filter { it.name.contains(q, true) || (it.mimeType ?: "").contains(q, true) }
                }
                _state.update { it.copy(items = list, loading = false) }
            }.onFailure { e ->
                _state.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    // ======= Portapapeles de archivos =======
    enum class ClipMode { COPY, CUT }
    data class Clip(val mode: ClipMode, val uri: Uri, val name: String)

    private val _clip = MutableStateFlow<Clip?>(null)
    val clip: StateFlow<Clip?> = _clip.asStateFlow()

    fun startCopy(item: FsItem) {
        _clip.value = Clip(ClipMode.COPY, item.uri, item.name)
    }

    fun startCut(item: FsItem) {
        _clip.value = Clip(ClipMode.CUT, item.uri, item.name)
    }

    fun clearClipboard() { _clip.value = null }

    /** Pegar en el directorio actual del stack. Si es CUT: copia + borra el original. */
    fun pasteHere(onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        val current = _state.value.pathStack.lastOrNull() ?: _state.value.currentTree
        val clip = _clip.value
        if (current == null || clip == null) {
            onResult(false, "Nada para pegar")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val ctx = getApplication<Application>()
                val parent = androidx.documentfile.provider.DocumentFile.fromTreeUri(ctx, current)
                    ?: return@runCatching onResult(false, "Destino inválido")
                val name = clip.name

                // Copia usando FileRepository
                val okCopy = repo.copyFile(clip.uri, parent, name)
                if (!okCopy) {
                    onResult(false, "No se pudo pegar")
                    return@runCatching
                }

                if (clip.mode == ClipMode.CUT) {
                    // Eliminar origen (move emulado)
                    androidx.documentfile.provider.DocumentFile.fromSingleUri(ctx, clip.uri)?.delete()
                }

                _clip.value = null
                onResult(true, "Pegado")
                refresh()
            }.onFailure { e ->
                onResult(false, e.message ?: "Error al pegar")
            }
        }
    }

}
