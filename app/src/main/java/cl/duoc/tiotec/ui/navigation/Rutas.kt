package cl.duoc.tiotec.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Todas las rutas de la app en un solo lugar.
 * Así nunca escribimos el texto de una ruta "a mano" en otra parte del código.
 */
sealed class Ruta(val ruta: String, val titulo: String) {
    data object Perfil : Ruta("perfil", "Crear perfil")
    data object Inicio : Ruta("inicio", "Inicio")
    data object Evaluacion : Ruta("evaluacion", "Evaluar")
    data object Resultado : Ruta("resultado/{evaluacionId}", "Resultado") {
        // Arma la ruta con el id real, por ejemplo "resultado/5"
        fun crear(evaluacionId: Long) = "resultado/$evaluacionId"
    }
    data object Acciones : Ruta("acciones", "Acciones")
    data object Historial : Ruta("historial", "Historial")
    data object Dispositivos : Ruta("dispositivos", "Dispositivos")
    data object Aprende : Ruta("aprende", "Aprende")
    data object Asistencia : Ruta("asistencia", "Asistencia")
}

/** Un botón de la barra inferior: a qué ruta lleva y qué ícono muestra. */
data class ItemBarra(val ruta: Ruta, val icono: ImageVector)

/** Las 5 secciones principales que aparecen en la barra inferior. */
val itemsBarraInferior = listOf(
    ItemBarra(Ruta.Inicio, Icons.Filled.Home),
    ItemBarra(Ruta.Evaluacion, Icons.Filled.Shield),
    ItemBarra(Ruta.Acciones, Icons.Filled.Checklist),
    ItemBarra(Ruta.Dispositivos, Icons.Filled.PhoneAndroid),
    ItemBarra(Ruta.Aprende, Icons.Filled.School)
)
