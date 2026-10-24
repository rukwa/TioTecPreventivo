package cl.duoc.tiotec.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DispositivoDao {

    @Insert
    suspend fun insertar(dispositivo: DispositivoEntity): Long

    @Update
    suspend fun actualizar(dispositivo: DispositivoEntity)

    @Delete
    suspend fun eliminar(dispositivo: DispositivoEntity)

    @Query("SELECT * FROM dispositivos ORDER BY nombre COLLATE NOCASE")
    fun obtenerTodos(): Flow<List<DispositivoEntity>>
}
