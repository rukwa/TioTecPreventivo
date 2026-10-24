package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.EvaluacionEntity
import cl.duoc.tiotec.data.repository.EvaluacionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Evaluaciones anteriores, de la más reciente a la más antigua. */
class HistorialViewModel(private val repositorio: EvaluacionRepository) : ViewModel() {

    val evaluaciones: StateFlow<List<EvaluacionEntity>> = repositorio.evaluaciones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Al borrar una evaluación, Room borra también sus acciones (ForeignKey CASCADE). */
    fun eliminar(evaluacion: EvaluacionEntity) {
        viewModelScope.launch { repositorio.eliminar(evaluacion) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                HistorialViewModel(app.evaluacionRepository)
            }
        }
    }
}
