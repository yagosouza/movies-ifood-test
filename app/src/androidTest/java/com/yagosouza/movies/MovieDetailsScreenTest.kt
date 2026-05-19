package com.yagosouza.movies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.yagosouza.movies.domain.model.Genre
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.presentation.movie_details.MovieDetailsContent
import com.yagosouza.movies.presentation.movie_details.MovieDetailsUiState
import com.yagosouza.movies.presentation.theme.MoviesTheme
import org.junit.Rule
import org.junit.Test

class MovieDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsMovieDetails_whenMovieLoaded() {
        val movie = Movie(
            id = 1,
            title = "Filme Detalhes Teste",
            overview = "Uma sinopse completa do filme para teste.",
            posterPath = null,
            backdropPath = null,
            voteAverage = 8.5,
            releaseDate = "2024-06-15",
            genres = listOf(
                Genre(id = 1, name = "Acao"),
                Genre(id = 2, name = "Aventura"),
            ),
            runtime = 120,
            tagline = "Uma tagline de teste",
        )

        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MovieDetailsContent(
                    uiState = MovieDetailsUiState(movie = movie),
                    onBackClick = {},
                    onRetry = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Filme Detalhes Teste").assertExists()
        composeTestRule.onNodeWithText("Sinopse").assertIsDisplayed()
        composeTestRule.onNodeWithText("Acao").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aventura").assertIsDisplayed()
        composeTestRule.onNodeWithText("120 min").assertIsDisplayed()
    }

    @Test
    fun showsErrorMessage_whenErrorState() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MovieDetailsContent(
                    uiState = MovieDetailsUiState(errorMessage = "Erro ao carregar detalhes"),
                    onBackClick = {},
                    onRetry = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Erro ao carregar detalhes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tentar Novamente").assertIsDisplayed()
    }
}
