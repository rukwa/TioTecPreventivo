package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.PerfilEntity
import cl.duoc.tiotec.data.repository.PerfilRepository
import cl.duoc.tiotec.utils.Validaciones
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Todo lo que muestra el formulario de perfil: valores y errores de cada campo. */
data class PerfilUiState(
    val nombre: String = "",
    val errorNombre: String? = null,
    val correo: String = "",
    val errorCorreo: String? = null,
    val nivel: String? = null,
    val datosFicticios: Boolean = false,
    val errorFicticios: String? = null,
    val guardando: Boolean = false
) {
    /** El botón "Crear perfil" solo se habilita cuando todo es válido. */
    val esValido: Boolean
        get() = Validaciones.nombre(nombre) == null &&
            Validaciones.correo(correo) == null &&
            Validaciones.seleccion(nivel) == null &&
            datosFicticios
}

/**
 * ViewModel del formulario de perfil.
 * Guarda el estado en un StateFlow: la pantalla lo observa y se redibuja sola
 * cada vez que cambia. Así el estado sobrevive si se gira el teléfono.
 */
class PerfilViewModel(private val repositorio: PerfilRepository) : ViewModel() {

    private val _ui = MutableStateFlow(PerfilUiState())
    val ui: StateFlow<PerfilUiState> = _ui.asStateFlow()

    /** Perfil ya guardado. Es null si es la primera vez que se abre la app. */
    val perfilGuardado: StateFlow<PerfilEntity?> = repositorio.perfil
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // Cada cambio valida el campo al instante usando las reglas de Validaciones.kt
    fun onNombreChange(valor: String) =
        _ui.update { it.copy(nombre = valor, errorNombre = Validaciones.nombre(valor)) }

    fun onCorreoChange(valor: String) =
        _ui.update { it.copy(correo = valor, errorCorreo = Validaciones.correo(valor)) }

    fun onNivelChange(valor: String) = _ui.update { it.copy(nivel = valor) }

    fun onDatosFicticiosChange(marcado: Boolean) = _ui.update {
        it.copy(
            datosFicticios = marcado,
            errorFicticios = Validaciones.aceptado(marcado, MENSAJE_FICTICIOS)
        )
    }

    /** Guarda el perfil en Room. La pantalla navega sola cuando perfilGuardado deja de ser null. */
    fun guardar() {
        val actual = _ui.value
        if (!actual.esValido || actual.guardando) return
        viewModelScope.launch {
            _ui.update { it.copy(guardando = true) }
            delay(600) // pausa corta para que se vea el indicador de carga
            repositorio.guardar(
                PerfilEntity(
                    nombre = actual.nombre.trim(),
                    correo = actual.correo.trim(),
                    nivelExperiencia = actual.nivel.orEmpty()
                )
            )
            _ui.update { it.copy(guardando = false) }
        }
    }

    companion object {
        val NIVELES = listOf("Básico", "Intermedio", "Avanzado")
        const val MENSAJE_FICTICIOS = "Debes confirmar que usarás datos ficticios"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                PerfilViewModel(app.perfilRepository)
            }
        }
    }
}
