package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.EvaluacionEntity
import cl.duoc.tiotec.data.model.Area
import cl.duoc.tiotec.data.model.BancoPreguntas
import cl.duoc.tiotec.data.model.Pregunta
import cl.duoc.tiotec.data.repository.AccionRepository
import cl.duoc.tiotec.data.repository.EvaluacionRepository
import cl.duoc.tiotec.utils.CalculadoraRiesgo
import cl.duoc.tiotec.utils.Fechas
import cl.duoc.tiotec.utils.GeneradorAcciones
import cl.duoc.tiotec.utils.Notificaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del cuestionario: en qué pregunta va y qué respondió en cada una. */
data class EvaluacionUiState(
    val indice: Int = 0,
    // id de la pregunta → puntaje elegido
    val respuestas: Map<Int, Int> = emptyMap(),
    val mostrarFaltantes: Boolean = false,
    val guardando: Boolean = false
) {
    val pregunta: Pregunta get() = BancoPreguntas.preguntas[indice]
    val total: Int get() = BancoPreguntas.preguntas.size
    val respondidas: Int get() = respuestas.size
    val faltantes: Int get() = total - respondidas
    val esUltima: Boolean get() = indice == total - 1
    val preguntaActualRespondida: Boolean get() = respuestas.containsKey(pregunta.id)
}

class EvaluacionViewModel(
    private val evaluaciones: EvaluacionRepository,
    private val acciones: AccionRepository,
    private val programarRecordatorio: (Long) -> Unit
) : ViewModel() {

    private val _ui = MutableStateFlow(EvaluacionUiState())
    val ui: StateFlow<EvaluacionUiState> = _ui.asStateFlow()

    fun responder(puntaje: Int) = _ui.update {
        it.copy(respuestas = it.respuestas + (it.pregunta.id to puntaje), mostrarFaltantes = false)
    }

    fun siguiente() = _ui.update { if (it.esUltima) it else it.copy(indice = it.indice + 1) }

    fun anterior() = _ui.update { if (it.indice == 0) it else it.copy(indice = it.indice - 1) }

    /**
     * Calcula el resultado, guarda la evaluación y su checklist en Room,
     * programa el recordatorio y devuelve el id para mostrar el resultado.
     */
    fun finalizar(alGuardar: (Long) -> Unit) {
        val estado = _ui.value
        // Estructura de control: no deja terminar si quedan preguntas sin responder
        if (estado.faltantes > 0) {
            _ui.update { it.copy(mostrarFaltantes = true) }
            return
        }
        if (estado.guardando) return

        viewModelScope.launch {
            _ui.update { it.copy(guardando = true) }
            val ahora = System.currentTimeMillis()
            val resultado = CalculadoraRiesgo.calcular(estado.respuestas)
            val proximaRevision = ahora + DIAS_HASTA_REVISION * Fechas.DIA_MS

            val id = evaluaciones.guardar(
                EvaluacionEntity(
                    fecha = ahora,
                    puntajeTotal = resultado.puntajeTotal,
                    puntajeRespaldos = resultado.puntajePorArea.getValue(Area.RESPALDOS),
                    puntajeActualizaciones = resultado.puntajePorArea.getValue(Area.ACTUALIZACIONES),
                    puntajeAutenticacion = resultado.puntajePorArea.getValue(Area.AUTENTICACION),
                    puntajeRecuperacion = resultado.puntajePorArea.getValue(Area.RECUPERACION),
                    nivelRiesgo = resultado.nivel.etiqueta,
                    proximaRevision = proximaRevision
                )
            )
            acciones.guardarTodas(GeneradorAcciones.generar(id, estado.respuestas, ahora))
            programarRecordatorio(proximaRevision)

            _ui.value = EvaluacionUiState() // deja el cuestionario limpio para la próxima vez
            alGuardar(id)
        }
    }

    companion object {
        const val DIAS_HASTA_REVISION = 30L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                EvaluacionViewModel(app.evaluacionRepository, app.accionRepository) { cuando ->
                    Notificaciones.programarRecordatorio(app, cuando)
                }
            }
        }
    }
}
