package cl.duoc.tiotec.data.model

/** Puntaje máximo que se puede obtener en una pregunta. */
const val PUNTAJE_MAXIMO_PREGUNTA = 2

/** Una alternativa de respuesta. Sí = 2 puntos, A veces = 1, No = 0. */
data class Opcion(
    val texto: String,
    val puntaje: Int
)

/**
 * Una pregunta del cuestionario.
 * Si el usuario no obtiene el puntaje máximo, se genera la recomendación
 * como acción en su plan preventivo.
 */
data class Pregunta(
    val id: Int,
    val area: Area,
    val enunciado: String,
    val opciones: List<Opcion>,
    val recomendacion: Recomendacion
)
