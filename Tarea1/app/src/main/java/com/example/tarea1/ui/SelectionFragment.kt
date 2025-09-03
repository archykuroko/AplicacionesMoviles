package com.example.tarea1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.tarea1.R

class SelectionFragment : Fragment() {

    private lateinit var cbA: CheckBox
    private lateinit var cbB: CheckBox
    private lateinit var rg: RadioGroup
    private lateinit var sw: Switch
    private lateinit var tvResumen: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cbA = view.findViewById(R.id.cbA)
        cbB = view.findViewById(R.id.cbB)
        rg = view.findViewById(R.id.rg)
        sw = view.findViewById(R.id.sw)
        tvResumen = view.findViewById(R.id.tvResumen)

        val update = { actualizarResumen() }
        cbA.setOnCheckedChangeListener { _, _ -> update() }
        cbB.setOnCheckedChangeListener { _, _ -> update() }
        rg.setOnCheckedChangeListener { _, _ -> update() }
        sw.setOnCheckedChangeListener { _, _ -> update() }
        actualizarResumen()
    }

    private fun actualizarResumen() {
        val checks = listOf(cbA, cbB).filter { it.isChecked }.joinToString { it.text.toString() }
        val rbSel = rg.children.filterIsInstance<RadioButton>().firstOrNull { it.isChecked }?.text ?: "Ninguno"
        val swTxt = if (sw.isChecked) "ON" else "OFF"
        tvResumen.text = "Resumen: CheckBox=[$checks], Radio=$rbSel, Switch=$swTxt"
    }
}
