package com.example.arcanavault.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val Typography = androidx.compose.material3.Typography()
val Shapes = androidx.compose.material3.Shapes()

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    background = md_theme_light_background,
    surface = md_theme_light_surface
    // ... fill in the rest of your color roles if needed
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    background = md_theme_dark_background,
    surface = md_theme_dark_surface
    // ... fill in the rest of your color roles
)

@Composable
fun ArcanaVaultTheme(
    darkTheme: Boolean = false,  // pass in whether dark theme should be used
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = if (darkTheme) DarkColors else LightColors

    // Wrap MaterialTheme with your colorScheme, typography, etc.
    androidx.compose.material3.MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // define or import your Typography
        shapes = Shapes,         // define or import your Shapes
        content = content
    )
}
