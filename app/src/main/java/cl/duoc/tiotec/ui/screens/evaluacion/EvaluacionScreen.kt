package cl.duoc.tiotec.ui.screens.evaluacion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tiotec.data.model.BancoPreguntas
import cl.duoc.tiotec.data.model.Pregunta
import cl.duoc.tiotec.ui.components.MensajeError
import cl.duoc.tiotec.viewmodel.EvaluacionViewModel

/** Cuestionario de 12 preguntas con barra de progreso animada. */
@Composable
fun EvaluacionScreen(
    alTerminar: (Long) -> Unit,
    viewModel: EvaluacionViewModel = viewModel(factory = EvaluacionViewModel.Factory)
) {
    val ui by viewModel.ui.collectAsStateWithLifecycle()

    // Animación: la barra avanza suavemente cada vez que se responde una pregunta
    val progreso by animateFloatAsState(
        targetValue = ui.respondidas / ui.total.toFloat(),
        animationSpec = tween(400),
        label = "progreso"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Evaluación preventiva", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Pregunta ${ui.indice + 1} de ${ui.total} · ${ui.respondidas} respondidas",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        // Animación: la pregunta entra deslizándose (hacia la izquierda si avanza, a la derecha si retrocede)
        AnimatedContent(
            targetState = ui.indice,
            transitionSpec = {
                val haciaAdelante = targetState > initialState
                (slideInHorizontally { ancho -> if (haciaAdelante) ancho else -ancho } + fadeIn()) togetherWith
                    (slideOutHorizontally { ancho -> if (haciaAdelante) -ancho else ancho } + fadeOut())
            },
            label = "pregunta",
            modifier = Modifier.weight(1f)
        ) { indice ->
            val pregunta = BancoPreguntas.preguntas[indice]
            TarjetaPregunta(
                pregunta = pregunta,
                seleccion = ui.respuestas[pregunta.id],
                onResponder = viewModel::responder
            )
        }

        MensajeError(if (ui.mostrarFaltantes) "Te faltan ${ui.faltantes} preguntas por responder" else null)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = viewModel::anterior,
                enabled = ui.indice > 0,
                modifier = Modifier.weight(1f)
            ) { Text("Anterior") }

            if (ui.esUltima) {
                Button(
                    onClick = { viewModel.finalizar(alTerminar) },
                    enabled = !ui.guardando,
                    modifier = Modifier.weight(1f)
                ) {
                    if (ui.guardando) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Ver resultado")
                    }
                }
            } else {
                Button(
                    onClick = viewModel::siguiente,
                    enabled = ui.preguntaActualRespondida,
                    modifier = Modifier.weight(1f)
                ) { Text("Siguiente") }
            }
        }
    }
}

@Composable
private fun TarjetaPregunta(pregunta: Pregunta, seleccion: Int?, onResponder: (Int) -> Unit) {
    Card(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                pregunta.area.titulo.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(pregunta.enunciado, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            pregunta.opciones.forEach { opcion ->
                val elegida = seleccion == opcion.puntaje
                // Animación: la opción elegida cambia de color suavemente
                val fondo by animateColorAsState(
                    targetValue = if (elegida) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    label = "opcion"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(fondo)
                        .selectable(
                            selected = elegida,
                            onClick = { onResponder(opcion.puntaje) },
                            role = Role.RadioButton
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = elegida, onClick = null)
                    Spacer(Modifier.width(8.dp))
                    Text(opcion.texto, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
