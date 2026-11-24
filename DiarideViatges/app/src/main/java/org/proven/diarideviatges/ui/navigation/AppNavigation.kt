package org.proven.diarideviatges.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.proven.diarideviatges.ui.screens.FormScreen
import org.proven.diarideviatges.ui.screens.HistoryScreen
import org.proven.diarideviatges.ui.screens.PostalScreen
import org.proven.diarideviatges.viewmodel.TravelDiaryViewModel

object NavRoutes {
    const val FORM = "form"
    const val POSTAL = "postal"
    const val HISTORY = "history"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: TravelDiaryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Carrega l'històric privat al iniciar l'aplicació
    LaunchedEffect(Unit) {
        viewModel.loadRoutes(context)
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.FORM
    ) {
        // Pantalla 1: Formulari de Nova Ruta
        composable(NavRoutes.FORM) {
            FormScreen(
                viewModel = viewModel,
                uiState = uiState,
                onNavigateToPostal = {
                    navController.navigate(NavRoutes.POSTAL)
                },
                onNavigateToHistory = {
                    navController.navigate(NavRoutes.HISTORY)
                }
            )
        }

        // Pantalla 2: Targeta Visual de Viatge / Postal
        composable(NavRoutes.POSTAL) {
            PostalScreen(
                viewModel = viewModel,
                route = uiState.selectedRoute,
                onNavigateBack = {
                    navController.popBackStack(NavRoutes.FORM, inclusive = false)
                },
                onNavigateToHistory = {
                    navController.navigate(NavRoutes.HISTORY)
                }
            )
        }

        // Pantalla 3: Històric de Rutes Guardades
        composable(NavRoutes.HISTORY) {
            HistoryScreen(
                viewModel = viewModel,
                uiState = uiState,
                onSelectRoute = {
                    navController.navigate(NavRoutes.POSTAL)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
