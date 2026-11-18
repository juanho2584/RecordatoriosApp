package com.pinto.recordatorios.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pinto.recordatorios.domain.Preferencias
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// 1. Instancia ÚNICA a nivel de archivo (top-level)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    // 2. Claves tipadas
    private object Keys {
        val TEMA_OSCURO = booleanPreferencesKey("tema_oscuro")
        val ORDEN = stringPreferencesKey("orden")
        val MOSTRAR_COMPLETADOS = booleanPreferencesKey("mostrar_completados")
    }

    // 3. Transformar los valores en un modelo propio (Flujo de Preferencias)
    val preferenciasFlow: Flow<Preferencias> = dataStore.data
        .catch { throwable ->
            // Si hay error de lectura, devolvemos valores predeterminados (emptyPreferences)
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }
        .map { prefs ->
            Preferencias(
                temaOscuro = prefs[Keys.TEMA_OSCURO] ?: false,
                ordenListado = prefs[Keys.ORDEN] ?: "fecha",
                mostrarCompletados = prefs[Keys.MOSTRAR_COMPLETADOS] ?: true
            )
        }

    // 4. Funciones suspendidas para modificar los valores
    suspend fun setTemaOscuro(oscuro: Boolean) {
        dataStore.edit { it[Keys.TEMA_OSCURO] = oscuro }
    }

    suspend fun setOrden(orden: String) {
        dataStore.edit { it[Keys.ORDEN] = orden }
    }

    suspend fun setMostrarCompletados(mostrar: Boolean) {
        dataStore.edit { it[Keys.MOSTRAR_COMPLETADOS] = mostrar }
    }
}