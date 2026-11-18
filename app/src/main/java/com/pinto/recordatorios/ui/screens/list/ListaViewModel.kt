package com.pinto.recordatorios.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinto.recordatorios.domain.Recordatorio
import com.pinto.recordatorios.domain.RecordatoriosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ListaUiState {
    data object Cargando : ListaUiState
    data object Vacio : ListaUiState
    data class Exito(val recordatorios: List<Recordatorio>) : ListaUiState
    data class Error(val mensaje: String) : ListaUiState
}

class ListaViewModel(
    private val repository: RecordatoriosRepository
) : ViewModel() {

    // Conectamos directamente el Flow de Room (a través del repositorio) al StateFlow de la UI
    val uiState: StateFlow<ListaUiState> = repository.obtenerRecordatoriosFlow()
        .map { lista ->
            if (lista.isEmpty()) {
                ListaUiState.Vacio as ListaUiState
            } else {
                ListaUiState.Exito(lista) as ListaUiState
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ListaUiState.Cargando
        )

    init {
        // Al iniciar la pantalla, intentamos sincronizar lo que haya quedado pendiente
        viewModelScope.launch {
            runCatching { repository.sincronizarPendientes() }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch { repository.eliminar(id) }
    }
}