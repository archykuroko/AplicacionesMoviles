package com.example.cyberdream.ui.barrio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cyberdream.databinding.FragmentMapaBarrioBinding

class MapaBarrioFragment : Fragment() {
    private var _b: FragmentMapaBarrioBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentMapaBarrioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }

    companion object { fun newInstance() = MapaBarrioFragment() }
}
