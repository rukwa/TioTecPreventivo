package cl.duoc.tiotec.data.repository

import cl.duoc.tiotec.data.local.EvaluacionDao
import cl.duoc.tiotec.data.local.EvaluacionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Única puerta de acceso a las evaluaciones.
 * Los ViewModels hablan con el Repository, nunca directo con Room.
 * Si mañana los datos vinieran de una API (EFT), solo cambia esta clase.
 */
class EvaluacionRepository(private val dao: EvaluacionDao) {

    val evaluaciones: Flow<List<EvaluacionEntity>> = dao.obtenerTodas()

    val ultima: Flow<EvaluacionEntity?> = dao.obtenerUltima()

    fun porId(id: Long): Flow<EvaluacionEntity?> = dao.obtenerPorId(id)

    /** Guarda la evaluación y devuelve el id generado (para navegar al resultado). */
    suspend fun guardar(evaluacion: EvaluacionEntity): Long = dao.insertar(evaluacion)

    suspend fun eliminar(evaluacion: EvaluacionEntity) = dao.eliminar(evaluacion)
}
