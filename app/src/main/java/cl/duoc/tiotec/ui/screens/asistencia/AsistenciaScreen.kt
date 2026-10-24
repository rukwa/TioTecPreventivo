package cl.duoc.tiotec.ui.screens.asistencia

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tiotec.data.local.SolicitudEntity
import cl.duoc.tiotec.ui.components.CampoConError
import cl.duoc.tiotec.ui.components.SelectorOpciones
import cl.duoc.tiotec.utils.Fechas
import cl.duoc.tiotec.viewmodel.AsistenciaViewModel
import kotlinx.coroutines.delay

/** Formulario 3: solicitud de asistencia técnica, con estado simulado. */
@Composable
fun AsistenciaScreen(
    viewModel: AsistenciaViewModel = viewModel(factory = AsistenciaViewModel.Factory)
) {
    val ui by viewModel.ui.collectAsStateWithLifecycle()
    val historial by viewModel.historial.collectAsStateWithLifecycle()
    val opcionesDispositivo by viewModel.opcionesDispositivo.collectAsStateWithLifecycle()

    // Reloj que avanza cada 15 segundos para que el estado simulado se vaya actualizando
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(15_000)
            ahora = System.currentTimeMillis()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Solicitar asistencia", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Cuéntanos qué necesitas y un técnico de TíoTec te contactará (simulado).",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SelectorOpciones(
            titulo = "Categoría",
            opciones = AsistenciaViewModel.CATEGORIAS,
            seleccion = ui.categoria,
            onSeleccion = viewModel::onCategoriaChange,
            error = ui.errorCategoria
        )
        SelectorOpciones(
            titulo = "Dispositivo",
            opciones = opcionesDispositivo,
            seleccion = ui.dispositivo,
            onSeleccion = viewModel::onDispositivoChange,
            error = ui.errorDispositivo
        )
        CampoConError(
            valor = ui.descripcion,
            onValorChange = viewModel::onDescripcionChange,
            etiqueta = "Describe el problema",
            error = ui.errorDescripcion,
            lineas = 4,
            ayuda = "${ui.descripcion.trim().length}/${AsistenciaViewModel.LARGO_MINIMO} caracteres mínimos"
        )

        Button(
            onClick = viewModel::enviar,
            enabled = !ui.enviando,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (ui.enviando) {
                CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
            } else {
                Text("Enviar solicitud")
            }
        }

        // Confirmación animada después de enviar
        AnimatedVisibility(visible = ui.enviada) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Solicitud enviada. Puedes ver su estado más abajo.")
                }
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Text("Mis solicitudes", style = MaterialTheme.typography.titleLarge)

        if (historial.isEmpty()) {
            Text("Todavía no envías solicitudes.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            historial.forEach { solicitud -> TarjetaSolicitud(solicitud, ahora) }
        }
    }
}

@Composable
private fun TarjetaSolicitud(solicitud: SolicitudEntity, ahora: Long) {
    val estado = AsistenciaViewModel.estadoSimulado(solicitud.fecha, ahora)
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(
                "${solicitud.categoria} · ${solicitud.dispositivo}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                solicitud.descripcion,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    Fechas.formatear(solicitud.fecha),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                EtiquetaEstado(estado)
            }
        }
    }
}

@Composable
private fun EtiquetaEstado(estado: String) {
    val color = when (estado) {
        "Enviada" -> MaterialTheme.colorScheme.tertiaryContainer
        "En revisión" -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    Surface(shape = RoundedCornerShape(50), color = color) {
        Text(
            estado,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
