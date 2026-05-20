package com.yagosouza.movies.presentation.search

import app.cash.turbine.test
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.usecase.SearchMoviesUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchMoviesUseCase = mockk()
        viewModel = SearchViewModel(searchMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearch updates state with search results`() = runTest {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Found Movie",
                overview = "Overview",
                posterPath = null,
                backdropPath = null,
                voteAverage = 7.0,
                releaseDate = "2024-01-01",
            ),
        )
        every { searchMoviesUseCase("test", page = 1) } returns flowOf(
            Resource.Loading,
            Resource.Success(movies),
        )

        viewModel.onQueryChanged("test")
        viewModel.onSearch()

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertFalse(state.isLoading)
            assertTrue(state.hasSearched)
            assertEquals(1, state.movies.size)
            assertEquals("Found Movie", state.movies[0].title)
        }
    }

    @Test
    fun `onSearch with blank query does nothing`() = runTest {
        viewModel.onSearch()

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertFalse(state.hasSearched)
            assertTrue(state.movies.isEmpty())
        }
    }

    @Test
    fun `onClearSearch resets state`() = runTest {
        viewModel.onQueryChanged("test")
        viewModel.onClearSearch()

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals("", state.query)
            assertFalse(state.hasSearched)
            assertTrue(state.movies.isEmpty())
        }
    }
}
