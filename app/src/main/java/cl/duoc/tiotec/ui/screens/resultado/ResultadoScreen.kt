package cl.duoc.tiotec.ui.screens.resultado

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tiotec.data.model.Area
import cl.duoc.tiotec.ui.components.Etiqueta
import cl.duoc.tiotec.ui.components.colorPara
import cl.duoc.tiotec.utils.CalculadoraRiesgo
import cl.duoc.tiotec.viewmodel.ResultadoViewModel

/** Resultado de la evaluación: puntaje que "cuenta" hasta su valor y barras por área animadas. */
@Composable
fun ResultadoScreen(
    evaluacionId: Long,
    verAcciones: () -> Unit,
    volverAlInicio: () -> Unit
) {
    val viewModel: ResultadoViewModel = viewModel(
        key = "resultado_$evaluacionId",
        factory = ResultadoViewModel.factory(evaluacionId)
    )
    val evaluacion by viewModel.evaluacion.collectAsStateWithLifecycle()
    val recomendaciones by viewModel.recomendaciones.collectAsStateWithLifecycle()

    val e = evaluacion
    if (e == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    // Las animaciones parten en 0 y suben hasta el valor real apenas se muestra la pantalla
    var animar by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animar = true }
    val puntaje by animateIntAsState(
        targetValue = if (animar) e.puntajeTotal else 0,
        animationSpec = tween(1200),
        label = "puntaje"
    )
    val nivel = CalculadoraRiesgo.nivelPara(e.puntajeTotal)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tu resultado", style = MaterialTheme.typography.headlineMedium)

        Card(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "$puntaje",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPara(nivel)
                )
                Text("de 100 puntos", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Etiqueta("Riesgo ${nivel.etiqueta}", colorPara(nivel), Modifier.padding(top = 8.dp))
            }
        }

        Text("Por área", style = MaterialTheme.typography.titleLarge)
        BarraArea(Area.RESPALDOS.titulo, e.puntajeRespaldos, animar)
        BarraArea(Area.ACTUALIZACIONES.titulo, e.puntajeActualizaciones, animar)
        BarraArea(Area.AUTENTICACION.titulo, e.puntajeAutenticacion, animar)
        BarraArea(Area.RECUPERACION.titulo, e.puntajeRecuperacion, animar)

        Text("Recomendaciones", style = MaterialTheme.typography.titleLarge)
        if (recomendaciones.isEmpty()) {
            Text("¡Excelente! No tienes acciones pendientes.")
        } else {
            recomendaciones.take(3).forEach { Text("• ${it.titulo}", style = MaterialTheme.typography.bodyLarge) }
            if (recomendaciones.size > 3) {
                Text(
                    "y ${recomendaciones.size - 3} más en tu plan de acciones",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(onClick = verAcciones, modifier = Modifier.fillMaxWidth()) {
            Text("Ver plan de acciones (${recomendaciones.size})")
        }
        OutlinedButton(onClick = volverAlInicio, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al inicio")
        }
    }
}

@Composable
private fun BarraArea(titulo: String, puntaje: Int, animar: Boolean) {
    val fraccion by animateFloatAsState(
        targetValue = if (animar) puntaje / 100f else 0f,
        animationSpec = tween(1000),
        label = "barra_$titulo"
    )
    Column {
        Row {
            Text(titulo, modifier = Modifier.weight(1f))
            Text("$puntaje%", fontWeight = FontWeight.SemiBold)
        }
        LinearProgressIndicator(
            progress = { fraccion },
            color = colorPara(CalculadoraRiesgo.nivelPara(puntaje)),
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
        )
    }
}
