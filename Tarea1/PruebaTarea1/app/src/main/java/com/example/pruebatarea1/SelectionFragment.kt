package com.example.pruebatarea1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class SelectionFragment : Fragment() {

    private fun updateResumen(view: View) {
        val chkA = view.findViewById<CheckBox>(R.id.chkA).isChecked
        val chkB = view.findViewById<CheckBox>(R.id.chkB).isChecked
        val switchX = view.findViewById<Switch>(R.id.switchX).isChecked

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val selectedId = radioGroup.checkedRadioButtonId
        val radioSel = if (selectedId != -1) view.findViewById<RadioButton>(selectedId).text else "Ninguno"

        val resumen = view.findViewById<TextView>(R.id.txtResumen)
        resumen.text = "CheckBox: A=${chkA}, B=${chkB} | Radio: $radioSel | Switch: ${if (switchX) "ON" else "OFF"}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.txtTitulo).text = "Elementos de selección"
        view.findViewById<TextView>(R.id.txtDesc).text = "Permiten elegir opciones: múltiples, únicas o activar/desactivar estados."

        val listener = View.OnClickListener { updateResumen(view) }

        view.findViewById<CheckBox>(R.id.chkA).setOnClickListener(listener)
        view.findViewById<CheckBox>(R.id.chkB).setOnClickListener(listener)
        view.findViewById<Switch>(R.id.switchX).setOnClickListener(listener)
        view.findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { _, _ -> updateResumen(view) }

        updateResumen(view)
    }
}
