package com.yagosouza.movies.di

import com.yagosouza.movies.domain.repository.MovieRepository
import com.yagosouza.movies.domain.usecase.GetMovieDetailsUseCase
import com.yagosouza.movies.domain.usecase.GetPopularMoviesUseCase
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
}
