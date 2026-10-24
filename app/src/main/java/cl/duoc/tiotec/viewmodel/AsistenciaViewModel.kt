package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.SolicitudEntity
import cl.duoc.tiotec.data.repository.DispositivoRepository
import cl.duoc.tiotec.data.repository.SolicitudRepository
import cl.duoc.tiotec.utils.Validaciones
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AsistenciaUiState(
    val categoria: String? = null,
    val errorCategoria: String? = null,
    val dispositivo: String? = null,
    val errorDispositivo: String? = null,
    val descripcion: String = "",
    val errorDescripcion: String? = null,
    val enviando: Boolean = false,
    val enviada: Boolean = false
)

class AsistenciaViewModel(
    private val repoSolicitudes: SolicitudRepository,
    repoDispositivos: DispositivoRepository
) : ViewModel() {

    val historial: StateFlow<List<SolicitudEntity>> = repoSolicitudes.solicitudes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Los dispositivos registrados más la opción "Otro dispositivo". */
    val opcionesDispositivo: StateFlow<List<String>> = repoDispositivos.dispositivos
        .map { lista -> lista.map { it.nombre } + OTRO_DISPOSITIVO }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), listOf(OTRO_DISPOSITIVO))

    private val _ui = MutableStateFlow(AsistenciaUiState())
    val ui: StateFlow<AsistenciaUiState> = _ui.asStateFlow()

    fun onCategoriaChange(valor: String) =
        _ui.update { it.copy(categoria = valor, errorCategoria = null, enviada = false) }

    fun onDispositivoChange(valor: String) =
        _ui.update { it.copy(dispositivo = valor, errorDispositivo = null, enviada = false) }

    fun onDescripcionChange(valor: String) = _ui.update {
        it.copy(
            descripcion = valor,
            errorDescripcion = Validaciones.largoMinimo(valor, LARGO_MINIMO),
            enviada = false
        )
    }

    fun enviar() {
        val actual = _ui.value
        if (actual.enviando) return
        val revisado = actual.copy(
            errorCategoria = Validaciones.seleccion(actual.categoria),
            errorDispositivo = Validaciones.seleccion(actual.dispositivo),
            errorDescripcion = Validaciones.largoMinimo(actual.descripcion, LARGO_MINIMO)
        )
        if (revisado.errorCategoria != null || revisado.errorDispositivo != null || revisado.errorDescripcion != null) {
            _ui.value = revisado
            return
        }
        viewModelScope.launch {
            _ui.update { it.copy(enviando = true) }
            delay(600) // simula el envío
            repoSolicitudes.enviar(
                SolicitudEntity(
                    categoria = actual.categoria.orEmpty(),
                    dispositivo = actual.dispositivo.orEmpty(),
                    descripcion = actual.descripcion.trim()
                )
            )
            _ui.value = AsistenciaUiState(enviada = true) // limpia el formulario
        }
    }

    companion object {
        const val LARGO_MINIMO = 20
        const val OTRO_DISPOSITIVO = "Otro dispositivo"
        val CATEGORIAS = listOf("Respaldos", "Actualizaciones", "Contraseñas y acceso", "Recuperar una cuenta")

        /** Estado simulado según el tiempo desde el envío (el caso pide estados simulados). */
        fun estadoSimulado(fecha: Long, ahora: Long): String = when {
            ahora - fecha < 60_000 -> "Enviada"
            ahora - fecha < 180_000 -> "En revisión"
            else -> "Resuelta"
        }

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                AsistenciaViewModel(app.solicitudRepository, app.dispositivoRepository)
            }
        }
    }
}
