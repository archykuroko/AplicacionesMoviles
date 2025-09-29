package com.example.cyberdream.ui.ciudad

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberdream.R
import com.example.cyberdream.databinding.FragmentListaBarriosBinding
import com.example.cyberdream.databinding.ItemBarrioBinding
import com.example.cyberdream.ui.barrio.BarrioActivity

data class BarrioUi(val id: String, val nombre: String, val desc: String, val thumbRes: Int)

class ListaBarriosFragment : Fragment() {

    private var _b: FragmentListaBarriosBinding? = null
    private val b get() = _b!!

    // Datos demo (al menos 1 item para que SI se vea)
    private val barrios = listOf(
        BarrioUi(
            id = "old_downtown",
            nombre = "Old Downtown",
            desc = "Luces viejas, neón eterno.",
            thumbRes = R.drawable.thumb_afterlife // pon cualquiera que tengas
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentListaBarriosBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // “Ping” para confirmar que el fragment se cargó
        Toast.makeText(requireContext(), "Barrios cargados", Toast.LENGTH_SHORT).show()

        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = Adap(barrios) { barrio ->
            // Por ahora solo abrimos Old Downtown
            startActivity(Intent(requireContext(), BarrioActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.act_slide_in_right, R.anim.act_fade_out)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }

    companion object { fun newInstance() = ListaBarriosFragment() }
}

private class Adap(
    private val items: List<BarrioUi>,
    private val onClick: (BarrioUi) -> Unit
) : RecyclerView.Adapter<Adap.VH>() {

    inner class VH(val vb: ItemBarrioBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val vb = ItemBarrioBinding.inflate(LayoutInflater.from(p.context), p, false)
        return VH(vb)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val it = items[pos]
        h.vb.titulo.text = it.nombre
        h.vb.subtitulo.text = it.desc
        if (it.thumbRes != 0) h.vb.img.setImageResource(it.thumbRes)

        h.vb.root.setOnClickListener {
            h.vb.root.startAnimation(AnimationUtils.loadAnimation(h.vb.root.context, R.anim.layout_fall_in))
            onClick(items[h.bindingAdapterPosition])
        }
    }

    override fun getItemCount() = items.size
}