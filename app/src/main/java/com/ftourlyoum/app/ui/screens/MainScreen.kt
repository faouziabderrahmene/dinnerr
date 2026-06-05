package com.ftourlyoum.app.ui.screens

// ===================================================================
// MainScreen.kt — Main Screen Composable
// ===================================================================
// The primary screen of the app. Displays:
//   1. Animated gradient background with floating orbs
//   2. Floating food emoji particles
//   3. "🍽️" icon with green status dot
//   4. "زعما شنوا فطور ليوم؟" title with shimmer effect
//   5. Subtitle text
//   6. "فكرة الفطور" action button with pulse glow
//   7. Previous pick card (after first selection)
//   8. Result dialog with slot machine animation
//
// All animations use Jetpack Compose animation APIs:
//   - animateFloatAsState for springs
//   - InfiniteTransition for continuous animations
//   - AnimatedVisibility for enter/exit
//   - LaunchedEffect for coroutine-based timing
// ===================================================================

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftourlyoum.app.data.Dish
import com.ftourlyoum.app.data.getRandomDish
import com.ftourlyoum.app.ui.components.AnimatedBackground
import com.ftourlyoum.app.ui.components.FloatingParticles
import com.ftourlyoum.app.ui.components.ResultDialog
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    // ═══════════════════════════════════════════════════════════
    // State Management
    // ═══════════════════════════════════════════════════════════
    var selectedDish by remember { mutableStateOf<Dish?>(null) }
    var isModalOpen by remember { mutableStateOf(false) }
    var isSpinning by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }
    var isButtonDisabled by remember { mutableStateOf(false) }
    var previousDish by remember { mutableStateOf<Dish?>(null) }

    // ═══════════════════════════════════════════════════════════
    // Entrance Animations (staggered appearance)
    // ═══════════════════════════════════════════════════════════
    var showIcon by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showSubtitle by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    var showFooter by remember { mutableStateOf(false) }

    // Stagger the entrance animations
    LaunchedEffect(Unit) {
        delay(300)
        showIcon = true
        delay(200)
        showTitle = true
        delay(200)
        showSubtitle = true
        delay(200)
        showButton = true
        delay(300)
        showFooter = true
    }

    // ═══════════════════════════════════════════════════════════
    // Main Layout — Layered composables
    // ═══════════════════════════════════════════════════════════
    Box(modifier = Modifier.fillMaxSize()) {
        // Layer 1: Animated gradient background
        AnimatedBackground()

        // Layer 2: Floating food particles
        FloatingParticles()

        // Layer 3: Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ─── Icon with green status dot ───
            AnimatedVisibility(
                visible = showIcon,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn()
            ) {
                IconWithBadge()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ─── Main title ───
            AnimatedVisibility(
                visible = showTitle,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(600)
                ) + fadeIn(tween(600))
            ) {
                TitleText()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Subtitle ───
            AnimatedVisibility(
                visible = showSubtitle,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(500)
                ) + fadeIn(tween(500))
            ) {
                Text(
                    text = "خلّي التطبيق يختارلك وصفة بالعشوائي 🎲",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ─── Main action button ───
            AnimatedVisibility(
                visible = showButton,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(600)
                ) + fadeIn(tween(600))
            ) {
                ActionButton(
                    enabled = !isButtonDisabled,
                    onClick = {
                        if (isButtonDisabled) return@ActionButton

                        // Pick a random dish
                        val dish = getRandomDish(previousDish)
                        selectedDish = dish
                        isModalOpen = true
                        isSpinning = true
                        showConfetti = false
                        isButtonDisabled = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ─── Previous pick card ───
            AnimatedVisibility(
                visible = previousDish != null && !isModalOpen,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it / 4 }
                ) + fadeOut()
            ) {
                previousDish?.let { dish ->
                    PreviousPickCard(dish = dish)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ─── Footer ───
            AnimatedVisibility(
                visible = showFooter,
                enter = fadeIn(tween(800))
            ) {
                Text(
                    text = "made by louay ♡",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        // Layer 4: Result dialog overlay
        ResultDialog(
            isOpen = isModalOpen,
            dish = selectedDish,
            isSpinning = isSpinning,
            showConfetti = showConfetti,
            onSpinComplete = {
                isSpinning = false
                showConfetti = true
            },
            onClose = {
                isModalOpen = false
                previousDish = selectedDish
                isButtonDisabled = false
            }
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Sub-Composables
// ═══════════════════════════════════════════════════════════════

/**
 * The "🍽️" icon with rounded container and green status badge.
 */
@Composable
private fun IconWithBadge() {
    Box {
        // Main icon container — glassmorphism style
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🍽️", fontSize = 48.sp)
        }

        // Green status dot (top-right corner)
        val pulseAnim = rememberInfiniteTransition(label = "pulse")
        val pulseAlpha by pulseAnim.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseAlpha"
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 4.dp, y = (-4).dp)
                .size(22.dp)
                .clip(CircleShape)
                .background(Color(0xFF4ADE80)) // green-400
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .alpha(pulseAlpha)
            )
        }
    }
}

/**
 * Main title with two-line layout.
 * "زعما شنوا" on line 1, "فطور ليوم؟" on line 2 (with gold accent).
 */
@Composable
private fun TitleText() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "زعما شنوا",
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        // Second line with golden color accent
        val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
        val shimmerAlpha by shimmerTransition.animateFloat(
            initialValue = 0.7f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "shimmerAlpha"
        )
        Text(
            text = "فطور ليوم؟",
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFFD700).copy(alpha = shimmerAlpha),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * The main "فكرة الفطور" button with:
 *   - Press scale animation (shrink on press)
 *   - Pulse glow shadow effect
 *   - Animated 🍳 emoji wiggle
 */
@Composable
private fun ActionButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    // ─── Interaction tracking for press animation ───
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale animation: 1.0 normally, 0.95 when pressed
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )

    // ─── Pulse glow animation for the button shadow ───
    val glowTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by glowTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    // ─── Emoji wiggle animation ───
    val emojiRotation by glowTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emojiWiggle"
    )

    Box(
        modifier = Modifier
            .scale(buttonScale)
            .shadow(
                elevation = (16 * glowAlpha).dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0xFFFFA500).copy(alpha = glowAlpha),
                spotColor = Color(0xFFFFA500).copy(alpha = glowAlpha)
            )
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFF97316),
                disabledContainerColor = Color.White.copy(alpha = 0.6f),
                disabledContentColor = Color(0xFFF97316).copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 12.dp,
                pressedElevation = 4.dp
            ),
            interactionSource = interactionSource
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Animated cooking emoji
                Text(
                    text = "🍳",
                    fontSize = 28.sp,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .scale(1f)
                        .then(
                            Modifier.offset(
                                y = (kotlin.math.sin(
                                    emojiRotation.toDouble() * Math.PI / 20
                                ) * 2).dp
                            )
                        )
                )

                Text(
                    text = "فكرة الفطور",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Card showing the previously selected dish.
 * Glassmorphism style with subtle border.
 */
@Composable
private fun PreviousPickCard(dish: Dish) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "آخر اختيار:",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = dish.emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dish.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}