package com.yagosouza.movies.domain.repository

import com.yagosouza.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): List<Movie>
    suspend fun getMovieDetails(movieId: Int): Movie
    suspend fun searchMovies(query: String, page: Int): List<Movie>
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun toggleFavorite(movie: Movie)
    fun isFavorite(movieId: Int): Flow<Boolean>
}
