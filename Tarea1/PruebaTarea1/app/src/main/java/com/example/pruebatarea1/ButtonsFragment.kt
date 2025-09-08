package com.example.pruebatarea1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ButtonsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.txtTitulo).text = "Botones (Button, ImageButton, FAB)"
        view.findViewById<TextView>(R.id.txtDesc).text = "Permiten disparar acciones. Pueden ser de texto, ícono o flotantes."

        view.findViewById<Button>(R.id.btnNormal).setOnClickListener {
            Toast.makeText(requireContext(), "Button normal presionado", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageButton>(R.id.btnImagen).setOnClickListener {
            Toast.makeText(requireContext(), "ImageButton presionado", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            Toast.makeText(requireContext(), "FAB presionado", Toast.LENGTH_SHORT).show()
        }
    }
}
