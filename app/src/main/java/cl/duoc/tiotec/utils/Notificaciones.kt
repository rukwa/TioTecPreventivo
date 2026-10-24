package cl.duoc.tiotec.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cl.duoc.tiotec.MainActivity
import java.util.concurrent.TimeUnit

/**
 * Recurso nativo: notificaciones locales.
 * - Desde Android 8 cada notificación pertenece a un "canal" que el usuario puede configurar.
 * - Desde Android 13 hay que pedir el permiso POST_NOTIFICATIONS en tiempo de ejecución.
 * - WorkManager programa el aviso aunque la app esté cerrada.
 */
object Notificaciones {

    const val CANAL_ID = "recordatorios"
    private const val ID_NOTIFICACION = 1
    private const val TRABAJO_REVISION = "recordatorio_revision"

    /** Se llama una vez al abrir la app (TioTecApp.onCreate). */
    fun crearCanal(context: Context) {
        val canal = NotificationChannel(
            CANAL_ID,
            "Recordatorios de revisión",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Avisos para volver a evaluar tu seguridad digital" }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    /** Muestra la notificación. Si no hay permiso, no hace nada. */
    @SuppressLint("MissingPermission") // el permiso se revisa en la primera línea
    fun mostrar(context: Context, titulo: String, texto: String) {
        if (!tienePermiso(context)) return
        // Al tocar la notificación se abre la app
        val abrirApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(ID_NOTIFICACION, notificacion)
    }

    /** Programa el aviso para la fecha de la próxima revisión (reemplaza el anterior). */
    fun programarRecordatorio(context: Context, cuandoMs: Long) {
        val demora = (cuandoMs - System.currentTimeMillis()).coerceAtLeast(0)
        val trabajo = OneTimeWorkRequestBuilder<RecordatorioWorker>()
            .setInitialDelay(demora, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    RecordatorioWorker.TITULO to "Es hora de tu revisión preventiva",
                    RecordatorioWorker.TEXTO to "Ya pasó un mes desde tu última evaluación. Vuelve a evaluarte en TíoTec."
                )
            )
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(TRABAJO_REVISION, ExistingWorkPolicy.REPLACE, trabajo)
    }

    /** Para la demostración: lanza una notificación de prueba en 10 segundos. */
    fun probar(context: Context) {
        val trabajo = OneTimeWorkRequestBuilder<RecordatorioWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    RecordatorioWorker.TITULO to "Recordatorio TíoTec (prueba)",
                    RecordatorioWorker.TEXTO to "Así te avisaremos cuando toque tu próxima revisión preventiva."
                )
            )
            .build()
        WorkManager.getInstance(context).enqueue(trabajo)
    }
}
