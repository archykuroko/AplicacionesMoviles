package com.example.cyberdream.ui.edificio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyberdream.comun.setNeonTabsActive
import com.example.cyberdream.databinding.ActivityEdificioBinding

class EdificioActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID_EDIFICIO = "extra_id_edificio"
        const val EXTRA_TRANSITION_NAME = "extra_transition_name" // 🔸 nuevo
    }

    private lateinit var b: ActivityEdificioBinding
    private lateinit var edificioId: String
    private var transitionName: String? = null // 🔸 nuevo

    override fun onCreate(savedInstanceState: Bundle?) {
        // 🔸 aplaza la transición hasta que el destino esté listo
        supportPostponeEnterTransition()

        super.onCreate(savedInstanceState)
        b = ActivityEdificioBinding.inflate(layoutInflater)
        setContentView(b.root)

        edificioId = intent.getStringExtra(EXTRA_ID_EDIFICIO) ?: "afterlife"
        transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME)

        b.titulo.text = when (edificioId) {
            "afterlife" -> "Afterlife Club"
            "estacion"  -> "Estación Fantasma"
            "mods"      -> "Callejón de Mods"
            else        -> "Edificio"
        }

        if (savedInstanceState == null) {
            // pasa también el transitionName al fragment
            reemplazar(InfoEdificioFragment.newInstance(edificioId, transitionName))
            setNeonTabsActive(b.tabInfo, b.tabGaleria, b.tabPois)
        }

        b.tabInfo.setOnClickListener {
            reemplazar(InfoEdificioFragment.newInstance(edificioId, transitionName))
            setNeonTabsActive(b.tabInfo, b.tabGaleria, b.tabPois)
        }
        b.tabGaleria.setOnClickListener {
            reemplazar(GaleriaEdificioFragment.newInstance(edificioId))
            setNeonTabsActive(b.tabGaleria, b.tabInfo, b.tabPois)
        }
        b.tabPois.setOnClickListener {
            reemplazar(PuntosInteresFragment.newInstance(edificioId))
            setNeonTabsActive(b.tabPois, b.tabInfo, b.tabGaleria)
        }
    }

    private fun reemplazar(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(b.contenedor.id, fragment)
            .commit()
    }
}
