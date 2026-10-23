package cl.duoc.tiotec.utils

import cl.duoc.tiotec.data.model.Area
import cl.duoc.tiotec.data.model.BancoPreguntas
import cl.duoc.tiotec.data.model.PUNTAJE_MAXIMO_PREGUNTA
import cl.duoc.tiotec.data.model.Pregunta

/** Nivel de riesgo: a MÁS puntaje, MENOS riesgo. */
enum class NivelRiesgo(val etiqueta: String) {
    BAJO("Bajo"),
    MEDIO("Medio"),
    ALTO("Alto")
}

/** Resultado de una evaluación: puntaje total y por área, de 0 a 100. */
data class ResultadoEvaluacion(
    val puntajeTotal: Int,
    val puntajePorArea: Map<Area, Int>,
    val nivel: NivelRiesgo
)

/**
 * Calcula el puntaje y el nivel de riesgo a partir de las respuestas.
 * Es Kotlin puro (no usa Android), por eso se puede probar con tests unitarios.
 */
object CalculadoraRiesgo {

    /** respuestas: id de la pregunta → puntaje obtenido (2 = Sí, 1 = A veces, 0 = No). */
    fun calcular(
        respuestas: Map<Int, Int>,
        preguntas: List<Pregunta> = BancoPreguntas.preguntas
    ): ResultadoEvaluacion {
        val porArea = Area.entries.associateWith { area ->
            val delArea = preguntas.filter { it.area == area }
            porcentaje(
                obtenido = delArea.sumOf { respuestas[it.id] ?: 0 },
                maximo = delArea.size * PUNTAJE_MAXIMO_PREGUNTA
            )
        }
        val total = porcentaje(
            obtenido = preguntas.sumOf { respuestas[it.id] ?: 0 },
            maximo = preguntas.size * PUNTAJE_MAXIMO_PREGUNTA
        )
        return ResultadoEvaluacion(total, porArea, nivelPara(total))
    }

    /** 75 a 100 = riesgo bajo, 45 a 74 = medio, 0 a 44 = alto. */
    fun nivelPara(puntaje: Int): NivelRiesgo = when {
        puntaje >= 75 -> NivelRiesgo.BAJO
        puntaje >= 45 -> NivelRiesgo.MEDIO
        else -> NivelRiesgo.ALTO
    }

    /** Porcentaje redondeado al entero más cercano. */
    private fun porcentaje(obtenido: Int, maximo: Int): Int =
        if (maximo == 0) 0 else (obtenido * 100 + maximo / 2) / maximo
}
