package com.yagosouza.movies.domain.usecase

import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.repository.MovieRepository

class ToggleFavoriteUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(movie: Movie) {
        repository.toggleFavorite(movie)
    }
}
