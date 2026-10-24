package cl.duoc.tiotec.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import cl.duoc.tiotec.ui.screens.acciones.AccionesScreen
import cl.duoc.tiotec.ui.screens.aprende.AprendeScreen
import cl.duoc.tiotec.ui.screens.asistencia.AsistenciaScreen
import cl.duoc.tiotec.ui.screens.dispositivos.DispositivosScreen
import cl.duoc.tiotec.ui.screens.evaluacion.EvaluacionScreen
import cl.duoc.tiotec.ui.screens.historial.HistorialScreen
import cl.duoc.tiotec.ui.screens.inicio.InicioScreen
import cl.duoc.tiotec.ui.screens.perfil.PerfilScreen
import cl.duoc.tiotec.ui.screens.resultado.ResultadoScreen

/**
 * Punto central de navegación.
 * - NavHost decide qué pantalla mostrar según la ruta actual.
 * - La barra inferior solo aparece en las 5 secciones principales.
 * - Entre pantallas hay una transición de fundido (enterTransition / exitTransition).
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
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Ruta.Perfil.ruta) {
                PerfilScreen(
                    alTerminar = {
                        // popUpTo con inclusive = true: al volver atrás desde Inicio
                        // la app se cierra en vez de regresar al formulario de perfil.
                        navController.navigate(Ruta.Inicio.ruta) {
                            popUpTo(Ruta.Perfil.ruta) { inclusive = true }
                        }
                    }
                )
            }

            composable(Ruta.Inicio.ruta) {
                InicioScreen(
                    irAEvaluar = { navController.irASeccion(Ruta.Evaluacion.ruta) },
                    irAAcciones = { navController.irASeccion(Ruta.Acciones.ruta) },
                    irAHistorial = { navController.navigate(Ruta.Historial.ruta) },
                    irAAsistencia = { navController.navigate(Ruta.Asistencia.ruta) }
                )
            }

            composable(Ruta.Evaluacion.ruta) {
                EvaluacionScreen(
                    alTerminar = { id -> navController.navigate(Ruta.Resultado.crear(id)) }
                )
            }

            // Ruta con argumento: recibe el id de la evaluación que se acaba de guardar
            composable(
                route = Ruta.Resultado.ruta,
                arguments = listOf(navArgument("evaluacionId") { type = NavType.LongType })
            ) { entrada ->
                val evaluacionId = entrada.arguments?.getLong("evaluacionId") ?: 0L
                ResultadoScreen(
                    evaluacionId = evaluacionId,
                    verAcciones = { navController.irASeccion(Ruta.Acciones.ruta) },
                    volverAlInicio = { navController.irASeccion(Ruta.Inicio.ruta) }
                )
            }

            composable(Ruta.Acciones.ruta) { AccionesScreen() }
            composable(Ruta.Historial.ruta) {
                HistorialScreen(verResultado = { id -> navController.navigate(Ruta.Resultado.crear(id)) })
            }
            composable(Ruta.Dispositivos.ruta) { DispositivosScreen() }
            composable(Ruta.Aprende.ruta) { AprendeScreen() }
            composable(Ruta.Asistencia.ruta) { AsistenciaScreen() }
        }
    }
}

/** Navega a una sección principal sin apilar la misma pantalla muchas veces. */
fun NavHostController.irASeccion(ruta: String) {
    navigate(ruta) {
        popUpTo(Ruta.Inicio.ruta) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun BarraInferior(navController: NavHostController, rutaActual: String?) {
    NavigationBar {
        itemsBarraInferior.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta.ruta,
                onClick = { navController.irASeccion(item.ruta.ruta) },
                icon = { Icon(imageVector = item.icono, contentDescription = item.ruta.titulo) },
                label = { Text(item.ruta.titulo) }
            )
        }
    }
}
