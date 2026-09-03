package com.himal.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(

    primary = HimalForest,
    onPrimary = Color.White,

    primaryContainer = HimalForestLight,
    onPrimaryContainer = HimalForestDark,

    secondary = HimalEarth,
    onSecondary = Color.White,

    secondaryContainer = HimalEarthLight,
    onSecondaryContainer = HimalEarthDark,

    tertiary = HimalSuccess,
    onTertiary = Color.White,

    background = HimalBackground,
    onBackground = HimalTextPrimary,

    surface = HimalSurface,
    onSurface = HimalTextPrimary,

    surfaceVariant = HimalForestLight,
    onSurfaceVariant = HimalTextSecondary,

    outline = HimalOutline,

    error = HimalError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(

    primary = HimalForestDarkTheme,
    onPrimary = Color(0xFF17391E),

    primaryContainer = HimalForestContainerDark,
    onPrimaryContainer = Color(0xFFC4E5C0),

    secondary = HimalEarthDarkTheme,
    onSecondary = Color(0xFF3B2E1D),

    secondaryContainer = HimalEarthContainerDark,
    onSecondaryContainer = Color(0xFFF0DCC1),

    tertiary = Color(0xFF93D39D),
    onTertiary = Color(0xFF113A1B),

    background = HimalDarkBackground,
    onBackground = HimalDarkTextPrimary,

    surface = HimalDarkSurface,
    onSurface = HimalDarkTextPrimary,

    surfaceVariant = HimalForestContainerDark,
    onSurfaceVariant = HimalDarkTextSecondary,

    outline = HimalDarkOutline,

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun HimalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}