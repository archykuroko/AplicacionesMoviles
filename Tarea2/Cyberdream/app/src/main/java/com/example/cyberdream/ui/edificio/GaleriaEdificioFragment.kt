package com.example.cyberdream.ui.edificio

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberdream.R
import com.example.cyberdream.databinding.FragmentGaleriaEdificioBinding
import com.example.cyberdream.databinding.ItemGaleriaBinding

class GaleriaEdificioFragment : Fragment() {

    private var _b: FragmentGaleriaEdificioBinding? = null
    private val b get() = _b!!

    private lateinit var edificioId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        edificioId = requireActivity()
            .intent.getStringExtra(EdificioActivity.EXTRA_ID_EDIFICIO) ?: "afterlife"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentGaleriaEdificioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imgs = imagenesPorEdificio(edificioId)

        b.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        b.recycler.adapter = GaleriaAdapter(imgs) { resId ->
            mostrarFullscreen(resId)
        }
    }

    private fun imagenesPorEdificio(id: String): List<Int> = when (id) {
        "afterlife" -> listOf(R.drawable.gal_afterlife_1, R.drawable.gal_afterlife_2)
        "estacion"  -> listOf(R.drawable.gal_estacion_1,  R.drawable.gal_estacion_2)
        "mods"      -> listOf(R.drawable.gal_mods_1,      R.drawable.gal_mods_2)
        else        -> emptyList()
    }

    private fun mostrarFullscreen(resId: Int) {
        val d = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        d.setContentView(R.layout.dialog_imagen)
        d.findViewById<ImageView>(R.id.imgFull).setImageResource(resId)
        d.findViewById<ImageButton>(R.id.btnCerrar).setOnClickListener { d.dismiss() }
        d.show()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }

    companion object { fun newInstance() = GaleriaEdificioFragment() }
}

private class GaleriaAdapter(
    private val items: List<Int>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<GaleriaAdapter.VH>() {

    inner class VH(val vb: ItemGaleriaBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val vb = ItemGaleriaBinding.inflate(LayoutInflater.from(p.context), p, false)
        return VH(vb)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val resId = items[pos]
        h.vb.img.setImageResource(resId)
        h.vb.root.setOnClickListener { onClick(resId) }
    }

    override fun getItemCount() = items.size
}