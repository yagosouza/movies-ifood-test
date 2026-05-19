package com.yagosouza.movies.presentation.movie_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<Int>("movieId")?.let { loadMovieDetails(it) }
    }

    fun loadMovieDetails(movieId: Int) {
        getMovieDetailsUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(isLoading = true, errorMessage = null)
                }
                is Resource.Success -> _uiState.update {
                    it.copy(isLoading = false, movie = result.data)
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }
}
