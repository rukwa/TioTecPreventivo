package cl.duoc.tiotec.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Un botón que muestra la pantalla provisoria: texto + qué hace al presionarlo. */
data class AccionProvisoria(val texto: String, val alPresionar: () -> Unit)

/**
 * Pantalla provisoria mientras construimos cada sección.
 * Cada vez que terminamos una pantalla real, la reemplazamos en AppNavigation.
 * El primer botón se muestra relleno (acción principal) y el resto con borde.
 */
@Composable
fun PantallaEnConstruccion(
    titulo: String,
    modifier: Modifier = Modifier,
    acciones: List<AccionProvisoria> = emptyList()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Construction,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Pantalla en construcción",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        acciones.forEachIndexed { indice, accion ->
            Spacer(Modifier.height(if (indice == 0) 24.dp else 8.dp))
            if (indice == 0) {
                Button(onClick = accion.alPresionar) { Text(accion.texto) }
            } else {
                OutlinedButton(onClick = accion.alPresionar) { Text(accion.texto) }
            }
        }
    }
}
