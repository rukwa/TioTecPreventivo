package cl.duoc.tiotec.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BancoPreguntasTest {

    @Test
    fun hayDocePreguntas() {
        assertEquals(12, BancoPreguntas.preguntas.size)
    }

    @Test
    fun cadaAreaTieneTresPreguntas() {
        Area.entries.forEach { area ->
            assertEquals("Área ${area.titulo}", 3, BancoPreguntas.porArea(area).size)
        }
    }

    @Test
    fun losIdsNoSeRepiten() {
        val ids = BancoPreguntas.preguntas.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun cadaPreguntaPermiteElPuntajeMaximo() {
        BancoPreguntas.preguntas.forEach { pregunta ->
            assertTrue(pregunta.opciones.any { it.puntaje == PUNTAJE_MAXIMO_PREGUNTA })
        }
    }
}
