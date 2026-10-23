package cl.duoc.tiotec.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import java.io.File

/**
 * Manejo de las fotos de los dispositivos.
 * Las fotos se guardan en el almacenamiento INTERNO de la app (carpeta "fotos"):
 * no necesitan permisos de almacenamiento y se borran si se desinstala la app.
 */
object FotoUtils {

    private const val CARPETA = "fotos"

    /** Crea un archivo vacío donde la cámara guardará la foto. */
    fun crearArchivo(context: Context): File {
        val carpeta = File(context.filesDir, CARPETA).apply { mkdirs() }
        return File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    }

    /**
     * La app de cámara es otra app: no puede escribir directo en nuestros archivos.
     * FileProvider le entrega una dirección temporal (content://) con permiso para escribir.
     * La "authority" debe coincidir con la del <provider> en el AndroidManifest.
     */
    fun uriPara(context: Context, archivo: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)

    /** Copia una imagen elegida en la galería a nuestra carpeta interna y devuelve la ruta. */
    fun copiarAInterno(context: Context, origen: Uri): String? = try {
        val destino = crearArchivo(context)
        context.contentResolver.openInputStream(origen)?.use { entrada ->
            destino.outputStream().use { salida -> entrada.copyTo(salida) }
        }
        destino.absolutePath
    } catch (e: Exception) {
        null
    }

    /**
     * Carga la foto reducida (máximo 512 px por lado) para no ocupar mucha memoria:
     * las fotos de la cámara pueden pesar varios MB.
     */
    fun cargar(ruta: String, ladoMaximo: Int = 512): ImageBitmap? {
        if (!File(ruta).exists()) return null
        val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(ruta, medidas)
        var muestra = 1
        while (medidas.outWidth / muestra > ladoMaximo || medidas.outHeight / muestra > ladoMaximo) {
            muestra *= 2
        }
        val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
        return BitmapFactory.decodeFile(ruta, opciones)?.asImageBitmap()
    }
}
