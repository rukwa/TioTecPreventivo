package cl.duoc.tiotec.utils

import cl.duoc.tiotec.data.model.Area
import cl.duoc.tiotec.data.model.BancoPreguntas
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadoraRiesgoTest {

    private val preguntas = BancoPreguntas.preguntas

    /** Responde todas las preguntas con el mismo puntaje. */
    private fun todasCon(puntaje: Int) = preguntas.associate { it.id to puntaje }

    @Test
    fun todoBien_da100_yRiesgoBajo() {
        val resultado = CalculadoraRiesgo.calcular(todasCon(2))
        assertEquals(100, resultado.puntajeTotal)
        assertEquals(NivelRiesgo.BAJO, resultado.nivel)
    }

    @Test
    fun todoMal_da0_yRiesgoAlto() {
        val resultado = CalculadoraRiesgo.calcular(todasCon(0))
        assertEquals(0, resultado.puntajeTotal)
        assertEquals(NivelRiesgo.ALTO, resultado.nivel)
    }

    @Test
    fun todoAVeces_da50_yRiesgoMedio() {
        val resultado = CalculadoraRiesgo.calcular(todasCon(1))
        assertEquals(50, resultado.puntajeTotal)
        assertEquals(NivelRiesgo.MEDIO, resultado.nivel)
    }

    @Test
    fun soloRespaldosBien_calculaCadaAreaPorSeparado() {
        val respuestas = preguntas.associate { it.id to if (it.area == Area.RESPALDOS) 2 else 0 }
        val resultado = CalculadoraRiesgo.calcular(respuestas)
        assertEquals(100, resultado.puntajePorArea.getValue(Area.RESPALDOS))
        assertEquals(0, resultado.puntajePorArea.getValue(Area.AUTENTICACION))
        assertEquals(25, resultado.puntajeTotal)
    }

    @Test
    fun limitesDeNivel() {
        assertEquals(NivelRiesgo.BAJO, CalculadoraRiesgo.nivelPara(75))
        assertEquals(NivelRiesgo.MEDIO, CalculadoraRiesgo.nivelPara(74))
        assertEquals(NivelRiesgo.MEDIO, CalculadoraRiesgo.nivelPara(45))
        assertEquals(NivelRiesgo.ALTO, CalculadoraRiesgo.nivelPara(44))
    }

    @Test
    fun todoBien_noGeneraAcciones() {
        assertTrue(GeneradorAcciones.generar(1, todasCon(2), ahora = 0).isEmpty())
    }

    @Test
    fun todoMal_generaUnaAccionPorPregunta_ordenadasPorPrioridad() {
        val acciones = GeneradorAcciones.generar(1, todasCon(0), ahora = 0)
        assertEquals(12, acciones.size)
        assertEquals(1, acciones.first().prioridad)
        assertTrue(acciones.zipWithNext().all { (a, b) -> a.prioridad <= b.prioridad })
    }
}
