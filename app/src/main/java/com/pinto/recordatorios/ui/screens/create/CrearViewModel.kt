package com.pinto.recordatorios.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinto.recordatorios.domain.EstadoRecordatorio
import com.pinto.recordatorios.domain.Prioridad
import com.pinto.recordatorios.domain.Recordatorio
import com.pinto.recordatorios.domain.RecordatoriosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val esValido: Boolean
        get() = titulo.trim().length >= 4 && descripcion.length <= 200
}

class CrearViewModel(
    private val repository: RecordatoriosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearUiState())
    val uiState: StateFlow<CrearUiState> = _uiState.asStateFlow()

    fun onTituloChange(nuevo: String) {
        val error = if (nuevo.trim().length in 1..3) "Mínimo 4 caracteres" else null
        _uiState.update { it.copy(titulo = nuevo, errorTitulo = error) }
    }

    fun onDescripcionChange(nueva: String) {
        if (nueva.length <= 200) _uiState.update { it.copy(descripcion = nueva) }
    }

    fun onCategoriaChange(id: String) = _uiState.update { it.copy(categoriaId = id) }
    fun onPrioridadChange(p: Prioridad) = _uiState.update { it.copy(prioridad = p) }

    fun guardar() {
        if (!_uiState.value.esValido) return
        viewModelScope.launch {
            _uiState.update { it.copy(enviando = true, errorGeneral = null) }
            try {
                val nuevo = Recordatorio(
                    id = System.currentTimeMillis(), // ID único temporal
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