package com.yagosouza.movies.domain.usecase

import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularMoviesUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(page: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val movies = repository.getPopularMovies(page)
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Erro ao carregar filmes", e))
        }
    }
}
