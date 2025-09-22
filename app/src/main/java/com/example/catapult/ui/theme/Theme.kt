package com.example.catapult.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

val LightColorScheme = lightColorScheme(
    primary = Pink40,
    onPrimary = Color.White,
    secondary = Purple40,
    onSecondary = Color.White,
    background = Grey10,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

// Dark tema
val DarkColorScheme = darkColorScheme(
    primary = Pink80,
    onPrimary = Color.Black,
    secondary = Purple80,
    onSecondary = Color.Black,
    background = Grey90,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatapultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

