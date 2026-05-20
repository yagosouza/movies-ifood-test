package com.yagosouza.movies.di

import com.yagosouza.movies.domain.repository.MovieRepository
import com.yagosouza.movies.domain.usecase.GetFavoriteMoviesUseCase
import com.yagosouza.movies.domain.usecase.GetMovieDetailsUseCase
import com.yagosouza.movies.domain.usecase.GetPopularMoviesUseCase
import com.yagosouza.movies.domain.usecase.IsFavoriteUseCase
import com.yagosouza.movies.domain.usecase.SearchMoviesUseCase
import com.yagosouza.movies.domain.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetPopularMoviesUseCase(repository: MovieRepository): GetPopularMoviesUseCase =
        GetPopularMoviesUseCase(repository)

    @Provides
    fun provideGetMovieDetailsUseCase(repository: MovieRepository): GetMovieDetailsUseCase =
        GetMovieDetailsUseCase(repository)

    @Provides
    fun provideSearchMoviesUseCase(repository: MovieRepository): SearchMoviesUseCase =
        SearchMoviesUseCase(repository)

    @Provides
    fun provideToggleFavoriteUseCase(repository: MovieRepository): ToggleFavoriteUseCase =
        ToggleFavoriteUseCase(repository)

    @Provides
    fun provideGetFavoriteMoviesUseCase(repository: MovieRepository): GetFavoriteMoviesUseCase =
        GetFavoriteMoviesUseCase(repository)

    @Provides
    fun provideIsFavoriteUseCase(repository: MovieRepository): IsFavoriteUseCase =
        IsFavoriteUseCase(repository)
}
