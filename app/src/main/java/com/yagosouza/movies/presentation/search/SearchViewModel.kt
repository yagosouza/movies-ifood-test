package com.yagosouza.movies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yagosouza.movies.domain.Resource
import com.yagosouza.movies.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onSearch() {
        val query = _uiState.value.query
        if (query.isBlank()) return

        searchMoviesUseCase(query, page = 1).onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(isLoading = true, errorMessage = null, hasSearched = true)
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

    fun onClearSearch() {
        _uiState.update { SearchUiState() }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingMore || state.hasReachedEnd || state.query.isBlank()) return

        val nextPage = state.currentPage + 1
        searchMoviesUseCase(state.query, page = nextPage).onEach { result ->
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
