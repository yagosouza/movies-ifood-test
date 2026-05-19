package com.yagosouza.movies.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Red500,
    onPrimary = Gray50,
    primaryContainer = Red700,
    secondary = Yellow500,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onBackground = Gray50,
    onSurface = Gray50,
    onSurfaceVariant = Gray400,
    error = Red200,
)

private val LightColorScheme = lightColorScheme(
    primary = Red500,
    onPrimary = Gray50,
    primaryContainer = Red200,
    secondary = Yellow500,
    background = Gray50,
    surface = Gray100,
    surfaceVariant = Gray200,
    onBackground = Gray900,
    onSurface = Gray900,
    onSurfaceVariant = Gray600,
    error = Red700,
)

@Composable
fun MoviesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
