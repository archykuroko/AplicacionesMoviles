package com.example.cyberdream.ui.edificio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.cyberdream.R
import com.example.cyberdream.data.DatosDemo
import com.example.cyberdream.databinding.FragmentInfoEdificioBinding

class InfoEdificioFragment : Fragment() {

    companion object {
        private const val ARG_ID = "id"
        fun newInstance(id: String, unused: String? = null) =
            InfoEdificioFragment().apply { arguments = bundleOf(ARG_ID to id) }
    }

    private var _b: FragmentInfoEdificioBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentInfoEdificioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = requireArguments().getString(ARG_ID) ?: "afterlife"
        val edificio = DatosDemo.oldDowntown.edificios.firstOrNull { it.id == id }

        b.txtTitulo.text = "Resumen"
        b.txtDescripcion.text = edificio?.descripcion ?: "Descripción no disponible."

        val mapa = when (id) {
            "afterlife" -> R.drawable.map_afterlife
            "estacion"  -> R.drawable.map_estacion
            "mods"      -> R.drawable.map_mods
            else        -> R.drawable.map_afterlife
        }
        b.imgMapaInterno.setImageResource(mapa)
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}