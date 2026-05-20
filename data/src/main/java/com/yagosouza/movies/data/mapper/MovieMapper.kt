package com.yagosouza.movies.data.mapper

import com.yagosouza.movies.data.local.entity.FavoriteMovieEntity
import com.yagosouza.movies.data.remote.dto.GenreDto
import com.yagosouza.movies.data.remote.dto.MovieDetailDto
import com.yagosouza.movies.data.remote.dto.MovieDto
import com.yagosouza.movies.domain.model.Genre
import com.yagosouza.movies.domain.model.Movie

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
)

fun MovieDetailDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    genres = genres.map { it.toDomain() },
    runtime = runtime,
    tagline = tagline,
)

fun GenreDto.toDomain(): Genre = Genre(
    id = id,
    name = name,
)

fun FavoriteMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
)

fun Movie.toEntity(): FavoriteMovieEntity = FavoriteMovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
)
