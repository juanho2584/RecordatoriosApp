package com.pinto.recordatorios.ui.screens.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRecordatorioScreen(
    viewModel: CrearViewModel = viewModel(),
    onGuardado: () -> Unit,
    onCancelar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Si el guardado es exitoso, avisamos para que navegue hacia atrás
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) onGuardado()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Recordatorio") },
                navigationIcon = {
                    TextButton(onClick = onCancelar) { Text("Cancelar") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo de Título con validación
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = viewModel::onTituloChange,
                label = { Text("Título") },
                isError = uiState.errorTitulo != null,
                supportingText = { uiState.errorTitulo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de Descripción
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = { Text("Descripción (máx 200 caracteres)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón de Guardar
            Button(
                onClick = viewModel::guardar,
                enabled = uiState.esValido && !uiState.enviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar Recordatorio")
                }
            }
        }
    }
}