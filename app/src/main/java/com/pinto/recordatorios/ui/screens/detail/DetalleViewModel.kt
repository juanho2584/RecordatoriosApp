package com.pinto.recordatorios.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinto.recordatorios.domain.Recordatorio
import com.pinto.recordatorios.domain.RecordatoriosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUiState(
    val recordatorio: Recordatorio? = null,
    val cargando: Boolean = true
)

class DetalleViewModel(
    private val repository: RecordatoriosRepository,
    private val id: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val r = repository.obtenerPorId(id)
            _uiState.value = DetalleUiState(recordatorio = r, cargando = false)
        }
    }
}