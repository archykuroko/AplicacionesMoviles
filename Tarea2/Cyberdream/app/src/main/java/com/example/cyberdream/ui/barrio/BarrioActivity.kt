package com.example.cyberdream.ui.barrio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyberdream.comun.setNeonTabsActive
import com.example.cyberdream.databinding.ActivityBarrioBinding

class BarrioActivity : AppCompatActivity() {

    private lateinit var b: ActivityBarrioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityBarrioBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Fragment inicial + tab activo
        if (savedInstanceState == null) {
            reemplazar(MapaBarrioFragment.newInstance())
            setNeonTabsActive(b.tabMapa, b.tabEdificios, b.tabAmbiente)
        }

        // Tabs
        b.tabMapa.setOnClickListener {
            reemplazar(MapaBarrioFragment.newInstance())
            setNeonTabsActive(b.tabMapa, b.tabEdificios, b.tabAmbiente)
        }
        b.tabEdificios.setOnClickListener {
            reemplazar(ListaEdificiosFragment.newInstance())
            setNeonTabsActive(b.tabEdificios, b.tabMapa, b.tabAmbiente)
        }
        b.tabAmbiente.setOnClickListener {
            reemplazar(AmbienteBarrioFragment.newInstance())
            setNeonTabsActive(b.tabAmbiente, b.tabMapa, b.tabEdificios)
        }
    }

    private fun reemplazar(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(b.contenedor.id, fragment)
            .commit()
    }
}
