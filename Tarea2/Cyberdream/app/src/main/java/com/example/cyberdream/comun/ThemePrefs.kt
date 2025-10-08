package com.example.cyberdream.comun

import android.content.Context
import android.content.SharedPreferences

object ThemePrefs {
    private const val FILE = "theme_prefs"
    private const val KEY_DARK_MODE = "dark_mode" // true = oscuro, false = claro

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun isDarkMode(ctx: Context): Boolean =
        prefs(ctx).getBoolean(KEY_DARK_MODE, true) // oscuro por defecto

    fun setDarkMode(ctx: Context, enabled: Boolean) {
        prefs(ctx).edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }
}
