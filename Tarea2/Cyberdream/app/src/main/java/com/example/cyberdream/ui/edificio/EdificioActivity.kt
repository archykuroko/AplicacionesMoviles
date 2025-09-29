package com.example.cyberdream.ui.edificio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.cyberdream.R
import com.example.cyberdream.comun.setNeonTabsActive
import com.example.cyberdream.databinding.ActivityEdificioBinding
import com.example.cyberdream.comun.applyDynamicBackdrop

class EdificioActivity : AppCompatActivity() {
    private lateinit var b: ActivityEdificioBinding
    companion object {
        const val EXTRA_ID_EDIFICIO = "extra_id_edificio"
        const val EXTRA_TRANSITION_NAME = "extra_transition_name"
    }


    private lateinit var edificioId: String
    private var transitionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportPostponeEnterTransition()
        super.onCreate(savedInstanceState)
        b = ActivityEdificioBinding.inflate(layoutInflater)
        setContentView(b.root)
        val edificioId = intent.getStringExtra(EXTRA_ID_EDIFICIO) ?: "default"
        transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME)
        applyDynamicBackdrop(b.root, key = edificioId, overlayGradient = true)
        // Título y portada
        b.titulo.text = when (edificioId) {
            "afterlife" -> "Afterlife Club"
            "estacion"  -> "Estación Fantasma"
            "mods"      -> "Callejón de Mods"
            else        -> "Edificio"
        }

        val portada = when (edificioId) {
            "afterlife" -> R.drawable.portada_afterlife
            "estacion"  -> R.drawable.portada_estacion
            "mods"      -> R.drawable.portada_mods
            else        -> 0
        }
        if (portada != 0) b.imgHeader.setImageResource(portada)

        // Shared element en el header
        transitionName?.let { ViewCompat.setTransitionName(b.imgHeader, it) }
        supportStartPostponedEnterTransition()

        if (savedInstanceState == null) {
            mostrar(InfoEdificioFragment.newInstance(edificioId, null))
            setNeonTabsActive(b.tabInfo, b.tabGaleria, b.tabPois)
        }

        b.tabInfo.setOnClickListener {
            mostrar(InfoEdificioFragment.newInstance(edificioId, null), desdeIzquierda = true)
            setNeonTabsActive(b.tabInfo, b.tabGaleria, b.tabPois)
        }
        b.tabGaleria.setOnClickListener {
            mostrar(GaleriaEdificioFragment.newInstance())
            setNeonTabsActive(b.tabGaleria, b.tabInfo, b.tabPois)
        }
        b.tabPois.setOnClickListener {
            mostrar(PuntosInteresFragment.newInstance())
            setNeonTabsActive(b.tabPois, b.tabInfo, b.tabGaleria)
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