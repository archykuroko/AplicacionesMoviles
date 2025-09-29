package com.example.cyberdream.ui.ciudad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyberdream.R
import com.example.cyberdream.comun.setNeonTabsActive
import com.example.cyberdream.databinding.ActivityCiudadBinding
import com.example.cyberdream.ui.barrio.BarrioActivity
import com.example.cyberdream.comun.applyDynamicBackdrop

class CiudadActivity : AppCompatActivity() {

    private lateinit var b: ActivityCiudadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCiudadBinding.inflate(layoutInflater)
        setContentView(b.root)
        applyDynamicBackdrop(b.root, key = "ciudad", overlayGradient = true)

        if (savedInstanceState == null) {
            mostrar(MapaCiudadFragment.newInstance())
            setNeonTabsActive(b.tabMapa, b.tabBarrios, b.tabNoticias)
        }

        // Tabs
        b.tabMapa.setOnClickListener {
            mostrar(MapaCiudadFragment.newInstance(), desdeIzquierda = true)
            setNeonTabsActive(b.tabMapa, b.tabBarrios, b.tabNoticias)
        }
        b.tabBarrios.setOnClickListener {
            mostrar(ListaBarriosFragment.newInstance())
            setNeonTabsActive(b.tabBarrios, b.tabMapa, b.tabNoticias)
        }
        b.tabNoticias.setOnClickListener {
            mostrar(NoticiasFragment.newInstance())
            setNeonTabsActive(b.tabNoticias, b.tabMapa, b.tabBarrios)
        }


        b.btnEntrarBarrio.setOnClickListener {
            startActivity(Intent(this, BarrioActivity::class.java))
            overridePendingTransition(R.anim.act_slide_in_right, R.anim.act_fade_out)
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
