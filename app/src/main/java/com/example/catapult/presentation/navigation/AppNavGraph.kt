package com.example.catapult.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.catapult.presentation.screens.startup.StartupScreen
import com.example.catapult.presentation.screens.login.LoginScreen
import com.example.catapult.presentation.screens.breeddetails.BreedDetailsScreen
import com.example.catapult.presentation.screens.breeddetails.BreedDetailsViewModel
import com.example.catapult.presentation.screens.breedgallery.BreedGalleryScreen
import com.example.catapult.presentation.screens.breedgallery.BreedGalleryViewModel
import com.example.catapult.presentation.screens.photoviewer.PhotoViewerScreen
import com.example.catapult.presentation.screens.main.MainScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "startup") {

        composable("startup") {
            StartupScreen(
                onUserExists = {
                    navController.navigate("main") { popUpTo(0) { inclusive = true } }
                },
                onUserNotFound = {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable("main") {
            MainScreen(navController = navController)
        }

        composable(
            route = "breedDetails/{breedId}",
            arguments = listOf(navArgument("breedId") { type = NavType.StringType })
        ) { backStackEntry ->
            val breedId = backStackEntry.arguments?.getString("breedId") ?: ""
            val viewModel: BreedDetailsViewModel = hiltViewModel()

            BreedDetailsScreen(
                viewModel = viewModel,
                onNavigateToGallery = { navController.navigate("breedGallery/$breedId") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "breedGallery/{breedId}",
            arguments = listOf(navArgument("breedId") { type = NavType.StringType })
        ) { backStackEntry ->
            val breedId = backStackEntry.arguments?.getString("breedId") ?: ""
            val viewModel: BreedGalleryViewModel = hiltViewModel()

            BreedGalleryScreen(
                viewModel = viewModel,
                breedId = breedId,
                onNavigateToPhotoViewer = { startIndex ->
                    navController.navigate("photoViewer/$breedId/$startIndex")
                }
            )
        }

        composable(
            route = "photoViewer/{breedId}/{startIndex}",
            arguments = listOf(
                navArgument("breedId") { type = NavType.StringType },
                navArgument("startIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val breedId = backStackEntry.arguments?.getString("breedId") ?: ""
            val startIndex = backStackEntry.arguments?.getInt("startIndex") ?: 0

            PhotoViewerScreen(
                breedId = breedId,
                startIndex = startIndex,
                onClose = { navController.popBackStack() }
            )
        }
    }
}
