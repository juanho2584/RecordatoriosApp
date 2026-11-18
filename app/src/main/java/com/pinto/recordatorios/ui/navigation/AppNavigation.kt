package com.pinto.recordatorios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pinto.recordatorios.data.AppContainer
import com.pinto.recordatorios.ui.screens.create.CrearRecordatorioScreen
import com.pinto.recordatorios.ui.screens.create.CrearViewModel
import com.pinto.recordatorios.ui.screens.detail.DetalleRecordatorioScreen
import com.pinto.recordatorios.ui.screens.detail.DetalleViewModel
import com.pinto.recordatorios.ui.screens.list.ListaRecordatoriosScreen
import com.pinto.recordatorios.ui.screens.list.ListaViewModel

@Composable
fun AppNavigation(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {

        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return ListaViewModel(container.recordatoriosRepository) as T
                }
            })
            ListaRecordatoriosScreen(
                viewModel = vm,
                onCrearClick = { navController.navigate("crear") },
                onRecordatorioClick = { id -> navController.navigate("detalle/$id") }
            )
        }

        composable("crear") {
            val vm: CrearViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return CrearViewModel(container.recordatoriosRepository) as T
                }
            })
            CrearRecordatorioScreen(
                viewModel = vm,
                onGuardado = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            // Instanciamos el ViewModel
            val vm: DetalleViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return DetalleViewModel(container.recordatoriosRepository, id) as T
                }
            })

            // Le pasamos el ViewModel a la pantalla
            DetalleRecordatorioScreen(
                viewModel = vm,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}