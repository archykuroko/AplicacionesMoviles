package com.example.cyberdream.ui

import android.os.Bundle
import com.example.cyberdream.R
import com.example.cyberdream.comun.ThemePrefs
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val swDark = findViewById<MaterialSwitch>(R.id.swDarkMode)

        // Cargar estado guardado
        swDark.isChecked = ThemePrefs.isDarkMode(this)

        // Guardar y aplicar
        swDark.setOnCheckedChangeListener { _, isChecked ->
            ThemePrefs.setDarkMode(this, isChecked)
            recreate() // aplica en esta activity de inmediato
        }
    }
}
