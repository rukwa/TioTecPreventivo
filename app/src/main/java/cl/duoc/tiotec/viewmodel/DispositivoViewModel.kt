package cl.duoc.tiotec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.tiotec.TioTecApp
import cl.duoc.tiotec.data.local.DispositivoEntity
import cl.duoc.tiotec.data.repository.DispositivoRepository
import cl.duoc.tiotec.utils.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Valores y errores del formulario de un dispositivo. id = 0 significa dispositivo nuevo. */
data class FormularioDispositivo(
    val id: Long = 0,
    val nombre: String = "",
    val errorNombre: String? = null,
    val tipo: String? = null,
    val errorTipo: String? = null,
    val sistema: String? = null,
    val errorSistema: String? = null,
    val version: String = "",
    val errorVersion: String? = null,
    val fotoRuta: String? = null
) {
    val esNuevo: Boolean get() = id == 0L
}

/** formulario == null → se muestra la lista; si no, se muestra el formulario. */
data class DispositivosUiState(
    val formulario: FormularioDispositivo? = null,
    val guardando: Boolean = false
)

class DispositivoViewModel(private val repositorio: DispositivoRepository) : ViewModel() {

    val dispositivos: StateFlow<List<DispositivoEntity>> = repositorio.dispositivos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _ui = MutableStateFlow(DispositivosUiState())
    val ui: StateFlow<DispositivosUiState> = _ui.asStateFlow()

    fun nuevo() = _ui.update { it.copy(formulario = FormularioDispositivo()) }

    fun editar(d: DispositivoEntity) = _ui.update {
        it.copy(
            formulario = FormularioDispositivo(
                id = d.id,
                nombre = d.nombre,
                tipo = d.tipo,
                sistema = d.sistema,
                version = d.version,
                fotoRuta = d.fotoRuta
            )
        )
    }

    fun cerrarFormulario() = _ui.update { DispositivosUiState() }

    /** Aplica un cambio al formulario solo si está abierto. */
    private fun cambiar(cambio: (FormularioDispositivo) -> FormularioDispositivo) = _ui.update { estado ->
        val formulario = estado.formulario ?: return@update estado
        estado.copy(formulario = cambio(formulario))
    }

    fun onNombreChange(valor: String) =
        cambiar { it.copy(nombre = valor, errorNombre = Validaciones.largoMinimo(valor, 3)) }

    fun onTipoChange(valor: String) = cambiar { it.copy(tipo = valor, errorTipo = null) }

    fun onSistemaChange(valor: String) = cambiar { it.copy(sistema = valor, errorSistema = null) }

    fun onVersionChange(valor: String) =
        cambiar { it.copy(version = valor, errorVersion = Validaciones.versionSistema(valor)) }

    fun onFotoChange(ruta: String) = cambiar { it.copy(fotoRuta = ruta) }

    /** Valida TODOS los campos al presionar Guardar; si hay errores, los muestra y no guarda. */
    fun guardar() {
        val f = _ui.value.formulario ?: return
        val revisado = f.copy(
            errorNombre = Validaciones.largoMinimo(f.nombre, 3),
            errorTipo = Validaciones.seleccion(f.tipo),
            errorSistema = Validaciones.seleccion(f.sistema),
            errorVersion = Validaciones.versionSistema(f.version)
        )
        val hayErrores = listOf(
            revisado.errorNombre, revisado.errorTipo, revisado.errorSistema, revisado.errorVersion
        ).any { it != null }

        if (hayErrores) {
            _ui.update { it.copy(formulario = revisado) }
            return
        }
        viewModelScope.launch {
            _ui.update { it.copy(guardando = true) }
            repositorio.guardar(
                DispositivoEntity(
                    id = f.id,
                    nombre = f.nombre.trim(),
                    tipo = f.tipo.orEmpty(),
                    sistema = f.sistema.orEmpty(),
                    version = f.version.trim(),
                    fotoRuta = f.fotoRuta
                )
            )
            _ui.update { DispositivosUiState() }
        }
    }

    fun eliminar(d: DispositivoEntity) {
        viewModelScope.launch { repositorio.eliminar(d) }
    }

    companion object {
        val TIPOS = listOf("Teléfono", "Tablet", "Computador")
        val SISTEMAS = listOf("Android", "iOS", "Windows", "macOS")

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TioTecApp
                DispositivoViewModel(app.dispositivoRepository)
            }
        }
    }
}
