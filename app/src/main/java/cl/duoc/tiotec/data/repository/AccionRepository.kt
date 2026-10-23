package cl.duoc.tiotec.data.repository

import cl.duoc.tiotec.data.local.AccionDao
import cl.duoc.tiotec.data.local.AccionEntity
import kotlinx.coroutines.flow.Flow

/** Única puerta de acceso al checklist de acciones preventivas. */
class AccionRepository(private val dao: AccionDao) {

    val acciones: Flow<List<AccionEntity>> = dao.obtenerTodas()

    val pendientes: Flow<Int> = dao.contarPendientes()

    fun porEvaluacion(evaluacionId: Long): Flow<List<AccionEntity>> =
        dao.obtenerPorEvaluacion(evaluacionId)

    suspend fun guardarTodas(acciones: List<AccionEntity>) = dao.insertarTodas(acciones)

    /** Marca una acción como completada o la vuelve a dejar pendiente. */
    suspend fun cambiarEstado(accion: AccionEntity, completada: Boolean) =
        dao.actualizar(accion.copy(completada = completada))
}
