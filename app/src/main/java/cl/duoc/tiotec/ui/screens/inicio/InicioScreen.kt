package cl.duoc.tiotec.ui.screens.inicio

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import cl.duoc.tiotec.utils.Notificaciones
import cl.duoc.tiotec.viewmodel.InicioViewModel

/** Inicio: último puntaje, acciones pendientes, próxima revisión y accesos rápidos. */
@Composable
fun InicioScreen(
    irAEvaluar: () -> Unit,
    irAAcciones: () -> Unit,
    irAHistorial: () -> Unit,
    irAAsistencia: () -> Unit,
    viewModel: InicioViewModel = viewModel(factory = InicioViewModel.Factory)
) {
    val ui by viewModel.ui.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            if (ui.nombre.isBlank()) "Hola" else "Hola, ${ui.nombre}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            "Revisa y mejora tu seguridad digital paso a paso.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        val ultima = ui.ultima
        if (ultima == null) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Aún no tienes evaluaciones", style = MaterialTheme.typography.titleMedium)
                    Text("Haz tu primera evaluación para conocer tu nivel de riesgo.")
                }
            }
        } else {
            Resumen(ultima = ultima, pendientes = ui.pendientes)
        }

        Button(onClick = irAEvaluar, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.Shield, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(if (ultima == null) "Hacer mi primera evaluación" else "Nueva evaluación")
        }
        OutlinedButton(onClick = irAAcciones, modifier = Modifier.fillMaxWidth()) {
            Text("Ver plan de acciones")
        }
        OutlinedButton(onClick = irAHistorial, modifier = Modifier.fillMaxWidth()) {
            Text("Historial de evaluaciones")
        }
        OutlinedButton(onClick = irAAsistencia, modifier = Modifier.fillMaxWidth()) {
            Text("Solicitar asistencia")
        }

        TarjetaRecordatorio()
    }
}

@Composable
private fun Resumen(ultima: EvaluacionEntity, pendientes: Int) {
    val nivel = CalculadoraRiesgo.nivelPara(ultima.puntajeTotal)
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp)) {
            Column(Modifier.weight(1f)) {
                Text("Último puntaje", style = MaterialTheme.typography.labelLarge)
                Text(
                    "${ultima.puntajeTotal}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPara(nivel)
                )
                Etiqueta("Riesgo ${nivel.etiqueta}", colorPara(nivel))
            }
            Column(Modifier.weight(1f)) {
                Text("Acciones pendientes", style = MaterialTheme.typography.labelLarge)
                Text("$pendientes", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Próxima revisión: ${Fechas.formatear(ultima.proximaRevision)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Recurso nativo: notificaciones.
 * En Android 13 o superior primero pide el permiso POST_NOTIFICATIONS.
 */
@Composable
private fun TarjetaRecordatorio() {
    val context = LocalContext.current
    var mensaje by remember { mutableStateOf<String?>(null) }

    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        mensaje = if (concedido) {
            Notificaciones.probar(context)
            "Listo: la notificación llegará en 10 segundos."
        } else {
            "Sin permiso de notificaciones no podemos recordarte tu revisión."
        }
    }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Recordatorio de revisión", style = MaterialTheme.typography.titleMedium)
            Text("Te avisaremos cuando toque volver a evaluarte. Prueba cómo se ve:")
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !Notificaciones.tienePermiso(context)) {
                    pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    Notificaciones.probar(context)
                    mensaje = "Listo: la notificación llegará en 10 segundos."
                }
            }) {
                Icon(Icons.Filled.Notifications, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Probar recordatorio")
            }
            AnimatedVisibility(visible = mensaje != null) {
                Text(mensaje.orEmpty(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
