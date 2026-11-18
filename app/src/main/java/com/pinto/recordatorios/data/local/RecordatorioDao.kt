package com.pinto.recordatorios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordatorioDao {

    // La UI observa este Flow. Filtra los que están pendientes de eliminar.
    @Query("SELECT * FROM recordatorios WHERE pendienteEliminacion = 0 ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<RecordatorioEntity>>

    @Query("SELECT * FROM recordatorios WHERE id = :id AND pendienteEliminacion = 0")
    suspend fun obtenerPorId(id: Long): RecordatorioEntity?

    // Filtro requerido por la consigna (por estado)
    @Query("SELECT * FROM recordatorios WHERE estado = :estado AND pendienteEliminacion = 0")
    fun obtenerPorEstado(estado: String): Flow<List<RecordatorioEntity>>

    // INSERT OR REPLACE evita duplicados si el ID ya existe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(recordatorio: RecordatorioEntity)

    @Query("UPDATE recordatorios SET pendienteEliminacion = 1, sincronizado = 0 WHERE id = :id")
    suspend fun marcarParaEliminar(id: Long)

    @Query("SELECT * FROM recordatorios WHERE sincronizado = 0 AND pendienteEliminacion = 0")
    suspend fun obtenerPendientesSincronizar(): List<RecordatorioEntity>

    @Query("SELECT * FROM recordatorios WHERE pendienteEliminacion = 1")
    suspend fun obtenerPendientesEliminar(): List<RecordatorioEntity>

    @Query("DELETE FROM recordatorios WHERE id = :id")
    suspend fun borrarDefinitivamente(id: Long)
}