package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.AccionEntity
import cl.duoc.tiotec.data.local.EvaluacionEntity
import cl.duoc.tiotec.data.repository.AccionRepository
import cl.duoc.tiotec.data.repository.EvaluacionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/** Lee de Room una evaluación y las acciones que generó. */
class ResultadoViewModel(
    evaluacionId: Long,
    evaluaciones: EvaluacionRepository,
    acciones: AccionRepository
) : ViewModel() {

    val evaluacion: StateFlow<EvaluacionEntity?> = evaluaciones.porId(evaluacionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val recomendaciones: StateFlow<List<AccionEntity>> = acciones.porEvaluacion(evaluacionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    companion object {
        /** Factory con parámetro: cada resultado necesita el id de su evaluación. */
        fun factory(evaluacionId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                ResultadoViewModel(evaluacionId, app.evaluacionRepository, app.accionRepository)
            }
        }
    }
}
