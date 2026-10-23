package cl.duoc.tiotec.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "dispositivos": los dispositivos de prueba que registra el usuario.
 * La foto no se guarda dentro de la base: se guarda como archivo en el
 * almacenamiento interno de la app y aquí solo queda su ruta.
 */
@Entity(tableName = "dispositivos")
data class DispositivoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    // "Teléfono", "Tablet" o "Computador"
    val tipo: String,
    // "Android", "iOS", "Windows" o "macOS"
    val sistema: String,
    val version: String,
    val fotoRuta: String? = null
)
