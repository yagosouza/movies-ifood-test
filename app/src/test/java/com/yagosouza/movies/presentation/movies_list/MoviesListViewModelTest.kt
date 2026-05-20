package com.yagosouza.movies.presentation.movies_list

import app.cash.turbine.test
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.model.MovieCategory
import com.yagosouza.movies.domain.usecase.GetPopularMoviesUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPopularMoviesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load updates state to Success with movies`() = runTest {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                overview = "Overview",
                posterPath = null,
                backdropPath = null,
                voteAverage = 7.0,
                releaseDate = "2024-01-01",
            ),
        )
        every {
            getPopularMoviesUseCase(category = MovieCategory.POPULAR, page = 1)
        } returns flowOf(
            Resource.Loading,
            Resource.Success(movies),
        )

        val viewModel = MoviesListViewModel(getPopularMoviesUseCase)

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial.movies.isEmpty())

            testDispatcher.scheduler.advanceUntilIdle()

            val loaded = expectMostRecentItem()
            assertFalse(loaded.isLoading)
            assertEquals(1, loaded.movies.size)
            assertEquals("Movie 1", loaded.movies[0].title)
            assertNull(loaded.errorMessage)
        }
    }

    @Test
    fun `initial load updates state to Error on failure`() = runTest {
        every {
            getPopularMoviesUseCase(category = MovieCategory.POPULAR, page = 1)
        } returns flowOf(
            Resource.Loading,
            Resource.Error("Network error"),
        )

        val viewModel = MoviesListViewModel(getPopularMoviesUseCase)

        viewModel.uiState.test {
            awaitItem()
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = expectMostRecentItem()
            assertFalse(errorState.isLoading)
            assertEquals("Network error", errorState.errorMessage)
            assertTrue(errorState.movies.isEmpty())
        }
    }

    @Test
    fun `onCategorySelected changes category and reloads`() = runTest {
        every {
            getPopularMoviesUseCase(category = MovieCategory.POPULAR, page = 1)
        } returns flowOf(Resource.Loading, Resource.Success(emptyList()))

        val topRatedMovies = listOf(
            Movie(
                id = 2,
                title = "Top Rated Movie",
                overview = "Overview",
                posterPath = null,
                backdropPath = null,
                voteAverage = 9.0,
                releaseDate = "2024-01-01",
            ),
        )
        every {
            getPopularMoviesUseCase(category = MovieCategory.TOP_RATED, page = 1)
        } returns flowOf(
            Resource.Loading,
            Resource.Success(topRatedMovies),
        )

        val viewModel = MoviesListViewModel(getPopularMoviesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onCategorySelected(MovieCategory.TOP_RATED)

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(MovieCategory.TOP_RATED, state.selectedCategory)
            assertEquals(1, state.movies.size)
            assertEquals("Top Rated Movie", state.movies[0].title)
        }
    }
}
