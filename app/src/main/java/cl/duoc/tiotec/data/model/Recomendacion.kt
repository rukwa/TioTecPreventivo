package cl.duoc.tiotec.data.model

/**
 * Prioridad de una acción preventiva.
 * "orden" se guarda en Room como número para poder ordenar: 1 = Alta primero.
 */
enum class Prioridad(val etiqueta: String, val orden: Int) {
    ALTA("Alta", 1),
    MEDIA("Media", 2),
    BAJA("Baja", 3);

    companion object {
        /** Convierte el número guardado en Room de vuelta a la prioridad. */
        fun desdeOrden(orden: Int): Prioridad = entries.firstOrNull { it.orden == orden } ?: MEDIA
    }
}

/** Qué se le recomienda hacer al usuario cuando responde mal una pregunta. */
data class Recomendacion(
    val titulo: String,
    val detalle: String,
    val prioridad: Prioridad
)
