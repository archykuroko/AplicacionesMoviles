package com.example.cyberdream.ui.edificio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.cyberdream.databinding.FragmentGaleriaEdificioBinding

class GaleriaEdificioFragment : Fragment() {

    companion object {
        private const val ARG_ID = "id"
        fun newInstance(id: String) = GaleriaEdificioFragment().apply {
            arguments = bundleOf(ARG_ID to id)
        }
    }

    private var _b: FragmentGaleriaEdificioBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentGaleriaEdificioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = requireArguments().getString(ARG_ID) ?: "afterlife"
        b.txtGaleriaTitulo.text = "Galería de: $id"
        // Más adelante: aquí puedes cargar imágenes reales (drawables o URLs)
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
