package com.yagosouza.movies.data.repository

import com.yagosouza.movies.data.mapper.toDomain
import com.yagosouza.movies.data.remote.TmdbApi
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): List<Movie> {
        return api.getPopularMovies(page).results.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(movieId: Int): Movie {
        return api.getMovieDetails(movieId).toDomain()
    }
}
