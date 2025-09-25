package com.example.cyberdream.ui.barrio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberdream.data.DatosDemo
import com.example.cyberdream.data.Edificio
import com.example.cyberdream.databinding.FragmentListaEdificiosBinding
import com.example.cyberdream.databinding.ItemEdificioBinding
import com.example.cyberdream.ui.edificio.EdificioActivity

class ListaEdificiosFragment : Fragment() {

    private var _b: FragmentListaEdificiosBinding? = null
    private val b get() = _b!!

    private val edificios: List<Edificio> = DatosDemo.oldDowntown.edificios

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentListaEdificiosBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = AdaptadorEdificios(edificios) { edificio, imgView ->
            // transitionName único por edificio
            val transitionName = "building_image_${edificio.id}"
            ViewCompat.setTransitionName(imgView, transitionName)

            val intent = Intent(requireContext(), EdificioActivity::class.java).apply {
                putExtra(EdificioActivity.EXTRA_ID_EDIFICIO, edificio.id)
                putExtra(EdificioActivity.EXTRA_TRANSITION_NAME, transitionName)
            }

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                Pair(imgView as View, transitionName)
            )
            startActivity(intent, options.toBundle())
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }

    companion object { fun newInstance() = ListaEdificiosFragment() }
}

class AdaptadorEdificios(
    private val items: List<Edificio>,
    private val onClick: (Edificio, View) -> Unit
) : RecyclerView.Adapter<AdaptadorEdificios.VH>() {

    inner class VH(val vb: ItemEdificioBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = ItemEdificioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(vb)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = items[position]
        holder.vb.titulo.text = e.nombre
        holder.vb.subtitulo.text = e.descripcion

        // Puedes setear una imagen distinta por edificio si quieres
        // holder.vb.img.setImageResource(...)

        holder.vb.root.setOnClickListener {
            onClick(e, holder.vb.img)
        }
    }

    override fun getItemCount() = items.size
}
