package cl.duoc.tiotec.utils

import cl.duoc.tiotec.data.local.AccionEntity
import cl.duoc.tiotec.data.model.BancoPreguntas
import cl.duoc.tiotec.data.model.PUNTAJE_MAXIMO_PREGUNTA
import cl.duoc.tiotec.data.model.Pregunta
import cl.duoc.tiotec.data.model.Prioridad

/**
 * Genera el checklist preventivo: una acción por cada pregunta que NO obtuvo el puntaje máximo.
 * La fecha de revisión depende de la prioridad: Alta en 7 días, Media en 14, Baja en 30.
 */
object GeneradorAcciones {

    fun generar(
        evaluacionId: Long,
        respuestas: Map<Int, Int>,
        ahora: Long = System.currentTimeMillis(),
        preguntas: List<Pregunta> = BancoPreguntas.preguntas
    ): List<AccionEntity> =
        preguntas
            .filter { (respuestas[it.id] ?: 0) < PUNTAJE_MAXIMO_PREGUNTA }
            .map { pregunta ->
                val recomendacion = pregunta.recomendacion
                AccionEntity(
                    evaluacionId = evaluacionId,
                    titulo = recomendacion.titulo,
                    detalle = recomendacion.detalle,
                    area = pregunta.area.name,
                    prioridad = recomendacion.prioridad.orden,
                    fechaRevision = ahora + diasPara(recomendacion.prioridad) * Fechas.DIA_MS
                )
            }
            .sortedBy { it.prioridad }

    fun diasPara(prioridad: Prioridad): Long = when (prioridad) {
        Prioridad.ALTA -> 7
        Prioridad.MEDIA -> 14
        Prioridad.BAJA -> 30
    }
}
