package cl.duoc.tiotec.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Consultas de la tabla "acciones" (checklist preventivo). */
@Dao
interface AccionDao {

    @Insert
    suspend fun insertarTodas(acciones: List<AccionEntity>)

    @Update
    suspend fun actualizar(accion: AccionEntity)

    // Primero las pendientes, luego por prioridad (1 = Alta) y por fecha de revisión
    @Query("SELECT * FROM acciones ORDER BY completada ASC, prioridad ASC, fechaRevision ASC")
    fun obtenerTodas(): Flow<List<AccionEntity>>

    @Query("SELECT * FROM acciones WHERE evaluacionId = :evaluacionId ORDER BY prioridad ASC")
    fun obtenerPorEvaluacion(evaluacionId: Long): Flow<List<AccionEntity>>

    @Query("SELECT COUNT(*) FROM acciones WHERE completada = 0")
    fun contarPendientes(): Flow<Int>
}
