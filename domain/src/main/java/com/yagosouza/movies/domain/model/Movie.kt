package com.yagosouza.movies.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String,
    val genres: List<Genre> = emptyList(),
    val runtime: Int? = null,
    val tagline: String? = null,
)

data class Genre(
    val id: Int,
    val name: String,
)
