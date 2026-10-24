package cl.duoc.tiotec.data.repository

import cl.duoc.tiotec.data.local.SolicitudDao
import cl.duoc.tiotec.data.local.SolicitudEntity
import kotlinx.coroutines.flow.Flow

/** Única puerta de acceso a las solicitudes de asistencia. */
class SolicitudRepository(private val dao: SolicitudDao) {

    val solicitudes: Flow<List<SolicitudEntity>> = dao.obtenerTodas()

    suspend fun enviar(solicitud: SolicitudEntity) = dao.insertar(solicitud)
}
