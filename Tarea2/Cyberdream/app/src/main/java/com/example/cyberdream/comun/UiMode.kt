package com.example.cyberdream.comun

import android.content.Context
import android.content.res.Configuration

fun Context.isNightMode(): Boolean {
    val mask = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return mask == Configuration.UI_MODE_NIGHT_YES
}
