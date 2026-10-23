package cl.duoc.tiotec.ui.screens.aprende

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cl.duoc.tiotec.data.model.Area
import cl.duoc.tiotec.data.model.ContenidoAprende

/** Contenidos educativos breves por área. Cada tarjeta se expande al tocarla. */
@Composable
fun AprendeScreen() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Aprende", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Consejos simples para cuidar tus dispositivos y cuentas. Toca un área para verlos.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        items(Area.entries) { area -> TarjetaArea(area) }
    }
}

@Composable
private fun TarjetaArea(area: Area) {
    var abierta by rememberSaveable(area.name) { mutableStateOf(false) }
    // Animación: la flecha gira 180° al abrir la tarjeta
    val giro by animateFloatAsState(targetValue = if (abierta) 180f else 0f, label = "flecha_${area.name}")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { abierta = !abierta }
    ) {
        // animateContentSize: la tarjeta crece suavemente al mostrar los consejos
        Column(
            Modifier
                .animateContentSize()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(iconoPara(area), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(area.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(
                        area.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (abierta) "Cerrar" else "Abrir",
                    modifier = Modifier.rotate(giro)
                )
            }
            if (abierta) {
                Spacer(Modifier.height(12.dp))
                ContenidoAprende.para(area).forEach { consejo ->
                    Row(Modifier.padding(vertical = 4.dp)) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(consejo, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

private fun iconoPara(area: Area): ImageVector = when (area) {
    Area.RESPALDOS -> Icons.Filled.CloudUpload
    Area.ACTUALIZACIONES -> Icons.Filled.SystemUpdate
    Area.AUTENTICACION -> Icons.Filled.Lock
    Area.RECUPERACION -> Icons.Filled.Restore
}
