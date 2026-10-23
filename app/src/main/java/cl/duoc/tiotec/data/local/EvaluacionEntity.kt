package cl.duoc.tiotec.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "evaluaciones": una fila por cada evaluación que completa el usuario.
 * Los puntajes van de 0 a 100. Las fechas se guardan en milisegundos (Long)
 * para no necesitar convertidores de tipo en Room.
 */
@Entity(tableName = "evaluaciones")
data class EvaluacionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Dispositivo evaluado. Queda null hasta que exista la tabla de dispositivos (miércoles).
    val dispositivoId: Long? = null,
    val fecha: Long = System.currentTimeMillis(),
    val puntajeTotal: Int,
    val puntajeRespaldos: Int,
    val puntajeActualizaciones: Int,
    val puntajeAutenticacion: Int,
    val puntajeRecuperacion: Int,
    // "Bajo", "Medio" o "Alto"
    val nivelRiesgo: String,
    val proximaRevision: Long
)
