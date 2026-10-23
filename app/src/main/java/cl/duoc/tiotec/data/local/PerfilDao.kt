package cl.duoc.tiotec.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDao {

    // REPLACE: si el perfil ya existe (mismo id), lo reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(perfil: PerfilEntity)

    @Query("SELECT * FROM perfil WHERE id = 1")
    fun obtener(): Flow<PerfilEntity?>
}
