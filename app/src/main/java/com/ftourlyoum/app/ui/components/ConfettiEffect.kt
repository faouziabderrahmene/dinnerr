package com.ftourlyoum.app.ui.components

// ===================================================================
// ConfettiEffect.kt — Confetti Celebration Animation
// ===================================================================
// Renders a burst of colorful confetti particles that fall from the
// top of the screen when a dish is revealed. Each particle has a
// random color, size, position, and fall trajectory.
// ===================================================================

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════
// Vibrant confetti colors matching the web version
// ═══════════════════════════════════════════════════════════════
private val CONFETTI_COLORS = listOf(
    Color(0xFFFF6B6B), Color(0xFFFFE66D), Color(0xFF4ECDC4),
    Color(0xFF45B7D1), Color(0xFF96CEB4), Color(0xFFFFEAA7),
    Color(0xFFDDA0DD), Color(0xFF98D8C8), Color(0xFFF7DC6F),
    Color(0xFFBB8FCE), Color(0xFFFF9FF3), Color(0xFF54A0FF),
    Color(0xFF5F27CD), Color(0xFF01A3A4), Color(0xFFF368E0)
)

/**
 * Data class for a single confetti particle.
 */
private data class ConfettiPiece(
    val color: Color,
    val xFraction: Float,     // Start X as fraction of screen width
    val size: Float,           // Size in pixels
    val delay: Int,            // Animation delay in ms
    val rotation: Float,       // Target rotation angle
    val isCircle: Boolean,     // Shape: circle or rectangle
    val horizontalDrift: Float // Lateral movement during fall
)

/**
 * Displays a confetti celebration animation.
 *
 * @param isActive When true, confetti particles animate from top to bottom
 */
@Composable
fun ConfettiEffect(isActive: Boolean) {
    if (!isActive) return

    // Generate confetti pieces (stable across recompositions)
    val pieces = remember {
        List(40) { i ->
            ConfettiPiece(
                color = CONFETTI_COLORS[i % CONFETTI_COLORS.size],
                xFraction = -0.1f + (Math.random() * 1.2).toFloat(),
                size = 6f + (Math.random() * 10).toFloat(),
                delay = (Math.random() * 500).toInt(),
                rotation = (Math.random() * 720 - 360).toFloat(),
                isCircle = Math.random() > 0.5,
                horizontalDrift = ((Math.random() - 0.5) * 200).toFloat()
            )
        }
    }

    // Main fall animation (0 → 1 over duration)
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(isActive) {
        animatable.snapTo(0f)
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = EaseOut
            )
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val progress = animatable.value

        pieces.forEach { piece ->
            // Calculate delayed progress for staggered start
            val pieceDelay = piece.delay / 3000f
            val pieceProgress = ((progress - pieceDelay) / (1f - pieceDelay))
                .coerceIn(0f, 1f)

            if (pieceProgress <= 0f) return@forEach

            // Calculate position
            val x = width * piece.xFraction + piece.horizontalDrift * pieceProgress
            val y = -20f + (height + 100f) * pieceProgress

            // Fade out near the end
            val alpha = if (pieceProgress > 0.7f) {
                1f - ((pieceProgress - 0.7f) / 0.3f)
            } else 1f

            if (piece.isCircle) {
                drawCircle(
                    color = piece.color.copy(alpha = alpha),
                    radius = piece.size / 2f,
                    center = Offset(x, y)
                )
            } else {
                drawRect(
                    color = piece.color.copy(alpha = alpha),
                    topLeft = Offset(x, y),
                    size = Size(piece.size * 1.5f, piece.size)
                )
            }
        }
    }
}