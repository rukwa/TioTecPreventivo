package cl.duoc.tiotec.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "perfil": el perfil ficticio del usuario.
 * Siempre hay uno solo por instalación, por eso el id es fijo (1).
 */
@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = 1,
    val nombre: String,
    val correo: String,
    // "Básico", "Intermedio" o "Avanzado"
    val nivelExperiencia: String,
    val creado: Long = System.currentTimeMillis()
)
