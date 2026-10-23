package cl.duoc.tiotec.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta TíoTec
// Azul = confianza (color principal), verde = seguro, ámbar = atención, rojo = riesgo.
// Los tonos "Claro" se usan como fondo de tarjetas y contenedores.

val AzulTioTec = Color(0xFF1F5FBF)
val AzulTioTecClaro = Color(0xFFD8E2FF)
val AzulTioTecOscuro = Color(0xFF001A43)

val VerdeSeguro = Color(0xFF2E7D32)
val VerdeSeguroClaro = Color(0xFFC8E6C9)
val VerdeSeguroOscuro = Color(0xFF002106)

val AmbarAlerta = Color(0xFFB26A00)
val AmbarAlertaClaro = Color(0xFFFFDDB3)
val AmbarAlertaOscuro = Color(0xFF2A1800)

val RojoRiesgo = Color(0xFFBA1A1A)

val FondoClaro = Color(0xFFF8F9FF)
val TextoClaro = Color(0xFF191C20)
val FondoOscuro = Color(0xFF111318)
val TextoOscuro = Color(0xFFE1E2E9)

// Colores para el nivel de riesgo en la pantalla de Resultado
val RiesgoBajo = VerdeSeguro
val RiesgoMedio = AmbarAlerta
val RiesgoAlto = RojoRiesgo
