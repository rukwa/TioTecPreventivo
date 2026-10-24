package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.EvaluacionEntity
import cl.duoc.tiotec.data.repository.AccionRepository
import cl.duoc.tiotec.data.repository.EvaluacionRepository
import cl.duoc.tiotec.data.repository.PerfilRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class InicioUiState(
    val nombre: String = "",
    val ultima: EvaluacionEntity? = null,
    val pendientes: Int = 0
)

/** Resumen de la pantalla de inicio: junta datos de 3 tablas distintas. */
class InicioViewModel(
    perfil: PerfilRepository,
    evaluaciones: EvaluacionRepository,
    acciones: AccionRepository
) : ViewModel() {

    val ui: StateFlow<InicioUiState> = combine(
        perfil.perfil,
        evaluaciones.ultima,
        acciones.pendientes
    ) { p, ultima, pendientes ->
        InicioUiState(nombre = p?.nombre.orEmpty(), ultima = ultima, pendientes = pendientes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InicioUiState())

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                InicioViewModel(app.perfilRepository, app.evaluacionRepository, app.accionRepository)
            }
        }
    }
}
