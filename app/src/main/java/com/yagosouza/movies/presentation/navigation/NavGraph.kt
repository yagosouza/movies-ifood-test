package com.yagosouza.movies.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yagosouza.movies.R
import com.yagosouza.movies.presentation.favorites.FavoritesScreen
import com.yagosouza.movies.presentation.movie_details.MovieDetailsScreen
import com.yagosouza.movies.presentation.movies_list.MoviesListScreen
import com.yagosouza.movies.presentation.search.SearchScreen

private val bottomNavItems = listOf(
    BottomNavItem.HOME,
    BottomNavItem.SEARCH,
    BottomNavItem.FAVORITES,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.any { it.screen.route == currentRoute }

    val topBarTitle = when (currentRoute) {
        Screen.MoviesList.route -> stringResource(R.string.movies_list_title)
        Screen.Search.route -> stringResource(R.string.nav_search)
        Screen.Favorites.route -> stringResource(R.string.nav_favorites)
        else -> null
    }

    Scaffold(
        topBar = {
            if (topBarTitle != null) {
                TopAppBar(
                    title = { Text(text = topBarTitle) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = navBackStackEntry?.destination?.hierarchy?.any {
                            it.route == item.screen.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(Screen.MoviesList.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(item.labelResId),
                                )
                            },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.MoviesList.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(route = Screen.MoviesList.route) {
                MoviesListScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.MovieDetails.createRoute(movieId))
                    },
                )
            }

            composable(route = Screen.Search.route) {
                SearchScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.MovieDetails.createRoute(movieId))
                    },
                )
            }

            composable(route = Screen.Favorites.route) {
                FavoritesScreen(
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
}
