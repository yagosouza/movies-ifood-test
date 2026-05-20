package com.yagosouza.movies.presentation.movies_list

import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.domain.model.MovieCategory

data class MoviesListUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val isLoadingMore: Boolean = false,
    val hasReachedEnd: Boolean = false,
    val selectedCategory: MovieCategory = MovieCategory.POPULAR,
)
