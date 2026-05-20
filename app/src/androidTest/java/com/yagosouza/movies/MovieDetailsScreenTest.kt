package com.yagosouza.movies

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
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
    fun showsBackButton_andMovieTitle_whenMovieLoaded() {
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
                    onToggleFavorite = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Voltar").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Filme Detalhes Teste").assertCountEquals(2)
    }

    @Test
    fun showsFavoriteButton_whenMovieLoaded() {
        val movie = Movie(
            id = 1,
            title = "Filme Teste",
            overview = "Sinopse",
            posterPath = null,
            backdropPath = null,
            voteAverage = 7.0,
            releaseDate = "2024-01-01",
        )

        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MovieDetailsContent(
                    uiState = MovieDetailsUiState(movie = movie, isFavorite = false),
                    onBackClick = {},
                    onRetry = {},
                    onToggleFavorite = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Adicionar aos favoritos").assertIsDisplayed()
    }

    @Test
    fun showsFilledHeart_whenIsFavorite() {
        val movie = Movie(
            id = 1,
            title = "Filme Teste",
            overview = "Sinopse",
            posterPath = null,
            backdropPath = null,
            voteAverage = 7.0,
            releaseDate = "2024-01-01",
        )

        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MovieDetailsContent(
                    uiState = MovieDetailsUiState(movie = movie, isFavorite = true),
                    onBackClick = {},
                    onRetry = {},
                    onToggleFavorite = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Remover dos favoritos").assertIsDisplayed()
    }

    @Test
    fun showsErrorMessage_whenErrorState() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MovieDetailsContent(
                    uiState = MovieDetailsUiState(errorMessage = "Erro ao carregar detalhes"),
                    onBackClick = {},
                    onRetry = {},
                    onToggleFavorite = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Erro ao carregar detalhes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tentar Novamente").assertIsDisplayed()
    }
}
