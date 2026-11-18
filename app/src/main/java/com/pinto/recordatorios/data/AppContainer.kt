package com.pinto.recordatorios.data

import android.content.Context
import com.pinto.recordatorios.data.local.RecordatoriosDatabase
import com.pinto.recordatorios.data.local.UserPreferencesRepository
import com.pinto.recordatorios.data.local.dataStore
import com.pinto.recordatorios.data.remote.NetworkModule
import com.pinto.recordatorios.domain.RecordatoriosRepository

class AppContainer(context: Context) {
    // Instanciamos la base de datos
    private val database = RecordatoriosDatabase.getInstance(context)

    // Instanciamos DataStore
    private val prefsRepository = UserPreferencesRepository(context.dataStore)

    // Creamos el repositorio inyectando sus dependencias
    val recordatoriosRepository: RecordatoriosRepository = RecordatoriosRepositoryImpl(
        dao = database.recordatorioDao(),
        api = NetworkModule.api,
        prefs = prefsRepository
    )

    // Exponemos las preferencias por si las necesitamos en un ViewModel
    val userPreferencesRepository: UserPreferencesRepository = prefsRepository
}