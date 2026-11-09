package com.pinto.recordatorios.data

import com.pinto.recordatorios.DatosSimulados
import com.pinto.recordatorios.Recordatorio
import kotlinx.coroutines.delay

interface RecordatoriosRepository {
    suspend fun obtenerRecordatorios(): List<Recordatorio>
    suspend fun obtenerPorId(id: Long): Recordatorio?
    suspend fun guardar(recordatorio: Recordatorio)
}

class RecordatoriosEnMemoriaRepository : RecordatoriosRepository {
    // Inicializamos la lista con los datos simulados de la Actividad 1
    private val lista = DatosSimulados.recordatorios.toMutableList()

    override suspend fun obtenerRecordatorios(): List<Recordatorio> {
        delay(1500) // Simula tiempo de carga (Consigna 4)

        return lista
    }

    override suspend fun obtenerPorId(id: Long): Recordatorio? {
        delay(500)
        return lista.find { it.id == id }
    }

    override suspend fun guardar(recordatorio: Recordatorio) {
        delay(1000) // Simula tiempo de guardado
        lista.add(recordatorio)
    }
}