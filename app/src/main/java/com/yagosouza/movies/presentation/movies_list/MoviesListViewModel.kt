package com.yagosouza.movies.presentation.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.usecase.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesListUiState())
    val uiState: StateFlow<MoviesListUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        getPopularMoviesUseCase(page = 1).onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(isLoading = true, errorMessage = null)
                }
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        movies = result.data,
                        currentPage = 1,
                        hasReachedEnd = result.data.isEmpty(),
                    )
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingMore || state.hasReachedEnd) return

        val nextPage = state.currentPage + 1
        getPopularMoviesUseCase(page = nextPage).onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(isLoadingMore = true)
                }
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        movies = it.movies + result.data,
                        currentPage = nextPage,
                        hasReachedEnd = result.data.isEmpty(),
                    )
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoadingMore = false, errorMessage = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }
}
