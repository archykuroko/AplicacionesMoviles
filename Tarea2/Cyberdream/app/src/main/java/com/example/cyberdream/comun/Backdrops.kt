package com.example.cyberdream.comun

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.cyberdream.R
import kotlin.math.abs

// --- Helpers ---
private fun View.isNightMode(): Boolean {
    val mask = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return mask == Configuration.UI_MODE_NIGHT_YES
}

private fun View.resolveDrawableId(name: String): Int =
    context.resources.getIdentifier(name, "drawable", context.packageName)

fun applyDynamicBackdrop(
    root: View,
    key: String? = null,
    overlayGradient: Boolean = true,
    alphaOnImage: Int = 255
) {
    val ctx = root.context
    val isNight = root.isNightMode()


    val baseNames = listOf(
        "bg_cyberpunk_1",
        "bg_cyberpunk_2",
        "bg_cyberpunk_3",
        "bg_cyberpunk_4"
    )

    // Construye el pool resolviendo primero *_light/_dark y, si no existen, el base
    val pool: List<Int> = baseNames.mapNotNull { base ->
        val themed = base + if (isNight) "_dark" else "_light"
        val themedId = root.resolveDrawableId(themed)
        val id = if (themedId != 0) themedId else root.resolveDrawableId(base)
        if (id != 0) id else null
    }.ifEmpty {
        // Fallback a los drawables tipados
        listOf(
            R.drawable.bg_cyberpunk_1,
            R.drawable.bg_cyberpunk_2,
            R.drawable.bg_cyberpunk_3,
            R.drawable.bg_cyberpunk_4
        )
    }

    // Para que cambie entre claro/oscuro aunque la key sea la misma
    val seed = (key ?: "default") + if (isNight) "_dark" else "_light"
    val index = abs(seed.hashCode()) % pool.size

    val image = ContextCompat.getDrawable(ctx, pool[index])!!.mutate().apply {
        alpha = alphaOnImage
    }

    if (!overlayGradient) {
        root.background = image
        return
    }


    val gradient = ContextCompat.getDrawable(ctx, R.drawable.bg_cyber_alt)!!.mutate()


    root.background = LayerDrawable(arrayOf<Drawable>(image, gradient))
}