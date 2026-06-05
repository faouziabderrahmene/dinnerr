package com.ftourlyoum.app.ui.components

// ===================================================================
// FloatingParticles.kt — Animated Food Emoji Particles
// ===================================================================
// Renders animated floating food emoji particles in the background.
// Each particle has a random position, size, and animation timing
// for a natural, organic floating effect.
// ===================================================================

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Food-related emojis for background particles
private val PARTICLE_EMOJIS = listOf(
    "🍽️", "🥄", "🍴", "🫕", "🍲", "🥘", "🥗",
    "🍳", "🧆", "🫓", "🥙", "🌶️", "🫒", "🍋"
)

/**
 * Data class representing a single floating particle.
 */
private data class Particle(
    val emoji: String,
    val xFraction: Float,   // 0..1 fraction of screen width
    val yFraction: Float,   // 0..1 fraction of screen height
    val size: Float,         // Font size in sp
    val opacity: Float,      // Alpha value 0..1
    val duration: Int,       // Animation duration in ms
    val delay: Int           // Animation start delay in ms
)

@Composable
fun FloatingParticles() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    // Generate particles once (stable across recompositions)
    val particles = remember {
        List(16) { i ->
            Particle(
                emoji = PARTICLE_EMOJIS[i % PARTICLE_EMOJIS.size],
                xFraction = Math.random().toFloat(),
                yFraction = Math.random().toFloat(),
                size = 16f + (Math.random() * 20).toFloat(),
                opacity = 0.08f + (Math.random() * 0.12).toFloat(),
                duration = 8000 + (Math.random() * 8000).toInt(),
                delay = (Math.random() * 5000).toInt()
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            FloatingParticleItem(
                particle = particle,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }
    }
}

/**
 * Individual floating particle with infinite Y/X/rotation animation.
 */
@Composable
private fun FloatingParticleItem(
    particle: Particle,
    screenWidth: Int,
    screenHeight: Int
) {
    // Create infinite transition for this particle
    val infiniteTransition = rememberInfiniteTransition(
        label = "particle_${particle.emoji}_${particle.xFraction}"
    )

    // Animate vertical floating motion
    val yOffset by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = particle.duration,
                delayMillis = particle.delay,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yFloat"
    )

    // Animate horizontal sway
    val xOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = particle.duration + 1000,
                delayMillis = particle.delay + 500,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "xFloat"
    )

    // Animate gentle rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = particle.duration + 2000,
                delayMillis = particle.delay,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Text(
        text = particle.emoji,
        fontSize = particle.size.sp,
        modifier = Modifier
            .offset(
                x = (screenWidth * particle.xFraction + xOffset).dp,
                y = (screenHeight * particle.yFraction + yOffset).dp
            )
            .alpha(particle.opacity)
            .rotate(rotation)
    )
}