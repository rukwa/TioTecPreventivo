package cl.duoc.tiotec.data.repository

import cl.duoc.tiotec.data.local.DispositivoDao
import cl.duoc.tiotec.data.local.DispositivoEntity
import kotlinx.coroutines.flow.Flow

/** Única puerta de acceso a los dispositivos registrados. */
class DispositivoRepository(private val dao: DispositivoDao) {

    val dispositivos: Flow<List<DispositivoEntity>> = dao.obtenerTodos()

    /** Si el dispositivo es nuevo (id 0) lo inserta; si ya existe, lo actualiza. */
    suspend fun guardar(dispositivo: DispositivoEntity) {
        if (dispositivo.id == 0L) dao.insertar(dispositivo) else dao.actualizar(dispositivo)
    }

    suspend fun eliminar(dispositivo: DispositivoEntity) = dao.eliminar(dispositivo)
}
