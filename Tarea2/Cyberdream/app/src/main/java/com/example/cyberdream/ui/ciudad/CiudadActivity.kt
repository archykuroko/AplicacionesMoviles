package com.example.cyberdream.ui.ciudad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyberdream.comun.setNeonTabsActive
import com.example.cyberdream.databinding.ActivityCiudadBinding
import com.example.cyberdream.ui.barrio.BarrioActivity

class CiudadActivity : AppCompatActivity() {

    private lateinit var b: ActivityCiudadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCiudadBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Fragment inicial + tabs activos
        if (savedInstanceState == null) {
            reemplazar(MapaCiudadFragment.newInstance())
            setNeonTabsActive(b.tabMapa, b.tabBarrios, b.tabNoticias)
        }

        // Tabs
        b.tabMapa.setOnClickListener {
            reemplazar(MapaCiudadFragment.newInstance())
            setNeonTabsActive(b.tabMapa, b.tabBarrios, b.tabNoticias)
        }
        b.tabBarrios.setOnClickListener {
            reemplazar(ListaBarriosFragment.newInstance())
            setNeonTabsActive(b.tabBarrios, b.tabMapa, b.tabNoticias)
        }
        b.tabNoticias.setOnClickListener {
            reemplazar(NoticiasFragment.newInstance())
            setNeonTabsActive(b.tabNoticias, b.tabMapa, b.tabBarrios)
        }

        // CTA: entrar a Old Downtown (BarrioActivity)
        b.btnEntrarBarrio.setOnClickListener {
            startActivity(Intent(this, BarrioActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
        }
    }

    private fun reemplazar(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(b.contenedor.id, fragment)
            .commit()
    }
}
