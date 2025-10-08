package com.example.cyberdream.ui.ciudad

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.cyberdream.R
import com.example.cyberdream.comun.applyDynamicBackdrop
import com.example.cyberdream.comun.setNeonTabsActive
import com.example.cyberdream.databinding.ActivityCiudadBinding
import com.example.cyberdream.ui.BaseActivity
import com.example.cyberdream.ui.SettingsActivity
import com.example.cyberdream.ui.barrio.BarrioActivity

class CiudadActivity : BaseActivity() {

    private lateinit var b: ActivityCiudadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCiudadBinding.inflate(layoutInflater)
        setContentView(b.root)   // ✅ usa el binding, no el layout id

        // Toolbar para el menú Ajustes
        val toolbar = findViewById<Toolbar>(R.id.toolbarCiudad)
        setSupportActionBar(toolbar)

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

    // Menú para abrir Settings
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ciudad, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
