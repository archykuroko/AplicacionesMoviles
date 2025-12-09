package ste.archykuroko.aguapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "water_prefs")

object WaterDataStore {
    private val VASOS_KEY = intPreferencesKey("vasos")

    fun getVasos(context: Context): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[VASOS_KEY] ?: 0
        }
    }

    suspend fun saveVasos(context: Context, value: Int) {
        context.dataStore.edit { prefs ->
            prefs[VASOS_KEY] = value
        }
    }
}
