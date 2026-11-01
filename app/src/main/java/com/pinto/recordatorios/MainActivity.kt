package com.pinto.recordatorios
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PantallaRecordatorios()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Recordatorios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(DatosSimulados.recordatorios) { recordatorio ->
                TarjetaRecordatorio(recordatorio)
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio) {
    val categoria = DatosSimulados.categoriasPorId[recordatorio.categoriaId]

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Título + estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recordatorio.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                EtiquetaEstado(recordatorio.estado)
            }

            // Categoría + prioridad
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                categoria?.let {
                    Box(
                        modifier = Modifier
                            .background(it.color, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = it.nombre,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = "Prioridad: ${etiquetaPrioridad(recordatorio.prioridad)}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Descripción (opcional)
            recordatorio.descripcion?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Ubicación y aviso (opcionales)
            if (recordatorio.ubicacion != null || recordatorio.tieneAviso) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    recordatorio.ubicacion?.let {
                        Text("📍 $it", fontSize = 12.sp)
                    }
                    if (recordatorio.tieneAviso) {
                        Text("🔔 Aviso", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun EtiquetaEstado(estado: EstadoRecordatorio) {
    val (texto, color) = when (estado) {
        EstadoRecordatorio.Pendiente -> "Pendiente" to Color(0xFFF9A825)
        EstadoRecordatorio.Completado -> "Completado" to Color(0xFF2E7D32)
        EstadoRecordatorio.Vencido -> "Vencido" to Color(0xFFC62828)
    }
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = texto, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

fun etiquetaPrioridad(prioridad: Prioridad): String = when (prioridad) {
    Prioridad.Alta -> "Alta"
    Prioridad.Media -> "Media"
    Prioridad.Baja -> "Baja"
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaRecordatoriosPreview() {
    MaterialTheme {
        PantallaRecordatorios()
    }
}