package cl.duoc.tiotec

import android.app.Application
import cl.duoc.tiotec.data.local.AppDatabase
import cl.duoc.tiotec.data.repository.AccionRepository
import cl.duoc.tiotec.data.repository.DispositivoRepository
import cl.duoc.tiotec.data.repository.EvaluacionRepository
import cl.duoc.tiotec.data.repository.PerfilRepository
import cl.duoc.tiotec.data.repository.SolicitudRepository

/**
 * Clase Application: se crea una sola vez cuando abre la app.
 * Aquí creamos la base de datos y los repositorios (inyección de dependencias manual).
 * "by lazy" hace que se creen recién la primera vez que alguien los usa.
 *
 * Los ViewModels los obtienen en su Factory:
 *   val app = this[APPLICATION_KEY] as TioTecApp
 *
 * Requiere android:name=".TioTecApp" en el AndroidManifest.xml.
 */
class TioTecApp : Application() {

    val baseDeDatos: AppDatabase by lazy { AppDatabase.obtener(this) }

    val evaluacionRepository: EvaluacionRepository by lazy {
        EvaluacionRepository(baseDeDatos.evaluacionDao())
    }

    val accionRepository: AccionRepository by lazy {
        AccionRepository(baseDeDatos.accionDao())
    }

    val perfilRepository: PerfilRepository by lazy {
        PerfilRepository(baseDeDatos.perfilDao())
    }

    val dispositivoRepository: DispositivoRepository by lazy {
        DispositivoRepository(baseDeDatos.dispositivoDao())
    }

    val solicitudRepository: SolicitudRepository by lazy {
        SolicitudRepository(baseDeDatos.solicitudDao())
    }
}
