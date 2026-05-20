package com.yagosouza.movies.domain.usecase

import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.exception.ErrorMapper
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.model.MovieCategory
import com.yagosouza.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularMoviesUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(
        category: MovieCategory = MovieCategory.POPULAR,
        page: Int,
    ): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val movies = repository.getMoviesByCategory(category, page)
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorMapper.map(e), e))
        }
    }
}
