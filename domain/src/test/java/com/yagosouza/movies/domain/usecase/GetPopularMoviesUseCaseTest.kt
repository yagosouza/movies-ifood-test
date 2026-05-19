package com.yagosouza.movies.domain.usecase

import app.cash.turbine.test
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPopularMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetPopularMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke emits Loading then Success when repository returns data`() = runTest {
        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                overview = "Overview 1",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                voteAverage = 8.5,
                releaseDate = "2024-01-01",
            ),
        )
        coEvery { repository.getPopularMovies(1) } returns movies

        useCase(page = 1).test {
            val loading = awaitItem()
            assertTrue(loading is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(movies, (success as Resource.Success).data)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error when repository throws exception`() = runTest {
        coEvery { repository.getPopularMovies(1) } throws RuntimeException("Network error")

        useCase(page = 1).test {
            val loading = awaitItem()
            assertTrue(loading is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals("Network error", (error as Resource.Error).message)

            awaitComplete()
        }
    }
}
