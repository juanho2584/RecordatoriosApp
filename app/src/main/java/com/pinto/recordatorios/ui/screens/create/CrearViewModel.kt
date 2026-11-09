package com.pinto.recordatorios.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinto.recordatorios.*
import com.pinto.recordatorios.data.RecordatoriosEnMemoriaRepository
import com.pinto.recordatorios.data.RecordatoriosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// CONSIGNA 3: Data class para el estado del formulario
data class CrearUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoriaId: String = "trabajo",
    val prioridad: Prioridad = Prioridad.Media,
    val errorTitulo: String? = null,
    val enviando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val errorGeneral: String? = null
) {
    // CONSIGNA 2: Validaciones
    val esValido: Boolean get() = titulo.trim().length >= 4 && descripcion.length <= 200
}

class CrearViewModel(
    private val repository: RecordatoriosRepository = RecordatoriosEnMemoriaRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(CrearUiState())
    val uiState: StateFlow<CrearUiState> = _uiState.asStateFlow()

    fun onTituloChange(nuevoTitulo: String) {
        val error = if (nuevoTitulo.trim().length in 1..3) "Mínimo 4 caracteres" else null
        _uiState.update { it.copy(titulo = nuevoTitulo, errorTitulo = error) }
    }

    fun onDescripcionChange(nuevaDesc: String) {
        if (nuevaDesc.length <= 200) {
            _uiState.update { it.copy(descripcion = nuevaDesc) }
        }
    }

    fun guardar() {
        if (!_uiState.value.esValido) return

        viewModelScope.launch {
            _uiState.update { it.copy(enviando = true, errorGeneral = null) }
            try {
                val nuevo = Recordatorio(
                    id = System.currentTimeMillis(),
                    titulo = _uiState.value.titulo,
                    descripcion = _uiState.value.descripcion.ifBlank { null },
                    categoriaId = _uiState.value.categoriaId,
                    prioridad = _uiState.value.prioridad,
                    estado = EstadoRecordatorio.Pendiente
                )
                repository.guardar(nuevo)
                _uiState.update { it.copy(enviando = false, guardadoExitoso = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(enviando = false, errorGeneral = "No se pudo guardar") }
            }
        }
    }
}