package com.yagosouza.movies.domain.usecase

import app.cash.turbine.test
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.model.Genre
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieDetailsUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke emits Loading then Success with movie details`() = runTest {
        val movie = Movie(
            id = 42,
            title = "Test Movie",
            overview = "A test movie overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 7.8,
            releaseDate = "2024-06-15",
            genres = listOf(Genre(id = 28, name = "Ação")),
            runtime = 120,
            tagline = "A tagline",
        )
        coEvery { repository.getMovieDetails(42) } returns movie

        useCase(movieId = 42).test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(movie, (success as Resource.Success).data)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error on failure`() = runTest {
        coEvery { repository.getMovieDetails(42) } throws RuntimeException("Not found")

        useCase(movieId = 42).test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals("Not found", (error as Resource.Error).message)

            awaitComplete()
        }
    }
}
