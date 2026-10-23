package cl.duoc.tiotec.ui.screens.dispositivos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tiotec.data.local.DispositivoEntity
import cl.duoc.tiotec.ui.components.CampoConError
import cl.duoc.tiotec.ui.components.FotoDispositivo
import cl.duoc.tiotec.ui.components.SelectorOpciones
import cl.duoc.tiotec.utils.FotoUtils
import cl.duoc.tiotec.viewmodel.DispositivoViewModel
import cl.duoc.tiotec.viewmodel.FormularioDispositivo

/** Formulario 2: dispositivos de prueba (listar, agregar, editar, borrar) con foto. */
@Composable
fun DispositivosScreen(
    viewModel: DispositivoViewModel = viewModel(factory = DispositivoViewModel.Factory)
) {
    val dispositivos by viewModel.dispositivos.collectAsStateWithLifecycle()
    val ui by viewModel.ui.collectAsStateWithLifecycle()

    // Animación: cambia entre la lista y el formulario con un fundido
    Crossfade(targetState = ui.formulario != null, label = "lista_formulario") { mostrarFormulario ->
        val formulario = ui.formulario
        if (mostrarFormulario && formulario != null) {
            EditorDispositivo(
                f = formulario,
                guardando = ui.guardando,
                viewModel = viewModel
            )
        } else {
            ListaDispositivos(
                dispositivos = dispositivos,
                onNuevo = viewModel::nuevo,
                onEditar = viewModel::editar,
                onEliminar = viewModel::eliminar
            )
        }
    }
}

@Composable
private fun ListaDispositivos(
    dispositivos: List<DispositivoEntity>,
    onNuevo: () -> Unit,
    onEditar: (DispositivoEntity) -> Unit,
    onEliminar: (DispositivoEntity) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        if (dispositivos.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FotoDispositivo(ruta = null, tamano = 96.dp)
                Spacer(Modifier.height(16.dp))
                Text("Aún no registras dispositivos", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Presiona + para agregar tu primer dispositivo de prueba.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 88.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Text("Mis dispositivos", style = MaterialTheme.typography.headlineMedium) }
                items(dispositivos, key = { it.id }) { d ->
                    TarjetaDispositivo(d, onEditar = { onEditar(d) }, onEliminar = { onEliminar(d) })
                }
            }
        }
        FloatingActionButton(
            onClick = onNuevo,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar dispositivo")
        }
    }
}

@Composable
private fun TarjetaDispositivo(d: DispositivoEntity, onEditar: () -> Unit, onEliminar: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FotoDispositivo(ruta = d.fotoRuta, tamano = 64.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(d.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${d.tipo} · ${d.sistema} ${d.version}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEditar) { Icon(Icons.Filled.Edit, contentDescription = "Editar") }
            IconButton(onClick = onEliminar) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
        }
    }
}

@Composable
private fun EditorDispositivo(
    f: FormularioDispositivo,
    guardando: Boolean,
    viewModel: DispositivoViewModel
) {
    // El botón "atrás" del teléfono cierra el formulario en vez de salir de la app
    BackHandler(onBack = viewModel::cerrarFormulario)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            if (f.esNuevo) "Nuevo dispositivo" else "Editar dispositivo",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            "Registra un dispositivo de prueba con datos ficticios.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SeccionFoto(fotoRuta = f.fotoRuta, onFoto = viewModel::onFotoChange)

        CampoConError(
            valor = f.nombre,
            onValorChange = viewModel::onNombreChange,
            etiqueta = "Nombre del dispositivo",
            error = f.errorNombre,
            ayuda = "Ej: Teléfono de prueba 1"
        )
        SelectorOpciones(
            titulo = "Tipo",
            opciones = DispositivoViewModel.TIPOS,
            seleccion = f.tipo,
            onSeleccion = viewModel::onTipoChange,
            error = f.errorTipo
        )
        SelectorOpciones(
            titulo = "Sistema operativo",
            opciones = DispositivoViewModel.SISTEMAS,
            seleccion = f.sistema,
            onSeleccion = viewModel::onSistemaChange,
            error = f.errorSistema
        )
        CampoConError(
            valor = f.version,
            onValorChange = viewModel::onVersionChange,
            etiqueta = "Versión del sistema",
            error = f.errorVersion,
            tipoTeclado = KeyboardType.Decimal,
            ayuda = "Ej: 14 o 17.2"
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = viewModel::cerrarFormulario, modifier = Modifier.weight(1f)) {
                Text("Cancelar")
            }
            Button(
                onClick = viewModel::guardar,
                enabled = !guardando,
                modifier = Modifier.weight(1f)
            ) {
                if (guardando) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}

/**
 * Recursos nativos: cámara y galería.
 * - Cámara: pide el permiso CAMERA en tiempo de ejecución; si se rechaza, lo explica
 *   y ofrece abrir los Ajustes.
 * - Galería: usa el Photo Picker de Android, que NO necesita permisos.
 */
@Composable
private fun SeccionFoto(fotoRuta: String?, onFoto: (String) -> Unit) {
    val context = LocalContext.current
    var rutaPendiente by rememberSaveable { mutableStateOf<String?>(null) }
    var permisoDenegado by rememberSaveable { mutableStateOf(false) }

    // 1) La cámara guarda la foto en el archivo que le entregamos; devuelve true si se tomó
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        val ruta = rutaPendiente
        if (exito && ruta != null) onFoto(ruta)
        rutaPendiente = null
    }

    fun abrirCamara() {
        val archivo = FotoUtils.crearArchivo(context)
        rutaPendiente = archivo.absolutePath
        camara.launch(FotoUtils.uriPara(context, archivo))
    }

    // 2) Pide el permiso de cámara y, si lo conceden, abre la cámara
    val permisoCamara = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        permisoDenegado = !concedido
        if (concedido) abrirCamara()
    }

    // 3) Galería: devuelve la imagen elegida (o null si el usuario cancela)
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) FotoUtils.copiarAInterno(context, uri)?.let(onFoto)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FotoDispositivo(ruta = fotoRuta, tamano = 120.dp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                val tienePermiso = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                if (tienePermiso) abrirCamara() else permisoCamara.launch(Manifest.permission.CAMERA)
            }) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cámara")
            }
            OutlinedButton(onClick = {
                galeria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Galería")
            }
        }
        AnimatedVisibility(visible = permisoDenegado) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Sin permiso de cámara no se puede tomar la foto. Puedes usar la galería o activar el permiso en Ajustes.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    context.startActivity(intent)
                }) {
                    Text("Abrir ajustes")
                }
            }
        }
    }
}
