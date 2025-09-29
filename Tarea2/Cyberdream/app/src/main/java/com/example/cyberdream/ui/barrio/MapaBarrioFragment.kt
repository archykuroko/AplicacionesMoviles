package com.example.cyberdream.ui.barrio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cyberdream.R
import com.example.cyberdream.databinding.FragmentMapaBarrioBinding
import com.example.cyberdream.ui.edificio.EdificioActivity

class MapaBarrioFragment : Fragment() {

    private var _b: FragmentMapaBarrioBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentMapaBarrioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.poiAfterlife.setOnClickListener { abrir("afterlife") }
        b.poiEstacion.setOnClickListener { abrir("estacion") }
        b.poiMods.setOnClickListener { abrir("mods") }
    }

    private fun abrir(id: String) {
        val intent = Intent(requireContext(), EdificioActivity::class.java)
            .putExtra(EdificioActivity.EXTRA_ID_EDIFICIO, id)
        startActivity(intent)
        // animación de entrada/salida entre activities
        requireActivity().overridePendingTransition(
            R.anim.act_slide_in_right,
            R.anim.act_fade_out
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        fun newInstance() = MapaBarrioFragment()
    }
}