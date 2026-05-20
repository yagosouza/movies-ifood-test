package com.yagosouza.movies.presentation.search

import com.yagosouza.movies.domain.model.Movie

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val hasSearched: Boolean = false,
    val currentPage: Int = 1,
    val isLoadingMore: Boolean = false,
    val hasReachedEnd: Boolean = false,
)
