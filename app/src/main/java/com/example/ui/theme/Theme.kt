package com.example.ui.theme

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
    primary = FreshGreenDark,
    onPrimary = FreshGreenOnDark,
    primaryContainer = FreshGreenDarkContainer,
    onPrimaryContainer = FreshGreenOnDarkContainer,
    secondary = CarrotOrangeDark,
    onSecondary = CarrotOrangeOnDark,
    secondaryContainer = CarrotOrangeDarkContainer,
    onSecondaryContainer = CarrotOrangeOnDarkContainer,
    tertiary = LeafMintDark,
    onTertiary = LeafMintOnDark,
    tertiaryContainer = LeafMintDarkContainer,
    onTertiaryContainer = LeafMintOnDarkContainer,
    background = FreshBackgroundDark,
    onBackground = FreshOnBackgroundDark,
    surface = FreshSurfaceDark,
    onSurface = FreshOnSurfaceDark,
    surfaceVariant = FreshSurfaceVariantDark,
    onSurfaceVariant = FreshOnSurfaceVariantDark,
    outline = FreshOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = ForestGreenOnPrimary,
    primaryContainer = ForestGreenPrimaryContainer,
    onPrimaryContainer = ForestGreenOnPrimaryContainer,
    secondary = CarrotOrangeSecondary,
    onSecondary = CarrotOrangeOnSecondary,
    secondaryContainer = CarrotOrangeSecondaryContainer,
    onSecondaryContainer = CarrotOrangeOnSecondaryContainer,
    tertiary = LeafMintTertiary,
    onTertiary = LeafMintOnTertiary,
    tertiaryContainer = LeafMintTertiaryContainer,
    onTertiaryContainer = LeafMintOnTertiaryContainer,
    background = FreshBackgroundLight,
    onBackground = FreshOnBackgroundLight,
    surface = FreshSurfaceLight,
    onSurface = FreshOnSurfaceLight,
    surfaceVariant = FreshSurfaceVariantLight,
    onSurfaceVariant = FreshOnSurfaceVariantLight,
    outline = FreshOutlineLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false by default so our signature fresh green brand shines!
    content: @Composable () -> Unit
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
        content = content
    )
}
