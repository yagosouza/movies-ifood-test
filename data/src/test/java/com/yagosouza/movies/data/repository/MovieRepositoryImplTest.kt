package com.yagosouza.movies.data.repository

import com.yagosouza.movies.data.local.dao.FavoriteMovieDao
import com.yagosouza.movies.data.remote.TmdbApi
import com.yagosouza.movies.data.remote.dto.GenreDto
import com.yagosouza.movies.data.remote.dto.MovieDetailDto
import com.yagosouza.movies.data.remote.dto.MovieDto
import com.yagosouza.movies.data.remote.dto.MovieListResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {

    private lateinit var api: TmdbApi
    private lateinit var favoriteMovieDao: FavoriteMovieDao
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        favoriteMovieDao = mockk()
        repository = MovieRepositoryImpl(api, favoriteMovieDao)
    }

    @Test
    fun `getPopularMovies maps DTOs to domain models`() = runTest {
        val response = MovieListResponse(
            page = 1,
            results = listOf(
                MovieDto(
                    id = 1,
                    title = "Film A",
                    overview = "Overview A",
                    posterPath = "/a.jpg",
                    backdropPath = "/ba.jpg",
                    voteAverage = 8.0,
                    releaseDate = "2024-01-01",
                ),
            ),
            totalPages = 10,
            totalResults = 200,
        )
        coEvery { api.getPopularMovies(1) } returns response

        val result = repository.getPopularMovies(1)

        assertEquals(1, result.size)
        assertEquals("Film A", result[0].title)
        assertEquals(1, result[0].id)
    }

    @Test
    fun `getMovieDetails maps detail DTO to domain model with genres`() = runTest {
        val detailDto = MovieDetailDto(
            id = 42,
            title = "Detail Film",
            overview = "Detail overview",
            posterPath = "/detail.jpg",
            backdropPath = "/detail_back.jpg",
            voteAverage = 9.0,
            releaseDate = "2024-05-20",
            genres = listOf(GenreDto(id = 28, name = "Action")),
            runtime = 150,
            tagline = "A tagline",
        )
        coEvery { api.getMovieDetails(42) } returns detailDto

        val result = repository.getMovieDetails(42)

        assertEquals(42, result.id)
        assertEquals("Detail Film", result.title)
        assertEquals(1, result.genres.size)
        assertEquals("Action", result.genres[0].name)
        assertEquals(150, result.runtime)
    }

    @Test
    fun `searchMovies maps DTOs to domain models`() = runTest {
        val response = MovieListResponse(
            page = 1,
            results = listOf(
                MovieDto(
                    id = 5,
                    title = "Search Result",
                    overview = "Found movie",
                    posterPath = "/search.jpg",
                    backdropPath = null,
                    voteAverage = 6.5,
                    releaseDate = "2024-03-10",
                ),
            ),
            totalPages = 1,
            totalResults = 1,
        )
        coEvery { api.searchMovies("search", 1) } returns response

        val result = repository.searchMovies("search", 1)

        assertEquals(1, result.size)
        assertEquals("Search Result", result[0].title)
    }
}
