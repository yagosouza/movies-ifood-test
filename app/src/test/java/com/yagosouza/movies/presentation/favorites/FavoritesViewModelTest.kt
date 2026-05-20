package com.yagosouza.movies.presentation.favorites

import app.cash.turbine.test
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.usecase.GetFavoriteMoviesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getFavoriteMoviesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits favorite movies from use case`() = runTest {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Favorite Movie",
                overview = "Overview",
                posterPath = null,
                backdropPath = null,
                voteAverage = 8.0,
                releaseDate = "2024-01-01",
            ),
        )
        every { getFavoriteMoviesUseCase() } returns flowOf(movies)

        val viewModel = FavoritesViewModel(getFavoriteMoviesUseCase)

        viewModel.uiState.test {
            awaitItem() // initial empty
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(1, state.movies.size)
            assertEquals("Favorite Movie", state.movies[0].title)
        }
    }

    @Test
    fun `emits empty list when no favorites`() = runTest {
        every { getFavoriteMoviesUseCase() } returns flowOf(emptyList())

        val viewModel = FavoritesViewModel(getFavoriteMoviesUseCase)

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(0, state.movies.size)
        }
    }
}
