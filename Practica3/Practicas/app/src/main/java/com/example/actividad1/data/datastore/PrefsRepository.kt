package com.example.actividad1.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore único para toda la app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PrefsRepository(private val context: Context) {

    companion object {
        private val KEY_THEME = stringPreferencesKey("theme")              // "guinda" | "azul"
        private val KEY_TREE_URI = stringPreferencesKey("tree_uri")        // Uri del SAF seleccionado
        private val KEY_FAVORITES = stringSetPreferencesKey("favorites")   // URIs favoritos
    }

    /* ---------- Tema ---------- */
    val themeFlow: Flow<String> = context.dataStore.data
        .map { it[KEY_THEME] ?: "guinda" }

    suspend fun setTheme(value: String) {
        context.dataStore.edit { it[KEY_THEME] = value }
    }

    /* ---------- SAF ---------- */
    val treeUriFlow: Flow<String?> = context.dataStore.data
        .map { it[KEY_TREE_URI] }

    suspend fun setTreeUri(uri: String) {
        context.dataStore.edit { it[KEY_TREE_URI] = uri }
    }

    /* ---------- Favoritos ---------- */
    val favoritesFlow: Flow<Set<String>> = context.dataStore.data
        .map { it[KEY_FAVORITES] ?: emptySet() }

    suspend fun toggleFavorite(uri: String) {
        context.dataStore.edit { p ->
            val set = p[KEY_FAVORITES]?.toMutableSet() ?: mutableSetOf()
            if (!set.add(uri)) set.remove(uri)
            p[KEY_FAVORITES] = set
        }
    }
}
