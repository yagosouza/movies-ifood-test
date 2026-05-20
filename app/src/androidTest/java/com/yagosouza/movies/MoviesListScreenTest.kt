package com.yagosouza.movies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.presentation.movies_list.MoviesListContent
import com.yagosouza.movies.presentation.movies_list.MoviesListUiState
import com.yagosouza.movies.presentation.theme.MoviesTheme
import org.junit.Rule
import org.junit.Test

class MoviesListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsMovieTitle_whenMoviesLoaded() {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Filme Teste",
                overview = "Sinopse do filme",
                posterPath = null,
                backdropPath = null,
                voteAverage = 7.5,
                releaseDate = "2024-01-01",
            ),
        )

        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MoviesListContent(
                    uiState = MoviesListUiState(movies = movies),
                    onMovieClick = {},
                    onRetry = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Filme Teste").assertIsDisplayed()
    }

    @Test
    fun showsErrorMessage_whenErrorState() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MoviesListContent(
                    uiState = MoviesListUiState(errorMessage = "Erro de conexao"),
                    onMovieClick = {},
                    onRetry = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Erro de conexao").assertIsDisplayed()
    }

    @Test
    fun showsCategoryChips() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MoviesListContent(
                    uiState = MoviesListUiState(),
                    onMovieClick = {},
                    onRetry = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Populares").assertIsDisplayed()
        composeTestRule.onNodeWithText("Em Cartaz").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mais Votados").assertIsDisplayed()
        composeTestRule.onNodeWithText("Em Breve").assertIsDisplayed()
    }

    @Test
    fun showsPopularChipSelected_byDefault() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                MoviesListContent(
                    uiState = MoviesListUiState(),
                    onMovieClick = {},
                    onRetry = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Populares").assertIsSelected()
    }
}
