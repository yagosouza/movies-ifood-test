package com.yagosouza.movies.domain.repository

import com.yagosouza.movies.domain.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): List<Movie>
    suspend fun getMovieDetails(movieId: Int): Movie
}
