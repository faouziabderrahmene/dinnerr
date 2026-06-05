package com.ftourlyoum.app.ui.components

// ===================================================================
// ResultDialog.kt — Animated Result Modal
// ===================================================================
// A beautifully animated full-screen dialog that displays the randomly
// selected dish. Features:
//   - Animated backdrop with blur/dim
//   - Spring-animated modal card entrance
//   - Slot machine spinning animation
//   - Confetti celebration on reveal
//   - Gradient glassmorphism design
//   - Close button (X icon) and "حسنا" dismiss button
// ===================================================================

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftourlyoum.app.data.Dish

/**
 * Full-screen animated result dialog.
 *
 * @param isOpen       Whether the dialog is visible
 * @param dish         The selected dish to display
 * @param isSpinning   Whether the slot machine is spinning
 * @param showConfetti Whether to show confetti celebration
 * @param onSpinComplete Called when spinning animation ends
 * @param onClose      Called when user dismisses the dialog
 */
@Composable
fun ResultDialog(
    isOpen: Boolean,
    dish: Dish?,
    isSpinning: Boolean,
    showConfetti: Boolean,
    onSpinComplete: () -> Unit,
    onClose: () -> Unit
) {
    if (dish == null) return

    // ═══════════════════════════════════════════════════════════
    // Entrance / Exit Animation
    // ═══════════════════════════════════════════════════════════
    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // ─── Dark backdrop ───
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (!isSpinning) onClose()
                    }
            )

            // ─── Modal card ───
            ModalCard(
                dish = dish,
                isSpinning = isSpinning,
                onSpinComplete = onSpinComplete,
                onClose = onClose,
                modifier = Modifier.align(Alignment.Center)
            )

            // ─── Confetti overlay ───
            ConfettiEffect(isActive = showConfetti)
        }
    }
}

/**
 * The main modal card with gradient background and animated content.
 */
@Composable
private fun ModalCard(
    dish: Dish,
    isSpinning: Boolean,
    onSpinComplete: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Spring scale animation for entrance
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "modalScale"
    )

    Card(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xE6F59E0B), // amber-500 at 90%
                            Color(0xE6F97316), // orange-500 at 90%
                            Color(0xE6EF4444)  // red-500 at 90%
                        )
                    )
                )
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // ─── Close button (X) — visible when not spinning ───
                if (!isSpinning) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable { onClose() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✕",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ─── Top decorative emoji icon ───
                val iconScale by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "iconBounce"
                )
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(iconScale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isSpinning) "🎰" else "🎉",
                        fontSize = 40.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ─── Title text ───
                Text(
                    text = if (isSpinning) "جاري الاختيار..." else "!فطور ليوم هو",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── Slot machine display area ───
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(16.dp)
                ) {
                    SlotMachine(
                        finalDish = dish,
                        isSpinning = isSpinning,
                        onComplete = onSpinComplete
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ─── Result text + dismiss button (after spinning) ───
                AnimatedVisibility(
                    visible = !isSpinning,
                    enter = fadeIn(tween(400)) + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    exit = fadeOut(tween(200))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Result announcement
                        Text(
                            text = "فطور ليوم هو ${dish.name}",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // ─── "حسنا" dismiss button ───
                        Button(
                            onClick = onClose,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFF97316) // orange-500
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Text(
                                text = "👍 حسنا",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}