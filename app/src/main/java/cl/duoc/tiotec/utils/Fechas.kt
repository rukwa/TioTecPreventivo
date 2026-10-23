package cl.duoc.tiotec.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Utilidades de fechas. En la base guardamos las fechas como milisegundos (Long). */
object Fechas {

    /** Milisegundos que tiene un día. */
    const val DIA_MS = 86_400_000L

    private val formato = SimpleDateFormat("dd-MM-yyyy", Locale.forLanguageTag("es-CL"))

    /** 1792454448000 → "19-10-2026" */
    fun formatear(milisegundos: Long): String = formato.format(Date(milisegundos))
}
