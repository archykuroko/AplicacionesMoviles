package com.example.actividad1.vm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad1.data.datastore.PrefsRepository
import com.example.actividad1.domain.repo.FileRepository
import com.example.actividad1.domain.repo.SafOps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TextViewerViewModel(app: Application) : AndroidViewModel(app) {

    // Dependencias internas
    private val prefs = PrefsRepository(app)
    private val saf = SafOps(app)
    private val repo = FileRepository(app, saf, prefs)

    private val _text = MutableStateFlow<String?>(null)
    val text: StateFlow<String?> = _text

    fun load(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val content = runCatching { repo.openText(uri) }.getOrNull()
            _text.value = content ?: "(No se pudo abrir el archivo)"
        }
    }
}
