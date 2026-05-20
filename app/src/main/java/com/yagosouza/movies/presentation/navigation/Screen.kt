package com.yagosouza.movies.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object MoviesList : Screen("movies_list")
    data object Search : Screen("search")
    data object Favorites : Screen("favorites")
    data object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }
}

enum class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val labelResId: Int,
) {
    HOME(Screen.MoviesList, Icons.Filled.Home, com.yagosouza.movies.R.string.nav_home),
    SEARCH(Screen.Search, Icons.Filled.Search, com.yagosouza.movies.R.string.nav_search),
    FAVORITES(Screen.Favorites, Icons.Filled.Favorite, com.yagosouza.movies.R.string.nav_favorites),
}
