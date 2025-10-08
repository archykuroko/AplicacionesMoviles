package com.example.cyberdream.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyberdream.R
import com.example.cyberdream.comun.ThemePrefs

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val dark = ThemePrefs.isDarkMode(this)
        setTheme(if (dark) R.style.Theme_Cyberdream_Dark else R.style.Theme_Cyberdream_Light)
        super.onCreate(savedInstanceState)
    }
}