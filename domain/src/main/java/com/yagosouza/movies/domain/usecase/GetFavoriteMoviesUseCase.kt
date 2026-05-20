package com.yagosouza.movies.domain.usecase

import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMoviesUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getFavoriteMovies()
    }
}
