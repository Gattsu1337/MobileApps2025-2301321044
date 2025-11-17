package com.example.mangaverse

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mangaverse.logic.MangaRepository
import com.example.mangaverse.logic.MangaViewModel
import com.example.mangaverse.logic.MangaViewModelFactory
import com.example.mangaverse.ui.screens.MangaDetailScreen
import com.example.mangaverse.ui.screens.MangaListScreen

sealed class Screen(val route: String) {
    object MangaList : Screen("manga_list")

    object MangaDetail : Screen("manga_detail/{mangaId}") {
        fun createRoute(mangaId: Int) = "manga_detail/$mangaId"
    }
}

@Composable
fun MangaVerseNavHost(app: MangaVerseApp) {
    val navController = rememberNavController()

    val repository = MangaRepository(app.database.mangaDao())
    val viewModel: MangaViewModel = viewModel(
        factory = MangaViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.MangaList.route
    ) {
        composable(route = Screen.MangaList.route) {
            MangaListScreen(
                viewModel = viewModel,
                onMangaClick = { mangaId ->
                    navController.navigate(Screen.MangaDetail.createRoute(mangaId))
                },
                onAddMangaClick = {
                    navController.navigate(Screen.MangaDetail.createRoute(0))
                }
            )
        }

        composable(
            route = Screen.MangaDetail.route,
            arguments =listOf(navArgumenet("mangaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") ?: 0

            MangaDetailScreen(
                mangaId = mangaId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}