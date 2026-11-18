package com.pinto.recordatorios.domain

import kotlinx.coroutines.flow.Flow

interface RecordatoriosRepository {
    fun obtenerRecordatoriosFlow(): Flow<List<Recordatorio>>
    suspend fun obtenerPorId(id: Long): Recordatorio?
    suspend fun guardar(recordatorio: Recordatorio)
    suspend fun eliminar(id: Long)
    suspend fun sincronizarPendientes()
}