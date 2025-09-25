package com.example.cyberdream.ui.ciudad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cyberdream.databinding.FragmentListaBarriosBinding

class ListaBarriosFragment : Fragment() {
    private var _b: FragmentListaBarriosBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentListaBarriosBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        fun newInstance() = ListaBarriosFragment()
    }
}
