package com.yagosouza.movies.presentation.movie_details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.model.Genre
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.usecase.GetMovieDetailsUseCase
import com.yagosouza.movies.domain.usecase.IsFavoriteUseCase
import com.yagosouza.movies.domain.usecase.ToggleFavoriteUseCase
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var isFavoriteUseCase: IsFavoriteUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getMovieDetailsUseCase = mockk()
        toggleFavoriteUseCase = mockk()
        isFavoriteUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads movie details from savedStateHandle movieId`() = runTest {
        val movie = Movie(
            id = 42,
            title = "Test Movie",
            overview = "Test overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.5,
            releaseDate = "2024-06-15",
            genres = listOf(Genre(id = 28, name = "Acao")),
            runtime = 120,
        )
        every { getMovieDetailsUseCase(movieId = 42) } returns flowOf(
            Resource.Loading,
            Resource.Success(movie),
        )
        every { isFavoriteUseCase(movieId = 42) } returns flowOf(false)

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 42))
        val viewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase,
            toggleFavoriteUseCase,
            isFavoriteUseCase,
            savedStateHandle,
        )

        viewModel.uiState.test {
            awaitItem()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertFalse(state.isLoading)
            assertNotNull(state.movie)
            assertEquals("Test Movie", state.movie!!.title)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `shows error state when loading fails`() = runTest {
        every { getMovieDetailsUseCase(movieId = 99) } returns flowOf(
            Resource.Loading,
            Resource.Error("Not found"),
        )
        every { isFavoriteUseCase(movieId = 99) } returns flowOf(false)

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 99))
        val viewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase,
            toggleFavoriteUseCase,
            isFavoriteUseCase,
            savedStateHandle,
        )

        viewModel.uiState.test {
            awaitItem()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = expectMostRecentItem()
            assertFalse(state.isLoading)
            assertEquals("Not found", state.errorMessage)
            assertNull(state.movie)
        }
    }
}
