package com.yagosouza.movies.domain.usecase

import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.exception.ErrorMapper
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieDetailsUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(movieId: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading)
        try {
            val movie = repository.getMovieDetails(movieId)
            emit(Resource.Success(movie))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorMapper.map(e), e))
        }
    }
}
