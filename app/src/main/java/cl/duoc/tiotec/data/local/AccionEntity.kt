package cl.duoc.tiotec.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "acciones": el checklist preventivo que se genera a partir de una evaluación.
 * Si se borra la evaluación, sus acciones se borran también (CASCADE).
 */
@Entity(
    tableName = "acciones",
    foreignKeys = [
        ForeignKey(
            entity = EvaluacionEntity::class,
            parentColumns = ["id"],
            childColumns = ["evaluacionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("evaluacionId")]
)
data class AccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val evaluacionId: Long,
    val titulo: String,
    val detalle: String,
    // Nombre del área, por ejemplo "RESPALDOS" (Area.name)
    val area: String,
    // 1 = Alta, 2 = Media, 3 = Baja (Prioridad.orden). Número para poder ordenar en SQL.
    val prioridad: Int,
    val completada: Boolean = false,
    val fechaRevision: Long
)
