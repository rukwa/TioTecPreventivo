package cl.duoc.tiotec.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "solicitudes": las solicitudes de asistencia que envía el usuario.
 * El estado (Enviada, En revisión, Resuelta) es simulado: se calcula
 * según el tiempo que pasó desde la fecha de envío.
 */
@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoria: String,
    val dispositivo: String,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis()
)
