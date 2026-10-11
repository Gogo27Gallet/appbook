package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TextPrimary,
    onPrimary = SurfacePureWhite,
    primaryContainer = CardBackgroundLight,
    onPrimaryContainer = TextPrimary,
    secondary = TextSecondary,
    onSecondary = SurfacePureWhite,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfacePureWhite,
    onSurface = TextPrimary,
    surfaceVariant = CardBackgroundLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = SurfacePureWhite,
    onPrimary = TextPrimary,
    primaryContainer = Color(0xFF2C2D31),
    onPrimaryContainer = SurfacePureWhite,
    secondary = TextTertiary,
    onSecondary = TextPrimary,
    background = Color(0xFF131416),
    onBackground = SurfacePureWhite,
    surface = Color(0xFF1C1D21),
    onSurface = SurfacePureWhite,
    surfaceVariant = Color(0xFF25262B),
    onSurfaceVariant = TextTertiary,
    outline = Color(0xFF35373E)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Keep light theme matching screenshots by default unless dark system
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
