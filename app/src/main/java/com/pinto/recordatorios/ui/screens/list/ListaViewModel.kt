package com.pinto.recordatorios.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinto.recordatorios.Recordatorio
import com.pinto.recordatorios.data.RecordatoriosEnMemoriaRepository
import com.pinto.recordatorios.data.RecordatoriosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// CONSIGNA 3: UiState tipado con sealed interface (estados mutuamente excluyentes)
sealed interface ListaUiState {
    data object Cargando : ListaUiState
    data object Vacio : ListaUiState
    data class Exito(val recordatorios: List<Recordatorio>) : ListaUiState
    data class Error(val mensaje: String) : ListaUiState
}

class ListaViewModel(
    private val repository: RecordatoriosRepository = RecordatoriosEnMemoriaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaUiState>(ListaUiState.Cargando)
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    init {
        cargarRecordatorios()
    }

    fun cargarRecordatorios() {
        viewModelScope.launch {
            _uiState.value = ListaUiState.Cargando
            try {
                val lista = repository.obtenerRecordatorios()
                _uiState.value = if (lista.isEmpty()) ListaUiState.Vacio else ListaUiState.Exito(lista)
            } catch (e: Exception) {
                _uiState.value = ListaUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}