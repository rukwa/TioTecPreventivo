package cl.duoc.tiotec.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos SQLite de la app (archivo "tiotec.db").
 *
 * IMPORTANTE para el equipo: cada vez que se agrega una entidad o una columna,
 * hay que agregarla en "entities" y SUBIR el número de "version".
 * Con fallbackToDestructiveMigration la base se recrea sola al cambiar la versión
 * (se pierden los datos de prueba, lo que está bien durante el desarrollo).
 */
@Database(
    entities = [
        EvaluacionEntity::class,
        AccionEntity::class,
        PerfilEntity::class,
        DispositivoEntity::class,
        SolicitudEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun evaluacionDao(): EvaluacionDao
    abstract fun accionDao(): AccionDao
    abstract fun perfilDao(): PerfilDao
    abstract fun dispositivoDao(): DispositivoDao
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        // Una sola instancia para toda la app (patrón Singleton)
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tiotec.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { instancia = it }
            }
    }
}
