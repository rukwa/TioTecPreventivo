package cl.duoc.tiotec.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Esquema de colores Material 3 para modo claro
private val EsquemaClaro = lightColorScheme(
    primary = AzulTioTec,
    onPrimary = Color.White,
    primaryContainer = AzulTioTecClaro,
    onPrimaryContainer = AzulTioTecOscuro,
    secondary = VerdeSeguro,
    onSecondary = Color.White,
    secondaryContainer = VerdeSeguroClaro,
    onSecondaryContainer = VerdeSeguroOscuro,
    tertiary = AmbarAlerta,
    onTertiary = Color.White,
    tertiaryContainer = AmbarAlertaClaro,
    onTertiaryContainer = AmbarAlertaOscuro,
    error = RojoRiesgo,
    onError = Color.White,
    background = FondoClaro,
    onBackground = TextoClaro,
    surface = FondoClaro,
    onSurface = TextoClaro
)

// Esquema de colores Material 3 para modo oscuro (tonos más claros sobre fondo oscuro)
private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFFADC6FF),
    onPrimary = Color(0xFF002E69),
    primaryContainer = Color(0xFF004494),
    onPrimaryContainer = AzulTioTecClaro,
    secondary = Color(0xFF9CD69B),
    onSecondary = Color(0xFF00390F),
    secondaryContainer = Color(0xFF15521F),
    onSecondaryContainer = VerdeSeguroClaro,
    tertiary = Color(0xFFFFB955),
    onTertiary = Color(0xFF462A00),
    tertiaryContainer = Color(0xFF643F00),
    onTertiaryContainer = AmbarAlertaClaro,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    background = FondoOscuro,
    onBackground = TextoOscuro,
    surface = FondoOscuro,
    onSurface = TextoOscuro
)

/**
 * Tema de la app. No usamos "dynamic color" (colores del fondo de pantalla del usuario)
 * para que la identidad de TíoTec y los colores de riesgo se vean siempre iguales.
 */
@Composable
fun TioTecPreventivoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
