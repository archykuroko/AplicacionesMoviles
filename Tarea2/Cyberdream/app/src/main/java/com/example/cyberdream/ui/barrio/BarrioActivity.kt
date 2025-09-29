package com.example.cyberdream.ui.barrio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyberdream.R
import com.example.cyberdream.comun.setNeonTabsActive
import com.example.cyberdream.databinding.ActivityBarrioBinding
import com.example.cyberdream.comun.applyDynamicBackdrop

class BarrioActivity : AppCompatActivity() {

    private lateinit var b: ActivityBarrioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityBarrioBinding.inflate(layoutInflater)
        setContentView(b.root)
        applyDynamicBackdrop(b.root, key = "old_downtown", overlayGradient = true)

        if (savedInstanceState == null) {
            mostrar(MapaBarrioFragment.newInstance())
            setNeonTabsActive(b.tabMapa, b.tabEdificios, b.tabAmbiente)
        }

        b.tabMapa.setOnClickListener {
            mostrar(MapaBarrioFragment.newInstance(), desdeIzquierda = true)
            setNeonTabsActive(b.tabMapa, b.tabEdificios, b.tabAmbiente)
        }
        b.tabEdificios.setOnClickListener {
            mostrar(ListaEdificiosFragment.newInstance())
            setNeonTabsActive(b.tabEdificios, b.tabMapa, b.tabAmbiente)
        }
        b.tabAmbiente.setOnClickListener {
            mostrar(AmbienteBarrioFragment.newInstance())
            setNeonTabsActive(b.tabAmbiente, b.tabMapa, b.tabEdificios)
        }
    }

    private fun mostrar(fragment: androidx.fragment.app.Fragment, desdeIzquierda: Boolean = false) {
        val enter = if (desdeIzquierda) R.anim.fragment_slide_in_left else R.anim.fragment_slide_in_right
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enter, R.anim.fragment_fade_out, 0, 0)
            .replace(b.contenedor.id, fragment)
            .commit()
    }


}