package com.example.tarea1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tarea1.R

class TextFieldsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_text_fields, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etUno = view.findViewById<EditText>(R.id.etUno)
        val etDos = view.findViewById<EditText>(R.id.etDos)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val msg = "${etUno.text} — ${etDos.text}"
            Toast.makeText(requireContext(), if (msg.isBlank()) "Escribe algo" else msg, Toast.LENGTH_SHORT).show()
        }
    }
}
