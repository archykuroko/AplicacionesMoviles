package com.example.actividad1.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object PrefKeys {
    val TREE_URI = stringPreferencesKey("tree_uri")
    val THEME = stringPreferencesKey("theme") // "GUINDA" | "AZUL"
    val VIEW_MODE = stringPreferencesKey("view_mode") // "LIST" | "GRID"
    val RECENTS = stringSetPreferencesKey("recents") // set de URIs
    val FAVORITES = stringSetPreferencesKey("favorites")
}