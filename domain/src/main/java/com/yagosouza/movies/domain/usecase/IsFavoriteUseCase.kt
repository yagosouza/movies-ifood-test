package com.yagosouza.movies.domain.usecase

import com.yagosouza.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class IsFavoriteUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(movieId: Int): Flow<Boolean> {
        return repository.isFavorite(movieId)
    }
}
