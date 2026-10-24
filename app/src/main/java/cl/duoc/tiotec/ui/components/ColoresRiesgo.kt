package cl.duoc.tiotec.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.duoc.tiotec.data.model.Prioridad
import cl.duoc.tiotec.ui.theme.RiesgoAlto
import cl.duoc.tiotec.ui.theme.RiesgoBajo
import cl.duoc.tiotec.ui.theme.RiesgoMedio
import cl.duoc.tiotec.utils.NivelRiesgo

/** Verde = riesgo bajo, ámbar = medio, rojo = alto (colores definidos en Color.kt). */
fun colorPara(nivel: NivelRiesgo): Color = when (nivel) {
    NivelRiesgo.BAJO -> RiesgoBajo
    NivelRiesgo.MEDIO -> RiesgoMedio
    NivelRiesgo.ALTO -> RiesgoAlto
}

fun colorPara(prioridad: Prioridad): Color = when (prioridad) {
    Prioridad.ALTA -> RiesgoAlto
    Prioridad.MEDIA -> RiesgoMedio
    Prioridad.BAJA -> RiesgoBajo
}

/** Etiqueta redondeada con texto blanco sobre un color. */
@Composable
fun Etiqueta(texto: String, color: Color, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(50), color = color, modifier = modifier) {
        Text(
            texto,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
