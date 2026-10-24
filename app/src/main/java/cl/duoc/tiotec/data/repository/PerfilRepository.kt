package cl.duoc.tiotec.data.repository

import cl.duoc.tiotec.data.local.PerfilDao
import cl.duoc.tiotec.data.local.PerfilEntity
import kotlinx.coroutines.flow.Flow

/** Única puerta de acceso al perfil del usuario. */
class PerfilRepository(private val dao: PerfilDao) {

    /** Emite null mientras el usuario no haya creado su perfil. */
    val perfil: Flow<PerfilEntity?> = dao.obtener()

    suspend fun guardar(perfil: PerfilEntity) = dao.guardar(perfil)
}
