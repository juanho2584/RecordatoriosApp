package com.pinto.recordatorios.data

import com.pinto.recordatorios.data.local.RecordatorioDao
import com.pinto.recordatorios.data.local.UserPreferencesRepository
import com.pinto.recordatorios.data.mappers.toDomain
import com.pinto.recordatorios.data.mappers.toDto
import com.pinto.recordatorios.data.mappers.toEntity
import com.pinto.recordatorios.data.remote.RecordatorioApi
import com.pinto.recordatorios.domain.Recordatorio
import com.pinto.recordatorios.domain.RecordatoriosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class RecordatoriosRepositoryImpl(
    private val dao: RecordatorioDao,
    private val api: RecordatorioApi,
    private val prefs: UserPreferencesRepository
) : RecordatoriosRepository {

    // 1. La UI consulta Room a través de este Flow
    override fun obtenerRecordatoriosFlow(): Flow<List<Recordatorio>> =
        dao.obtenerTodos().map { lista -> lista.map { it.toDomain() } }

    override suspend fun obtenerPorId(id: Long): Recordatorio? =
        dao.obtenerPorId(id)?.toDomain()

    // 2. Guardar: Primero local, luego remoto
    override suspend fun guardar(recordatorio: Recordatorio) {
        // Paso A: Guardar INMEDIATAMENTE en Room (marcado como no sincronizado)
        val entity = recordatorio.toEntity(sincronizado = false)
        dao.insertar(entity)

        // Paso B: Intentar subir al servidor
        try {
            val respuesta = api.crearRecordatorio(recordatorio.toDto())

            // Éxito: Actualizamos Room con el ID del servidor y marcamos como sincronizado
            // Usamos REPLACE, así que no se genera un nuevo registro (evita duplicados)
            val entidadSincronizada = entity.copy(
                id = respuesta.id ?: entity.id,
                sincronizado = true
            )
            dao.insertar(entidadSincronizada)

        } catch (e: SocketTimeoutException) {
            // Timeout: El registro ya está en Room. Queda pendiente.
        } catch (e: HttpException) {
            // Error del servidor o solicitud inválida (4xx/5xx). Queda pendiente.
        } catch (e: IOException) {
            // Falta de conexión. Queda pendiente.
        }
    }

    // 3. Eliminar: Primero local, luego remoto
    override suspend fun eliminar(id: Long) {
        // Marcamos como pendiente de eliminación en local
        dao.marcarParaEliminar(id)

        try {
            // Si tu API real tuviera un endpoint DELETE, lo llamarías acá:
            // api.eliminarRecordatorio(id)

            // Si el servidor confirma (o si no hay servidor real), borramos de Room definitivamente
            dao.borrarDefinitivamente(id)
        } catch (e: Exception) {
            // Si falla la red, el registro queda con pendienteEliminacion = true
            // y se procesará en el próximo sincronizarPendientes()
        }
    }

    // 4. Sincronización masiva (cuando vuelve la red)
    override suspend fun sincronizarPendientes() {
        // A. Subir creaciones/ediciones pendientes
        val pendientesSubir = dao.obtenerPendientesSincronizar()
        pendientesSubir.forEach { entity ->
            try {
                val respuesta = api.crearRecordatorio(
                    com.pinto.recordatorios.data.remote.RecordatorioDto(
                        id = entity.id,
                        title = entity.titulo,
                        body = entity.descripcion
                    )
                )
                dao.insertar(entity.copy(id = respuesta.id ?: entity.id, sincronizado = true))
            } catch (e: Exception) {
                // Si falla, se queda en la lista de pendientes para el próximo intento
            }
        }

        // B. Procesar eliminaciones pendientes
        val pendientesEliminar = dao.obtenerPendientesEliminar()
        pendientesEliminar.forEach { entity ->
            try {
                // api.eliminarRecordatorio(entity.id)
                dao.borrarDefinitivamente(entity.id)
            } catch (e: Exception) {
                // Se queda pendiente
            }
        }
    }
}