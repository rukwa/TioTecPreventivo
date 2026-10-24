package cl.duoc.tiotec.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Consultas de la tabla "evaluaciones".
 * - Las funciones "suspend" escriben (se ejecutan fuera del hilo principal).
 * - Las que devuelven Flow se actualizan solas cuando cambian los datos,
 *   por eso la pantalla se refresca sin recargar nada a mano.
 */
@Dao
interface EvaluacionDao {

    @Insert
    suspend fun insertar(evaluacion: EvaluacionEntity): Long

    @Delete
    suspend fun eliminar(evaluacion: EvaluacionEntity)

    @Query("SELECT * FROM evaluaciones ORDER BY fecha DESC")
    fun obtenerTodas(): Flow<List<EvaluacionEntity>>

    @Query("SELECT * FROM evaluaciones WHERE id = :id")
    fun obtenerPorId(id: Long): Flow<EvaluacionEntity?>

    @Query("SELECT * FROM evaluaciones ORDER BY fecha DESC LIMIT 1")
    fun obtenerUltima(): Flow<EvaluacionEntity?>
}
