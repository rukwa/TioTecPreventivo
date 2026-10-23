package cl.duoc.tiotec.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.tiotec.ui.components.AccionProvisoria
import cl.duoc.tiotec.ui.components.PantallaEnConstruccion

/**
 * Punto central de navegación.
 * - NavHost decide qué pantalla mostrar según la ruta actual.
 * - La barra inferior solo aparece en las 5 secciones principales.
 * - Por ahora cada pantalla es provisoria; se reemplaza cuando la terminamos.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val mostrarBarra = itemsBarraInferior.any { it.ruta.ruta == rutaActual }

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                BarraInferior(navController = navController, rutaActual = rutaActual)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Ruta.Perfil.ruta,
            modifier = Modifier.padding(padding)
        ) {
            composable(Ruta.Perfil.ruta) {
                PantallaEnConstruccion(
                    titulo = Ruta.Perfil.titulo,
                    acciones = listOf(
                        AccionProvisoria("Continuar") {
                            // popUpTo con inclusive = true: al volver atrás desde Inicio
                            // la app se cierra en vez de regresar al formulario de perfil.
                            navController.navigate(Ruta.Inicio.ruta) {
                                popUpTo(Ruta.Perfil.ruta) { inclusive = true }
                            }
                        }
                    )
                )
            }

            composable(Ruta.Inicio.ruta) {
                PantallaEnConstruccion(
                    titulo = Ruta.Inicio.titulo,
                    acciones = listOf(
                        AccionProvisoria("Ver historial") { navController.navigate(Ruta.Historial.ruta) },
                        AccionProvisoria("Pedir asistencia") { navController.navigate(Ruta.Asistencia.ruta) }
                    )
                )
            }

            composable(Ruta.Evaluacion.ruta) {
                PantallaEnConstruccion(
                    titulo = Ruta.Evaluacion.titulo,
                    acciones = listOf(
                        AccionProvisoria("Ver resultado de prueba") {
                            navController.navigate(Ruta.Resultado.crear(evaluacionId = 0))
                        }
                    )
                )
            }

            // Ruta con argumento: recibe el id de la evaluación que se acaba de guardar
            composable(
                route = Ruta.Resultado.ruta,
                arguments = listOf(navArgument("evaluacionId") { type = NavType.LongType })
            ) { entrada ->
                val evaluacionId = entrada.arguments?.getLong("evaluacionId") ?: 0L
                PantallaEnConstruccion(titulo = "Resultado #$evaluacionId")
            }

            composable(Ruta.Acciones.ruta) { PantallaEnConstruccion(titulo = Ruta.Acciones.titulo) }
            composable(Ruta.Historial.ruta) { PantallaEnConstruccion(titulo = Ruta.Historial.titulo) }
            composable(Ruta.Dispositivos.ruta) { PantallaEnConstruccion(titulo = Ruta.Dispositivos.titulo) }
            composable(Ruta.Aprende.ruta) { PantallaEnConstruccion(titulo = Ruta.Aprende.titulo) }
            composable(Ruta.Asistencia.ruta) { PantallaEnConstruccion(titulo = Ruta.Asistencia.titulo) }
        }
    }
}

@Composable
private fun BarraInferior(navController: NavHostController, rutaActual: String?) {
    NavigationBar {
        itemsBarraInferior.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta.ruta,
                onClick = {
                    navController.navigate(item.ruta.ruta) {
                        // Evita apilar la misma sección muchas veces al tocar la barra
                        popUpTo(Ruta.Inicio.ruta) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icono, contentDescription = item.ruta.titulo) },
                label = { Text(item.ruta.titulo) }
            )
        }
    }
}
