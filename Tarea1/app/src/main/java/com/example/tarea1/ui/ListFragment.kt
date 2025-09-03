package com.example.tarea1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tarea1.R

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listView = view.findViewById<ListView>(R.id.listView)
        val datos = mutableListOf("Elemento 1", "Elemento 2", "Elemento 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, datos)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(requireContext(), "Click en ${datos[position]}", Toast.LENGTH_SHORT).show()
        }
        listView.setOnItemLongClickListener { _, _, position, _ ->
            datos.removeAt(position); adapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Eliminado", Toast.LENGTH_SHORT).show()
            true
        }
    }
}
