package com.example.cyberdream.ui.edificio

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberdream.R
import com.example.cyberdream.databinding.FragmentPoisEdificioBinding
import com.example.cyberdream.databinding.ItemPoiButtonBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

data class PoiUi(val nombre: String, val desc: String, val iconPink: Boolean = false)

class PuntosInteresFragment : Fragment() {

    private var _b: FragmentPoisEdificioBinding? = null
    private val b get() = _b!!

    private lateinit var edificioId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        edificioId = requireActivity()
            .intent.getStringExtra(EdificioActivity.EXTRA_ID_EDIFICIO) ?: "afterlife"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentPoisEdificioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pois = poisPorEdificio(edificioId)

        b.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        b.recycler.adapter = AdapPois(pois) { poi ->
            mostrarDetalle(poi)
        }
    }

    private fun mostrarDetalle(p: PoiUi) {
        val dialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bs_poi_detalle, null)
        v.findViewById<TextView>(R.id.titulo).text = p.nombre
        v.findViewById<TextView>(R.id.descripcion).text = p.desc
        dialog.setContentView(v)
        dialog.show()
    }

    private fun poisPorEdificio(id: String): List<PoiUi> = when (id) {
        "afterlife" -> listOf(
            PoiUi("Bar principal", "Cócteles fluorescentes y barra holo."),
            PoiUi("Dance Floor", "Pista con bajos subterráneos."),
            PoiUi("VIP Area", "Reservado para fixers.", iconPink = true),
            PoiUi("Backstage", "Acceso del staff.")
        )
        "estacion" -> listOf(
            PoiUi("Platform", "Andén principal con luces intermitentes."),
            PoiUi("Control Room", "Sala de paneles antiguos."),
            PoiUi("Hidden Passage", "Rumores de un túnel ciego.", iconPink = true)
        )
        "mods" -> listOf(
            PoiUi("Cybernetic Shop", "Implantes y repuestos."),
            PoiUi("Repair Garage", "Mecánica de drones y bikes."),
            PoiUi("Black Market", "Chips no rastreables.", iconPink = true)
        )
        else -> emptyList()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }

    companion object { fun newInstance() = PuntosInteresFragment() }
}

private class AdapPois(
    private val items: List<PoiUi>,
    private val onClick: (PoiUi) -> Unit
) : RecyclerView.Adapter<AdapPois.VH>() {

    inner class VH(val vb: ItemPoiButtonBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val vb = ItemPoiButtonBinding.inflate(LayoutInflater.from(p.context), p, false)
        return VH(vb)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val it = items[pos]
        h.vb.root.text = it.nombre
        // alterna icono cian/rosa
        h.vb.root.icon = h.vb.root.context.getDrawable(
            if (it.iconPink) R.drawable.cy_poi_dot_pink else R.drawable.cy_poi_dot
        )
        h.vb.root.setOnClickListener { onClick(items[h.bindingAdapterPosition]) }
    }

    override fun getItemCount() = items.size
}