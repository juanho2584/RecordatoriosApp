package com.pinto.recordatorios.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecordatorioScreen(
    id: Long,
    onVolver: () -> Unit
) {
    // Como necesitamos pasarle el ID al ViewModel, usamos una Factory personalizada
    val viewModel: DetalleViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetalleViewModel(id) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Recordatorio") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.cargando) {
                CircularProgressIndicator()
            } else if (uiState.recordatorio != null) {
                val rec = uiState.recordatorio!!
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = rec.titulo, style = MaterialTheme.typography.headlineMedium)
                    Text(text = "Categoría: ${rec.categoriaId}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Prioridad: ${rec.prioridad}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Estado: ${rec.estado}", style = MaterialTheme.typography.bodyLarge)

                    rec.descripcion?.let {
                        Text(text = "Descripción: $it", style = MaterialTheme.typography.bodyMedium)
                    }

                    rec.ubicacion?.let {
                        Text(text = "Ubicación: $it", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                Text("Recordatorio no encontrado")
            }
        }
    }
}