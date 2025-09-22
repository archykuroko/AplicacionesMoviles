package com.example.pruebatarea1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {

    private val datos = listOf(
        "Elemento 1", "Elemento 2", "Elemento 3",
        "Elemento 4", "Elemento 5", "Elemento 6"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.txtTitulo).text = "Listas (RecyclerView)"
        view.findViewById<TextView>(R.id.txtDesc).text = "Muestran colecciones de ítems de forma eficiente. Permiten scroll y vistas recicladas."

        val rv = view.findViewById<RecyclerView>(R.id.recycler)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = SimpleAdapter(datos) { item ->
            Toast.makeText(requireContext(), "Click: $item", Toast.LENGTH_SHORT).show()
        }
    }

    private class SimpleAdapter(
        val items: List<String>,
        val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<SimpleAdapter.VH>() {

        inner class VH(val v: View) : RecyclerView.ViewHolder(v) {
            val tv: TextView = v.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.tv.text = item
            holder.v.setOnClickListener { onClick(item) }
        }

        override fun getItemCount(): Int = items.size
    }
}
