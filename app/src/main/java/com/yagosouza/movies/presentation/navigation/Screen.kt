package com.yagosouza.movies.presentation.navigation

sealed class Screen(val route: String) {
    data object MoviesList : Screen("movies_list")
    data object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }
}
