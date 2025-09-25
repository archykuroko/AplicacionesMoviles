package com.example.cyberdream.ui.edificio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberdream.data.DatosDemo
import com.example.cyberdream.data.PuntoInteres
import com.example.cyberdream.databinding.FragmentPoisBinding
import com.example.cyberdream.databinding.ItemPoiBinding

class PuntosInteresFragment : Fragment() {

    companion object {
        private const val ARG_ID = "id"
        fun newInstance(id: String) = PuntosInteresFragment().apply {
            arguments = bundleOf(ARG_ID to id)
        }
    }

    private var _b: FragmentPoisBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentPoisBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = requireArguments().getString(ARG_ID) ?: return
        val edificio = DatosDemo.oldDowntown.edificios.firstOrNull { it.id == id }
        val lista: List<PuntoInteres> = edificio?.puntos ?: emptyList()

        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = PoisAdapter(lista)
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}

class PoisAdapter(private val items: List<PuntoInteres>) :
    RecyclerView.Adapter<PoisAdapter.VH>() {

    inner class VH(val vb: ItemPoiBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = ItemPoiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(vb)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.vb.titulo.text = it.nombre
        holder.vb.subtitulo.text = it.descripcion
    }

    override fun getItemCount() = items.size
}
