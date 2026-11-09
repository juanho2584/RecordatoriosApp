package com.pinto.recordatorios.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinto.recordatorios.Recordatorio
import com.pinto.recordatorios.data.RecordatoriosEnMemoriaRepository
import com.pinto.recordatorios.data.RecordatoriosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUiState(
    val recordatorio: Recordatorio? = null,
    val cargando: Boolean = true
)

class DetalleViewModel(
    private val id: Long,
    private val repository: RecordatoriosRepository = RecordatoriosEnMemoriaRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val rec = repository.obtenerPorId(id)
            _uiState.value = DetalleUiState(recordatorio = rec, cargando = false)
        }
    }
}