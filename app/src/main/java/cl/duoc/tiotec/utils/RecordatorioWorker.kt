package cl.duoc.tiotec.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/** Tarea en segundo plano que WorkManager ejecuta a la hora programada: muestra la notificación. */
class RecordatorioWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Notificaciones.mostrar(
            applicationContext,
            inputData.getString(TITULO) ?: "TíoTec",
            inputData.getString(TEXTO).orEmpty()
        )
        return Result.success()
    }

    companion object {
        const val TITULO = "titulo"
        const val TEXTO = "texto"
    }
}
