package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.AccionEntity
import cl.duoc.tiotec.data.repository.AccionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FiltroAcciones(val etiqueta: String) {
    PENDIENTES("Pendientes"),
    COMPLETADAS("Completadas"),
    TODAS("Todas")
}

/** Checklist preventivo: filtra las acciones y marca/desmarca cada una (se guarda en Room). */
class AccionesViewModel(private val repositorio: AccionRepository) : ViewModel() {

    private val _filtro = MutableStateFlow(FiltroAcciones.PENDIENTES)
    val filtro: StateFlow<FiltroAcciones> = _filtro.asStateFlow()

    // combine: cada vez que cambian las acciones en Room O el filtro, se recalcula la lista
    val acciones: StateFlow<List<AccionEntity>> = combine(repositorio.acciones, _filtro) { lista, filtro ->
        when (filtro) {
            FiltroAcciones.PENDIENTES -> lista.filter { !it.completada }
            FiltroAcciones.COMPLETADAS -> lista.filter { it.completada }
            FiltroAcciones.TODAS -> lista
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun cambiarFiltro(nuevo: FiltroAcciones) {
        _filtro.value = nuevo
    }

    fun cambiarEstado(accion: AccionEntity, completada: Boolean) {
        viewModelScope.launch { repositorio.cambiarEstado(accion, completada) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                AccionesViewModel(app.accionRepository)
            }
        }
    }
}
