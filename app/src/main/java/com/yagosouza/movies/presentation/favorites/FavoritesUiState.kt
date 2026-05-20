package com.yagosouza.movies.presentation.favorites

import com.yagosouza.movies.domain.model.Movie

data class FavoritesUiState(
    val movies: List<Movie> = emptyList(),
)
