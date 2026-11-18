package com.pinto.recordatorios.data.mappers

import com.pinto.recordatorios.data.local.RecordatorioEntity
import com.pinto.recordatorios.data.remote.RecordatorioDto
import com.pinto.recordatorios.domain.EstadoRecordatorio
import com.pinto.recordatorios.domain.Prioridad
import com.pinto.recordatorios.domain.Recordatorio

// 1. Entidad (Room) -> Dominio (UI)
fun RecordatorioEntity.toDomain(): Recordatorio = Recordatorio(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    categoriaId = categoria,
    prioridad = when (prioridad) {
        "Alta" -> Prioridad.Alta
        "Baja" -> Prioridad.Baja
        else -> Prioridad.Media
    },
    estado = when (estado) {
        "Completado" -> EstadoRecordatorio.Completado
        "Vencido" -> EstadoRecordatorio.Vencido
        else -> EstadoRecordatorio.Pendiente
    },
    fecha = fecha
)

// 2. Dominio (UI) -> Entidad (Room)
fun Recordatorio.toEntity(sincronizado: Boolean = false): RecordatorioEntity = RecordatorioEntity(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    categoria = categoriaId,
    prioridad = when (prioridad) {
        is Prioridad.Alta -> "Alta"
        is Prioridad.Media -> "Media"
        is Prioridad.Baja -> "Baja"
    },
    estado = when (estado) {
        is EstadoRecordatorio.Completado -> "Completado"
        is EstadoRecordatorio.Vencido -> "Vencido"
        is EstadoRecordatorio.Pendiente -> "Pendiente"
    },
    fecha = fecha,
    sincronizado = sincronizado
)

// 3. DTO (Servidor) -> Entidad (Room)
fun RecordatorioDto.toEntity(): RecordatorioEntity = RecordatorioEntity(
    id = id ?: System.currentTimeMillis(),
    titulo = title,
    descripcion = body,
    categoria = "general",
    prioridad = "Media",
    estado = "Pendiente",
    fecha = System.currentTimeMillis(),
    sincronizado = true
)

// 4. Dominio (UI) -> DTO (Servidor)
fun Recordatorio.toDto(): RecordatorioDto = RecordatorioDto(
    id = id,
    title = titulo,
    body = descripcion
)