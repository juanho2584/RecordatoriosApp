package com.pinto.recordatorios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pinto.recordatorios.ui.screens.create.CrearRecordatorioScreen
import com.pinto.recordatorios.ui.screens.detail.DetalleRecordatorioScreen
import com.pinto.recordatorios.ui.screens.list.ListaRecordatoriosScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // CONSIGNA 5: Grafo de navegación
    NavHost(navController = navController, startDestination = "lista") {

        composable("lista") {
            ListaRecordatoriosScreen(
                onCrearClick = { navController.navigate("crear") },
                onRecordatorioClick = { id -> navController.navigate("detalle/$id") }
            )
        }

        composable("crear") {
            CrearRecordatorioScreen(
                // Usamos popBackStack para no acumular pantallas en el historial
                onGuardado = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        // CONSIGNA 5: Destino detalle que recibe el identificador
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            DetalleRecordatorioScreen(
                id = id,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}