package com.example.cyberdream.comun

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.cyberdream.R
import kotlin.math.abs


fun applyDynamicBackdrop(
    root: View,
    key: String? = null,
    overlayGradient: Boolean = true,
    alphaOnImage: Int = 255
) {
    val pool = listOf(
        R.drawable.bg_cyberpunk_1,
        R.drawable.bg_cyberpunk_2,
        R.drawable.bg_cyberpunk_3,
        R.drawable.bg_cyberpunk_4
    )

    // Elige índice
    val index = if (key.isNullOrEmpty()) {
        (pool.indices).random()
    } else {
        abs(key.hashCode()) % pool.size
    }

    val ctx = root.context
    val img = (ContextCompat.getDrawable(ctx, pool[index]) as Drawable).mutate()
    if (img is BitmapDrawable) img.alpha = alphaOnImage

    if (overlayGradient) {
        val gradient = ContextCompat.getDrawable(ctx, R.drawable.bg_cyber_alt)!! // o bg_cyber_gradient
        val layers = arrayOf(gradient, img)
        root.background = LayerDrawable(layers)
    } else {
        root.background = img
    }
}
