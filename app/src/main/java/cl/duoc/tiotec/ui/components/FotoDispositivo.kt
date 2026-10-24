package cl.duoc.tiotec.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import cl.duoc.tiotec.utils.FotoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Foto del dispositivo con tamaño fijo y bordes redondeados (pauta IE 2.4.2).
 * - ContentScale.Crop recorta la foto para llenar el cuadrado sin deformarla.
 * - clip(RoundedCornerShape) redondea las esquinas.
 * Si no hay foto, muestra un ícono de teléfono.
 */
@Composable
fun FotoDispositivo(ruta: String?, tamano: Dp, modifier: Modifier = Modifier) {
    // La foto se lee del disco en segundo plano (Dispatchers.IO) para no trabar la pantalla
    val imagen by produceState<ImageBitmap?>(initialValue = null, ruta) {
        value = if (ruta == null) null else withContext(Dispatchers.IO) { FotoUtils.cargar(ruta) }
    }
    val forma = RoundedCornerShape(16)
    val foto = imagen

    if (foto != null) {
        Image(
            bitmap = foto,
            contentDescription = "Foto del dispositivo",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(tamano)
                .clip(forma)
        )
    } else {
        Surface(
            modifier = modifier.size(tamano),
            shape = forma,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.PhoneAndroid,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(tamano / 2)
                )
            }
        }
    }
}
