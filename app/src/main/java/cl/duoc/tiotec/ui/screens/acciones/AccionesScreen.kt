package cl.duoc.tiotec.ui.screens.acciones

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tiotec.data.local.AccionEntity
import cl.duoc.tiotec.data.model.Prioridad
import cl.duoc.tiotec.ui.components.Etiqueta
import cl.duoc.tiotec.ui.components.colorPara
import cl.duoc.tiotec.utils.Fechas
import cl.duoc.tiotec.viewmodel.AccionesViewModel
import cl.duoc.tiotec.viewmodel.FiltroAcciones

/** Plan de acciones: checklist priorizado que se marca como completado. */
@Composable
fun AccionesScreen(
    viewModel: AccionesViewModel = viewModel(factory = AccionesViewModel.Factory)
) {
    val acciones by viewModel.acciones.collectAsStateWithLifecycle()
    val filtro by viewModel.filtro.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {
            Text("Plan de acciones", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Marca cada acción cuando la completes.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FiltroAcciones.entries.forEach { opcion ->
                    FilterChip(
                        selected = opcion == filtro,
                        onClick = { viewModel.cambiarFiltro(opcion) },
                        label = { Text(opcion.etiqueta) }
                    )
                }
            }
        }

        if (acciones.isEmpty()) {
            Text(
                if (filtro == FiltroAcciones.PENDIENTES) "No tienes acciones pendientes." else "No hay acciones para mostrar.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(acciones, key = { it.id }) { accion ->
                    TarjetaAccion(
                        accion = accion,
                        onCambio = { marcada -> viewModel.cambiarEstado(accion, marcada) },
                        // Animación: la tarjeta se desliza al aparecer o desaparecer de la lista
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaAccion(accion: AccionEntity, onCambio: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    // Animación: al completarla, la tarjeta cambia de color suavemente
    val fondo by animateColorAsState(
        targetValue = if (accion.completada) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "fondo_accion"
    )
    val prioridad = Prioridad.desdeOrden(accion.prioridad)

    Card(
        colors = CardDefaults.cardColors(containerColor = fondo),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Checkbox(checked = accion.completada, onCheckedChange = onCambio)
            Column(Modifier.weight(1f)) {
                Text(
                    accion.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (accion.completada) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(accion.detalle, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Etiqueta("Prioridad ${prioridad.etiqueta}", colorPara(prioridad))
                    Spacer(Modifier.weight(1f))
                    Text(
                        "Revisar: ${Fechas.formatear(accion.fechaRevision)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
