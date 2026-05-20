package com.yagosouza.movies.presentation.movies_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yagosouza.movies.R
import com.yagosouza.movies.data.remote.TmdbApi
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.presentation.components.ErrorState
import com.yagosouza.movies.presentation.components.LoadingState
import com.yagosouza.movies.presentation.components.VoteAverage
import com.yagosouza.movies.presentation.theme.MoviesTheme

private const val POSTER_ASPECT_RATIO = 2f / 3f
private const val TITLE_MAX_LINES = 2

@Composable
fun MoviesListScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MoviesListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesListContent(
        uiState = uiState,
        onMovieClick = onMovieClick,
        onRetry = viewModel::loadMovies,
        onLoadMore = viewModel::loadNextPage,
    )
}

@Composable
fun MoviesListContent(
    uiState: MoviesListUiState,
    onMovieClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.movies.isEmpty() -> LoadingState()
            uiState.errorMessage != null && uiState.movies.isEmpty() -> ErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry,
            )
            else -> MoviesGrid(
                movies = uiState.movies,
                isLoadingMore = uiState.isLoadingMore,
                onMovieClick = onMovieClick,
                onLoadMore = onLoadMore,
            )
        }
    }
}

@Composable
private fun MoviesGrid(
    movies: List<Movie>,
    isLoadingMore: Boolean,
    onMovieClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
) {
    val gridState = rememberLazyGridState()
    val gridColumns = integerResource(R.integer.grid_columns)
    val prefetchThreshold = integerResource(R.integer.grid_prefetch_threshold)

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= movies.size - prefetchThreshold
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    val gridPadding = dimensionResource(R.dimen.spacing_lg)
    val gridSpacing = dimensionResource(R.dimen.spacing_md)

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        state = gridState,
        contentPadding = PaddingValues(gridPadding),
        horizontalArrangement = Arrangement.spacedBy(gridSpacing),
        verticalArrangement = Arrangement.spacedBy(gridSpacing),
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
        }

        if (isLoadingMore) {
            item(span = { GridItemSpan(gridColumns) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.spacing_lg)),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_loading)),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cornerRadius = dimensionResource(R.dimen.card_corner_radius)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation),
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column {
            AsyncImage(
                model = movie.posterPath?.let { "${TmdbApi.POSTER_W500}$it" },
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(POSTER_ASPECT_RATIO)
                    .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)),
            )

            Column(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_md))) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = TITLE_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

                VoteAverage(
                    voteAverage = movie.voteAverage,
                    iconSize = dimensionResource(R.dimen.icon_sm),
                )
            }
        }
    }
}

// region Previews

private class MoviesListUiStateProvider : PreviewParameterProvider<MoviesListUiState> {
    override val values = sequenceOf(
        MoviesListUiState(isLoading = true),
        MoviesListUiState(
            movies = List(6) { index ->
                Movie(
                    id = index,
                    title = "Filme Exemplo ${index + 1}",
                    overview = "Sinopse do filme exemplo",
                    posterPath = null,
                    backdropPath = null,
                    voteAverage = 7.5 + index * 0.3,
                    releaseDate = "2024-01-01",
                )
            },
        ),
        MoviesListUiState(errorMessage = "Sem conexao com a internet"),
    )
}

@Preview(showBackground = true)
@Composable
private fun MoviesListContentPreview(
    @PreviewParameter(MoviesListUiStateProvider::class) uiState: MoviesListUiState,
) {
    MoviesTheme(dynamicColor = false) {
        MoviesListContent(
            uiState = uiState,
            onMovieClick = {},
            onRetry = {},
            onLoadMore = {},
        )
    }
}

// endregion
