package com.yagosouza.movies.data.repository

import com.yagosouza.movies.data.local.dao.FavoriteMovieDao
import com.yagosouza.movies.data.mapper.toDomain
import com.yagosouza.movies.data.mapper.toEntity
import com.yagosouza.movies.data.remote.TmdbApi
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.model.MovieCategory
import com.yagosouza.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val favoriteMovieDao: FavoriteMovieDao,
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): List<Movie> {
        return api.getPopularMovies(page).results.map { it.toDomain() }
    }

    override suspend fun getMoviesByCategory(category: MovieCategory, page: Int): List<Movie> {
        val response = when (category) {
            MovieCategory.POPULAR -> api.getPopularMovies(page)
            MovieCategory.NOW_PLAYING -> api.getNowPlayingMovies(page)
            MovieCategory.TOP_RATED -> api.getTopRatedMovies(page)
            MovieCategory.UPCOMING -> api.getUpcomingMovies(page)
        }
        return response.results.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(movieId: Int): Movie {
        return api.getMovieDetails(movieId).toDomain()
    }

    override suspend fun searchMovies(query: String, page: Int): List<Movie> {
        return api.searchMovies(query, page).results.map { it.toDomain() }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return favoriteMovieDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun toggleFavorite(movie: Movie) {
        val existing = favoriteMovieDao.getById(movie.id)
        if (existing != null) {
            favoriteMovieDao.delete(existing)
        } else {
            favoriteMovieDao.insert(movie.toEntity())
        }
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return favoriteMovieDao.isFavorite(movieId)
    }
}
