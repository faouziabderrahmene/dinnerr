package com.ftourlyoum.app.ui.theme

// ===================================================================
// Theme.kt — Material3 Theme Configuration
// ===================================================================
// Defines the app's color scheme, typography, and overall Material3
// theme. Uses a warm orange/amber palette matching the web version.
// ===================================================================

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════════════
// Color Palette — Matches the web version's warm gradient theme
// ═══════════════════════════════════════════════════════════════
val OrangeLight = Color(0xFFFFA726)
val OrangePrimary = Color(0xFFF97316)
val OrangeDark = Color(0xFFEA580C)
val RedAccent = Color(0xFFEF4444)
val PinkAccent = Color(0xFFEC4899)
val AmberGlow = Color(0xFFFFB74D)
val White = Color(0xFFFFFFFF)
val White80 = Color(0xCCFFFFFF)
val White60 = Color(0x99FFFFFF)
val White40 = Color(0x66FFFFFF)
val White20 = Color(0x33FFFFFF)
val White10 = Color(0x1AFFFFFF)

// Dark color scheme for the app (dark theme is the default)
private val AppColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = White,
    secondary = AmberGlow,
    onSecondary = Color(0xFF1A1A2E),
    tertiary = PinkAccent,
    background = Color(0xFF1A1A2E),
    surface = Color(0xFF1A1A2E),
    onBackground = White,
    onSurface = White,
)

// ═══════════════════════════════════════════════════════════════
// Typography — Clean, bold Arabic-friendly text styles
// ═══════════════════════════════════════════════════════════════
private val AppTypography = Typography(
    // Main title text style
    displayLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 40.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        color = White
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 42.sp,
        letterSpacing = 0.sp,
        color = White
    ),
    // Subtitle
    titleLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = White60
    ),
    // Button text
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = OrangePrimary
    ),
    // Body text
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        color = White80
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = White40
    ),
)

/**
 * Main app theme composable.
 * Wraps content with Material3 theming (colors + typography).
 */
@Composable
fun FtourLyoumTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}