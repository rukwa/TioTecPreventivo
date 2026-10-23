package cl.duoc.tiotec.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {

    @Insert
    suspend fun insertar(solicitud: SolicitudEntity): Long

    @Query("SELECT * FROM solicitudes ORDER BY fecha DESC")
    fun obtenerTodas(): Flow<List<SolicitudEntity>>
}
