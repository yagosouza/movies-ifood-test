package com.yagosouza.movies.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.yagosouza.movies.R
import com.yagosouza.movies.presentation.theme.Yellow500

@Composable
fun VoteAverage(
    voteAverage: Double,
    iconSize: Dp,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Yellow500,
            modifier = Modifier.size(iconSize),
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_xs)))
        Text(
            text = stringResource(R.string.vote_average_format, voteAverage),
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
