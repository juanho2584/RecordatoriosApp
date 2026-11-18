package com.pinto.recordatorios.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recordatorios")
data class RecordatorioEntity(
    @PrimaryKey val id: Long,
    val titulo: String,
    val descripcion: String?,
    val categoria: String,
    val prioridad: String,
    val estado: String,
    val fecha: Long,
    val sincronizado: Boolean = false,       // false = pendiente de subir al servidor
    val pendienteEliminacion: Boolean = false // true = hay que borrarlo en el servidor
)