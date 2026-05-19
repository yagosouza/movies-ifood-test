package com.yagosouza.movies.presentation.movie_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yagosouza.movies.R
import com.yagosouza.movies.data.remote.TmdbApi
import com.yagosouza.movies.domain.model.Movie
import com.yagosouza.movies.presentation.components.ErrorState
import com.yagosouza.movies.presentation.components.IconInfoRow
import com.yagosouza.movies.presentation.components.LoadingState
import com.yagosouza.movies.presentation.components.VoteAverage

private const val BACKDROP_ASPECT_RATIO = 16f / 9f
private const val POSTER_ASPECT_RATIO = 2f / 3f
private const val GRADIENT_START_Y = 200f
private const val GRADIENT_ALPHA = 0.7f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.movie?.title.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.errorMessage != null -> {
                    ErrorState(
                        message = uiState.errorMessage.orEmpty(),
                        onRetry = {
                            uiState.movie?.id?.let { viewModel.loadMovieDetails(it) }
                        },
                    )
                }
                uiState.movie != null -> {
                    val movie = uiState.movie ?: return@Scaffold
                    MovieDetailsContent(movie = movie)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MovieDetailsContent(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box {
            AsyncImage(
                model = movie.backdropPath?.let { "${TmdbApi.BACKDROP_W780}$it" },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(BACKDROP_ASPECT_RATIO),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(BACKDROP_ASPECT_RATIO)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = GRADIENT_ALPHA)),
                            startY = GRADIENT_START_Y,
                        )
                    ),
            )

            AsyncImage(
                model = movie.posterPath?.let { "${TmdbApi.POSTER_W500}$it" },
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.poster_overlay_width))
                    .aspectRatio(POSTER_ASPECT_RATIO)
                    .align(Alignment.BottomStart)
                    .offset(
                        x = dimensionResource(R.dimen.spacing_lg),
                        y = dimensionResource(R.dimen.poster_overlay_offset_y),
                    )
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.poster_corner_radius))),
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xxl)))

        Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_lg))) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            val tagline = movie.tagline
            if (!tagline.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = tagline,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VoteAverage(
                    voteAverage = movie.voteAverage,
                    iconSize = dimensionResource(R.dimen.icon_lg),
                    textStyle = MaterialTheme.typography.titleMedium,
                )

                val runtime = movie.runtime
                if (runtime != null) {
                    IconInfoRow(
                        icon = Icons.Filled.AccessTime,
                        text = stringResource(R.string.runtime_format, runtime),
                    )
                }

                if (movie.releaseDate.isNotBlank()) {
                    IconInfoRow(
                        icon = Icons.Filled.CalendarToday,
                        text = movie.releaseDate,
                    )
                }
            }

            if (movie.genres.isNotEmpty()) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs)),
                ) {
                    movie.genres.forEach { genre ->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = genre.name,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_lg)))

            Text(
                text = stringResource(R.string.synopsis_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))

            Text(
                text = movie.overview.ifBlank { stringResource(R.string.synopsis_unavailable) },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
        }
    }
}

