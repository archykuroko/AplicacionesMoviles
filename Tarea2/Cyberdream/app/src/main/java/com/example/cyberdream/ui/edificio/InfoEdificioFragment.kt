package com.example.cyberdream.ui.edificio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.cyberdream.data.DatosDemo
import com.example.cyberdream.databinding.FragmentInfoEdificioBinding

class InfoEdificioFragment : Fragment() {

    companion object {
        private const val ARG_ID = "id"
        private const val ARG_TN = "tn"

        fun newInstance(id: String, transitionName: String? = null) =
            InfoEdificioFragment().apply {
                arguments = bundleOf(ARG_ID to id, ARG_TN to transitionName)
            }
    }

    private var _b: FragmentInfoEdificioBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentInfoEdificioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = requireArguments().getString(ARG_ID) ?: "afterlife"
        val tn = requireArguments().getString(ARG_TN)

        // Aplica el mismo transitionName a la imagen destino
        if (!tn.isNullOrEmpty()) {
            ViewCompat.setTransitionName(b.imgPortada, tn)
        }

        val edificio = DatosDemo.oldDowntown.edificios.firstOrNull { it.id == id }
        b.txtTitulo.text = edificio?.nombre ?: "Edificio"
        b.txtDescripcion.text = edificio?.descripcion ?: "Descripción no disponible"

        // Si cargaras imagen asíncrona, empieza la transición en onResourceReady.
        // Como usamos placeholder sin carga, arrancamos ya:
        requireActivity().supportStartPostponedEnterTransition()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
