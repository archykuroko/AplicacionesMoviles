package com.example.cyberdream.ui.ciudad

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberdream.databinding.FragmentNoticiasBinding
import com.example.cyberdream.databinding.ItemNoticiaBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.cyberdream.R
import android.widget.TextView

data class NoticiaUi(val titulo: String, val resumen: String, val fecha: String, val fuente: String)

class NoticiasFragment : Fragment() {

    private var _b: FragmentNoticiasBinding? = null
    private val b get() = _b!!

    private val noticias = listOf(
        NoticiaUi("Corte de energía en Old Downtown",
            "Parpadeos en la red dejaron sin luz varios bloques. Los generadores privados sostuvieron el Afterlife.",
            "Hace 2 h", "Distrito Energía"),
        NoticiaUi("Intervención de mods en el callejón",
            "Autoridades inspeccionaron tres talleres; sin incidentes mayores.",
            "Ayer", "Seguridad Urbana"),
        NoticiaUi("Nueva línea de metro fantasma",
            "Rumores señalan que un tren experimental usa la Estación Fantasma como depósito.",
            "Hoy", "Foro Subterráneo")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentNoticiasBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = AdapNoticias(noticias) { noticia ->
            mostrarDetalle(noticia)
        }
    }

    private fun mostrarDetalle(n: NoticiaUi) {
        val dialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bs_noticia_detalle, null)
        v.findViewById<TextView>(R.id.titulo).text = n.titulo
        v.findViewById<TextView>(R.id.resumen).text = n.resumen
        v.findViewById<TextView>(R.id.meta).text = "${n.fecha} • ${n.fuente}"
        dialog.setContentView(v)
        dialog.show()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }

    companion object { fun newInstance() = NoticiasFragment() }
}

private class AdapNoticias(
    private val items: List<NoticiaUi>,
    private val onClick: (NoticiaUi) -> Unit
) : RecyclerView.Adapter<AdapNoticias.VH>() {

    inner class VH(val vb: ItemNoticiaBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val vb = ItemNoticiaBinding.inflate(LayoutInflater.from(p.context), p, false)
        return VH(vb)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val it = items[pos]
        h.vb.titulo.text = it.titulo
        h.vb.subtitulo.text = it.resumen
        h.vb.fecha.text = "${it.fecha} • ${it.fuente}"
        h.vb.root.setOnClickListener { onClick(items[h.bindingAdapterPosition]) }
    }

    override fun getItemCount() = items.size
}