package com.example.pruebatarea1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast

class TextFieldsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_textfields, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val titulo = view.findViewById<TextView>(R.id.txtTitulo)
        val desc = view.findViewById<TextView>(R.id.txtDesc)
        val edt = view.findViewById<EditText>(R.id.edtNombre)
        val btn = view.findViewById<Button>(R.id.btnMostrar)
        val salida = view.findViewById<TextView>(R.id.txtSalida)

        titulo.text = "EditText (TextFields)"
        desc.text = "Permiten ingresar texto del usuario. Útiles para capturar datos como nombre, correo, etc."

        btn.setOnClickListener {
            val nombre = edt.text.toString().trim()
            val correo = view.findViewById<EditText>(R.id.edtCorreo).text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Escribe tu nombre", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(requireContext(), "Correo inválido", Toast.LENGTH_SHORT).show()
            } else {
                salida.text = "Hola $nombre\nCorreo: $correo"
            }
        }
    }
}
