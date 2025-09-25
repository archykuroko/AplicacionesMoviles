package com.example.cyberdream.comun

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.cyberdream.R
import com.google.android.material.button.MaterialButton

/** Deja [active] como píldora rellena y [inactive] como outlined. */
fun setNeonTabsActive(active: MaterialButton, vararg inactive: MaterialButton) {
    val ctx = active.context
    val cyan = ContextCompat.getColor(ctx, R.color.neon_cyan)
    val bg = ContextCompat.getColor(ctx, R.color.bg_oscuro)

    // Activo -> relleno
    active.backgroundTintList = ColorStateList.valueOf(cyan)
    active.setTextColor(bg)
    active.strokeWidth = 0

    // Inactivos -> outlined
    val strokePx = ctx.resources.getDimensionPixelSize(R.dimen.cyber_stroke_2)
    inactive.forEach { b ->
        b.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        b.setTextColor(cyan)
        b.strokeColor = ColorStateList.valueOf(cyan)
        b.strokeWidth = strokePx
    }
}
