package com.duoc.tiotecpreventivo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.tiotecpreventivo.ui.screens.EvaluationScreen
import com.duoc.tiotecpreventivo.ui.screens.HomeScreen
import com.duoc.tiotecpreventivo.viewmodel.EvaluationViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Evaluation : Screen("evaluation")
}

@Composable
fun AppNavigation(evaluationViewModel: EvaluationViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToEvaluation = { navController.navigate(Screen.Evaluation.route) }
            )
        }
        composable(Screen.Evaluation.route) {
            EvaluationScreen(
                viewModel = evaluationViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}