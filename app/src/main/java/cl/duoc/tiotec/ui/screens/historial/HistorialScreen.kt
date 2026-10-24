package cl.duoc.tiotec.ui.screens.historial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tiotec.data.local.EvaluacionEntity
import cl.duoc.tiotec.ui.components.Etiqueta
import cl.duoc.tiotec.ui.components.colorPara
import cl.duoc.tiotec.utils.CalculadoraRiesgo
import cl.duoc.tiotec.utils.Fechas
import cl.duoc.tiotec.viewmodel.HistorialViewModel

/** Historial de evaluaciones guardadas en Room. Tocar una abre su resultado. */
@Composable
fun HistorialScreen(
    verResultado: (Long) -> Unit,
    viewModel: HistorialViewModel = viewModel(factory = HistorialViewModel.Factory)
) {
    val evaluaciones by viewModel.evaluaciones.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("Historial", style = MaterialTheme.typography.headlineMedium) }
        if (evaluaciones.isEmpty()) {
            item {
                Text(
                    "Todavía no tienes evaluaciones guardadas.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        items(evaluaciones, key = { it.id }) { evaluacion ->
            TarjetaEvaluacion(
                evaluacion = evaluacion,
                onAbrir = { verResultado(evaluacion.id) },
                onEliminar = { viewModel.eliminar(evaluacion) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun TarjetaEvaluacion(
    evaluacion: EvaluacionEntity,
    onAbrir: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nivel = CalculadoraRiesgo.nivelPara(evaluacion.puntajeTotal)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onAbrir)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${evaluacion.puntajeTotal}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colorPara(nivel)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(Fechas.formatear(evaluacion.fecha), style = MaterialTheme.typography.titleMedium)
                Etiqueta("Riesgo ${nivel.etiqueta}", colorPara(nivel))
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar evaluación")
            }
        }
    }
}
