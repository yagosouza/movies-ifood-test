package com.yagosouza.movies.data.remote

import com.yagosouza.movies.data.remote.dto.MovieDetailDto
import com.yagosouza.movies.data.remote.dto.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "pt-BR",
    ): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "pt-BR",
    ): MovieDetailDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        const val POSTER_W500 = "${IMAGE_BASE_URL}w500"
        const val BACKDROP_W780 = "${IMAGE_BASE_URL}w780"
    }
}
