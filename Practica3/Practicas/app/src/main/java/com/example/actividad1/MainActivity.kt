package com.example.actividad1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.actividad1.data.datastore.PrefsRepository
import com.example.actividad1.ui.navigation.AppNavGraph
import com.example.actividad1.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PrefsRepository(applicationContext)

        setContent {
            AppTheme(prefs = prefs) {
                val nav = rememberNavController()
                AppNavGraph(nav = nav)
            }
        }
    }
}
