package com.pinto.recordatorios.ui.screens.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinto.recordatorios.ui.components.EstadoError
import com.pinto.recordatorios.ui.components.EstadoVacio
import com.pinto.recordatorios.ui.components.TarjetaRecordatorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRecordatoriosScreen(
    viewModel: ListaViewModel = viewModel(),
    onCrearClick: () -> Unit,
    onRecordatorioClick: (Long) -> Unit
) {
    // CONSIGNA 4: Recolección consciente del ciclo de vida
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Recordatorios") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // CONSIGNA 3: When exhaustivo
            when (val estado = uiState) {
                is ListaUiState.Cargando -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is ListaUiState.Vacio -> EstadoVacio(onCrear = onCrearClick)
                is ListaUiState.Error -> EstadoError(mensaje = estado.mensaje, onReintentar = { viewModel.cargarRecordatorios() })
                is ListaUiState.Exito -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estado.recordatorios, key = { it.id }) { recordatorio ->
                            TarjetaRecordatorio(
                                recordatorio = recordatorio,
                                onClick = { onRecordatorioClick(recordatorio.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}