package com.yagosouza.movies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.presentation.search.SearchContent
import com.yagosouza.movies.presentation.search.SearchUiState
import com.yagosouza.movies.presentation.theme.MoviesTheme
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsSearchPrompt_whenNotSearchedYet() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                SearchContent(
                    uiState = SearchUiState(),
                    onMovieClick = {},
                    onQueryChanged = {},
                    onSearch = {},
                    onClearSearch = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Busque por filmes pelo nome").assertIsDisplayed()
    }

    @Test
    fun showsEmptyResult_whenSearchReturnsNothing() {
        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                SearchContent(
                    uiState = SearchUiState(hasSearched = true, query = "xyzabc"),
                    onMovieClick = {},
                    onQueryChanged = {},
                    onSearch = {},
                    onClearSearch = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Nenhum filme encontrado").assertIsDisplayed()
    }

    @Test
    fun showsResults_whenSearchReturnsMovies() {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Resultado da Busca",
                overview = "Sinopse",
                posterPath = null,
                backdropPath = null,
                voteAverage = 7.0,
                releaseDate = "2024-01-01",
            ),
        )

        composeTestRule.setContent {
            MoviesTheme(dynamicColor = false) {
                SearchContent(
                    uiState = SearchUiState(hasSearched = true, query = "busca", movies = movies),
                    onMovieClick = {},
                    onQueryChanged = {},
                    onSearch = {},
                    onClearSearch = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Resultado da Busca").assertIsDisplayed()
    }
}
