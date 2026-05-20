package com.yagosouza.movies.presentation.movie_details

import com.yagosouza.movies.domain.model.Movie

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
)
