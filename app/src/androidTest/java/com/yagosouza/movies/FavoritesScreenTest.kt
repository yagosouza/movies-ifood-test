package com.yagosouza.movies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.presentation.favorites.FavoritesContent
import com.yagosouza.movies.presentation.favorites.FavoritesUiState
import com.yagosouza.movies.presentation.theme.MoviesTheme
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsEmptyState_whenNoFavorites() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                FavoritesContent(
                    uiState = FavoritesUiState(),
                    onMovieClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Nenhum favorito ainda").assertIsDisplayed()
    }

    @Test
    fun showsFavoriteMovies_whenHasFavorites() {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Filme Favorito",
                overview = "Sinopse",
                posterPath = null,
                backdropPath = null,
                voteAverage = 9.0,
                releaseDate = "2024-01-01",
            ),
        )

        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                FavoritesContent(
                    uiState = FavoritesUiState(movies = movies),
                    onMovieClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Filme Favorito").assertIsDisplayed()
    }
}
