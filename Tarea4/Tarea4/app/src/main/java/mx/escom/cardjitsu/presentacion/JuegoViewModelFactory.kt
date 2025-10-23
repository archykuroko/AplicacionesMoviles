package mx.escom.cardjitsu.presentacion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JuegoViewModelFactory(
    private val modo: ModoJuego
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return JuegoViewModel(SavedStateHandle(), modo) as T
    }
}
