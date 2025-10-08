package com.example.cyberdream.comun

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
    alphaOnImage: Int = 255,          // opacidad de la imagen
    overlayAlphaOnTop: Int = -1       // opacidad del gradiente (0-255). -1 = auto
) {
    val ctx = root.context
    val isNight = ctx.isNightMode()


    val baseNames = listOf(
        "bg_cyberpunk_1",
        "bg_cyberpunk_2",
        "bg_cyberpunk_3",
        "bg_cyberpunk_4"
    )

    fun resId(name: String): Int =
        ctx.resources.getIdentifier(name, "drawable", ctx.packageName)


    val themedIds: List<Int> = baseNames.mapNotNull { base ->
        val themedName = base + if (isNight) "_dark" else "_light"
        val idThemed = resId(themedName)
        val id = if (idThemed != 0) idThemed else resId(base)
        if (id != 0) id else null
    }


    val pool = if (themedIdsNotEmpty(themedIds)) themedIds else listOf(
        R.drawable.bg_cyberpunk_1,
        R.drawable.bg_cyberpunk_2,
        R.drawable.bg_cyberpunk_3,
        R.drawable.bg_cyberpunk_4
    )

    // Para que cambie entre claro/oscuro, el "seed" incluye el modo
    val effectiveKey = (key ?: "default") + if (isNight) "_dark" else "_light"
    val index = abs(effectiveKey.hashCode()) % pool.size

    val img = ContextCompat.getDrawable(ctx, pool[index])!!.mutate().apply {
        alpha = alphaOnImage
    }

    if (!overlayGradient) {
        root.background = img
        return
    }

    // Gradiente por tema
    val gradName = if (isNight) "bg_cyber_alt_dark" else "bg_cyber_alt_light"
    val gradRes = resId(gradName).takeIf { it != 0 } ?: R.drawable.bg_cyber_alt
    val gradient = ContextCompat.getDrawable(ctx, gradRes)!!.mutate().apply {
        alpha = if (overlayAlphaOnTop >= 0) {
            overlayAlphaOnTop
        } else {
            if (isNight) 80 /*~31%*/ else 36 /*~14%*/
        }
    }


    root.background = LayerDrawable(arrayOf<Drawable>(img, gradient))
}

private fun themedIdsNotEmpty(list: List<Int>) = list.isNotEmpty()
