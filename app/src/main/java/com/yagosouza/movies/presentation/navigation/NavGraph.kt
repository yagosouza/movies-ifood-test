package com.yagosouza.movies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yagosouza.movies.presentation.movie_details.MovieDetailsScreen
import com.yagosouza.movies.presentation.movies_list.MoviesListScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MoviesList.route,
    ) {
        composable(route = Screen.MoviesList.route) {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                },
            )
        }

        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            ),
        ) {
            MovieDetailsScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
