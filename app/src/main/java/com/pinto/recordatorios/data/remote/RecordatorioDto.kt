package com.pinto.recordatorios.data.remote

// Representa el contrato del servicio (lo que viaja por JSON)
data class RecordatorioDto(
    val id: Long? = null,
    val title: String,
    val body: String? = null
)