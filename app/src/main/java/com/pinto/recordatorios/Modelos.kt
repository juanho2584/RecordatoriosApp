package com.pinto.recordatorios

import androidx.compose.ui.graphics.Color

// ---------- ESTADOS Y PRIORIDADES (sealed classes) ----------

sealed class EstadoRecordatorio {
    data object Pendiente : EstadoRecordatorio()
    data object Completado : EstadoRecordatorio()
    data object Vencido : EstadoRecordatorio()
}

sealed class Prioridad {
    data object Alta : Prioridad()
    data object Media : Prioridad()
    data object Baja : Prioridad()
}

// ---------- CATEGORÍA ----------

data class Categoria(
    val id: String,
    val nombre: String,
    val color: Color
)

// ---------- RECORDATORIO (data class inmutable) ----------

data class Recordatorio(
    val id: Long,
    val titulo: String,
    val descripcion: String? = null,   // opcional
    val categoriaId: String,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio,
    val ubicacion: String? = null,     // opcional (asociación con lugar)
    val tieneAviso: Boolean = false
)

// ---------- DATOS SIMULADOS ----------

object DatosSimulados {

    // Map: categoría -> etiqueta/color
    val categoriasPorId: Map<String, Categoria> = mapOf(
        "trabajo" to Categoria("trabajo", "Trabajo", Color(0xFF1976D2)),
        "hogar" to Categoria("hogar", "Hogar", Color(0xFF388E3C)),
        "estudio" to Categoria("estudio", "Estudio", Color(0xFFF57C00)),
        "salud" to Categoria("salud", "Salud", Color(0xFFD32F2F))
    )

    // Set: evita categorías repetidas en la UI
    val idsCategorias: Set<String> = categoriasPorId.keys

    // List: recordatorios simulados
    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            id = 1L,
            titulo = "Entregar informe mensual",
            descripcion = "Revisar métricas antes de enviar",
            categoriaId = "trabajo",
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Pendiente,
            ubicacion = "Oficina central",
            tieneAviso = true
        ),
        Recordatorio(
            id = 2L,
            titulo = "Comprar insumos de limpieza",
            categoriaId = "hogar",
            prioridad = Prioridad.Media,
            estado = EstadoRecordatorio.Pendiente,
            tieneAviso = false
        ),
        Recordatorio(
            id = 3L,
            titulo = "Estudiar Kotlin – tema 3",
            descripcion = "Repasar sealed classes",
            categoriaId = "estudio",
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Completado
        ),
        Recordatorio(
            id = 4L,
            titulo = "Turno con el médico",
            categoriaId = "salud",
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Vencido,
            ubicacion = "Hospital Regional",
            tieneAviso = true
        ),
        Recordatorio(
            id = 5L,
            titulo = "Pagar servicios",
            categoriaId = "hogar",
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Pendiente
        )
    )

    // Filtros (Consigna 3: filtrar según estado o prioridad)
    fun filtrarPorEstado(estado: EstadoRecordatorio): List<Recordatorio> =
        recordatorios.filter { it.estado == estado }

    fun filtrarPorPrioridad(prioridad: Prioridad): List<Recordatorio> =
        recordatorios.filter { it.prioridad == prioridad }

    // Regla de validación (Consigna 2: evitar estados inválidos)
    fun validarRecordatorio(recordatorio: Recordatorio): String? = when {
        recordatorio.titulo.isBlank() -> "El título es obligatorio"
        recordatorio.titulo.trim().length < 4 -> "El título debe tener al menos 4 caracteres"
        recordatorio.categoriaId !in idsCategorias -> "Categoría inexistente"
        else -> null
    }
}