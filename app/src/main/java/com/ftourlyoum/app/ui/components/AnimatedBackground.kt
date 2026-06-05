package com.ftourlyoum.app.ui.components

// ===================================================================
// AnimatedBackground.kt — Animated Gradient Background
// ===================================================================
// Renders a beautiful animated gradient background with floating
// blurred orbs, matching the web version's visual design.
// ===================================================================

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedBackground() {
    // ═══════════════════════════════════════════════════════════
    // Infinite transition for smooth, continuous gradient shifts
    // ═══════════════════════════════════════════════════════════
    val infiniteTransition = rememberInfiniteTransition(label = "bg")

    // Animate the gradient offset to create a shifting effect
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientShift"
    )

    // Animate floating orb positions
    val orbOffset1 by infiniteTransition.animateFloat(
        initialValue = -0.1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )

    val orbOffset2 by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = -0.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )

    val orbOffset3 by infiniteTransition.animateFloat(
        initialValue = -0.06f,
        targetValue = 0.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb3"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // ═══════════════════════════════════════════════════════
        // Main animated gradient background
        // Colors: orange-600 → red-500 → pink-600
        // ═══════════════════════════════════════════════════════
        val gradientBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFEA580C),  // orange-600
                Color(0xFFEF4444),  // red-500
                Color(0xFFDB2777),  // pink-600
                Color(0xFFF97316),  // orange-500
            ),
            start = Offset(width * gradientOffset, 0f),
            end = Offset(width * (1f - gradientOffset), height)
        )
        drawRect(brush = gradientBrush)

        // ═══════════════════════════════════════════════════════
        // Floating blurred orbs (decorative background circles)
        // ═══════════════════════════════════════════════════════

        // Orb 1: Yellow — top right
        drawCircle(
            color = Color(0x33FACC15), // yellow-400 at 20% opacity
            radius = width * 0.45f,
            center = Offset(
                width * 0.85f,
                height * (0.15f + orbOffset1)
            )
        )

        // Orb 2: Purple — bottom left
        drawCircle(
            color = Color(0x33A855F7), // purple-500 at 20% opacity
            radius = width * 0.4f,
            center = Offset(
                width * 0.1f,
                height * (0.85f + orbOffset2)
            )
        )

        // Orb 3: Orange — center-right
        drawCircle(
            color = Color(0x26FDBA74), // orange-300 at 15% opacity
            radius = width * 0.3f,
            center = Offset(
                width * 0.65f,
                height * (0.45f + orbOffset3)
            )
        )
    }
}