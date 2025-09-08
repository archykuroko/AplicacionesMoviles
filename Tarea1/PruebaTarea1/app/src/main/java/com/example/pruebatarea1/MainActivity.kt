package com.example.pruebatarea1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    private fun showFragment(tag: String) {
        val fragment = when (tag) {
            "text" -> TextFieldsFragment()
            "buttons" -> ButtonsFragment()
            "selection" -> SelectionFragment()
            "list" -> ListFragment()
            "info" -> InfoFragment()
            else -> TextFieldsFragment()
        }
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment, tag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajuste de insets para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottom = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_textfields -> showFragment("text")
                R.id.nav_buttons -> showFragment("buttons")
                R.id.nav_selection -> showFragment("selection")
                R.id.nav_list -> showFragment("list")
                R.id.nav_info -> showFragment("info")
            }
            true
        }

        // Fragment inicial
        if (savedInstanceState == null) {
            bottom.selectedItemId = R.id.nav_textfields
        }

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)

        bottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_textfields -> { showFragment("text"); toolbar.title = "TextFields" }
                R.id.nav_buttons    -> { showFragment("buttons"); toolbar.title = "Botones" }
                R.id.nav_selection  -> { showFragment("selection"); toolbar.title = "Selección" }
                R.id.nav_list       -> { showFragment("list"); toolbar.title = "Lista" }
                R.id.nav_info       -> { showFragment("info"); toolbar.title = "Información" }
            }
            true
        }



    }
}
